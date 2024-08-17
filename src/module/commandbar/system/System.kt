package module.commandbar.system

import module.SIDE
import module.commandbar.exceptions.DebugException
import module.commandbar.exceptions.FileException
import module.commandbar.exceptions.InvalidCommandException
import module.settings.Settings
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.File
import javax.swing.JFrame
import kotlin.system.exitProcess


class System(val settings: Settings) {

    private val systemCommandJsonAddress: String = javaClass.getResource(settings.getSettingOfString("commands.system.jsonAddress"))!!.readText()
    private var commands: HashMap<String, JSONArray> = HashMap()
    private var customCommands: HashMap<String, String> = HashMap()

    init {
        loadCommand()
    }

    fun loadCommand() {
        val jsonObject: JSONObject = JSONParser().parse(systemCommandJsonAddress) as JSONObject

        for (command in jsonObject) {
            if (command.key.toString() != "custom") {
                commands.put(command.key.toString(), command.value as JSONArray)
            }
        }

        val customArray = jsonObject["custom"] as JSONArray
        for (i in 0..<customArray.size) {
            val custom = customArray[i] as JSONObject
            val command = custom["command"] as String
            val action = custom["action"] as String
            customCommands[command] = action
        }
        val commandsToRemove = HashMap<String, String>() //삭제할 새끼 저장할 맵 <commandType, command>
        val keywords = ArrayList<String>()
        for (commandType in commands) { //키워드 선택(exit, save등)
            for (commandIndex in commandType.value) {//해당 키워드에 있는 명령어분신 선택(나가기 등)
                val command = commandIndex as String//명령어분신을 스트링으로 변환
                if (commands.contains(command)) {//.contains가 key를 포함하는지 찾는거니까, command가 키워드에 있는지 확인
                    commandsToRemove[commandType.key] = command//삭제할 새끼에 추가
                }
            }
            keywords.add(commandType.key)//키워드 추가
        }
        for (command in commandsToRemove) {//삭제할 새끼 하나씩 꺼내서
            println("$command is keyword!! so disabled!!")//있다면 욕한바가지 해주고
            commands[command.key]?.remove(command.value)//삭제
        }

        val customCommandToRemove = HashMap<String, String>()
        for (customCommand in customCommands) {
            val customCommandFragment = customCommand.key.split(" ")
            for (keyword in keywords) {
                for (fragment in customCommandFragment) {
                    if (commands[keyword]?.contains(fragment) == true || keyword == fragment) {
                        customCommandToRemove[customCommand.key] = customCommand.value
                        break
                    }
                }
                if (customCommandToRemove.containsKey(customCommand.key)) {
                    break
                }
            }
        }
        for (customCommand in customCommandToRemove) {
            println("${customCommand.key} is contains keyword!! so disabled!!")
            customCommands.remove(customCommand.key)
        }

        //커스텀 커맨드가 예약어랑 같으면 삭제? 그것도 중복되니까 잡아야겠지?
        //커스텀 커맨드에 예약어가 있으면 삭제 예약어도 exit, save같은걸 잡는거지 저장, 나가기 이런걸 잡진 않게 해놨는데 잡아야 하나?
        //이미 해놨네 이제 performSystemCommand 할차례
        //아 그건 했지
        //그럼 말고
        //원리... 가 뭐야 ㅇㅋ? 삭제를 보류했다가 한번에 삭제? 그렇군요ㅇㅇ 두번쨰 알고리즘이 바로 삭제였는데 그러니까 인덱스 문제나니까..
    }

    fun performSystemCommand(command: String, mainSide:SIDE): Boolean {
        val commandOperand = command.split(" ")[0]
        if (customCommands.contains(command)) {
            //TODO execute command(s) by alias action
            return true
        } else if (commandOperand == "exit" || commands["exit"]?.contains(commandOperand) == true) {
            exitProcess(0)
//            return true
        } else if (commandOperand == "save" || commands["save"]?.contains(commandOperand) == true) {
            //TODO save file
            val commandSplit = command.split(" ")
            if (commandSplit.size != 1) {
                throw InvalidCommandException(command, "Invalid command argument. Usage: save")
            }
            if (mainSide.projectName == null) {
                throw FileException(command, "File is not selected. Please save as")
            }
            val file = File(mainSide.projectName!!)
            if (!file.exists()) {
                throw FileException(command, "File not found")
            }
            file.writeText(mainSide.codeSpace.codeEditor.text)
            return true
        } else if (commandOperand == "save_as" || commands["save_as"]?.contains(commandOperand) == true) {
            val commandSplit = command.split(" ")
            if (commandSplit.size != 2) {
                throw InvalidCommandException(command, "Invalid command argument. Usage: save_as [file path]")
            }
            val file = File(commandSplit[1])
            file.writeText(mainSide.codeSpace.codeEditor.text)
            updateCurrentProjectName(mainSide, file.name, commandSplit[1])
            return true
        } else if(commandOperand == "load" || commands["load"]?.contains(commandOperand) == true){
            val commandSplit = command.split(" ")
            if(commandSplit.size != 2){
                throw InvalidCommandException(command, "Invalid command argument. Usage: load [file path]")
            }
            val file = File(commandSplit[1])
            if(!file.exists()){
                throw InvalidCommandException(command, "File not found")
            }
            val fileContent = file.readText()
            mainSide.codeSpace.codeEditor.text = fileContent
            updateCurrentProjectName(mainSide, file.name, commandSplit[1])
            return true
        }
        // alias command가 "exit asdf"면 어떻게되 이건 띄어쓰기까지 하나의 문자야 그렇지 명령어를 복합적으로 쓰기위해
        // 위에 split(" ")[0]가 있는데 alias에 띄어쓰기가 들어간 사용자 지정 명령이 있으면? goodㅋㅋㅋㅋ 돌려막기
        // 사실 나 alias는 아 건들여야 하나? 사용자 커스텀이 목적이잖아
        // 음 잡아주는게 좋을듯
        return false
    }

    private fun updateCurrentProjectName(mainSide: SIDE, projectName: String, name: String) {
        mainSide.projectName = projectName
        mainSide.handleBar.title.text = projectName
        val fileExtender = name.split(".")[name.split(".").size - 1]
        println("fileExtender: $fileExtender")
        mainSide.codeSpace.codeModeChange(getLanguageName(fileExtender))
    }

    private fun getLanguageName(extender: String): String {
        if (extender == "py") return "python"
        if (extender == "kt") return "kotlin"
        if (extender == "java") return "java"
        return ""
    }
}

//제안 checkSystemCommand 그래? 사실 근데 바로 적용도 시킬라 그랬는데? 그나저나 이것도 고민이 있어.
//이것도 json 만들어서, kv를 나가기:exit 이런식으로 하면 여러나라 언어를 지원할 수 있지 않을까?
//커맨드창에 입력할려면 사람들이 커맨드를 지정할 수 있게 json으로 커맨드를 불러오는거지
//{
//  "exit":"exit",
//  "나가기":"exit",
//  "종료":"exit"
//}
//커맨드도 사용자지정할 수 있게
// 기본적으로 난 그런 의미없는 단축키를 지원 안할 예정이긴 하지만, 사람들이 좋다면 json을 수정해서 사용할 수 있는거지 ㅇㅇ
// "wq":"exit" 굳 작업하러 가보자좋네



