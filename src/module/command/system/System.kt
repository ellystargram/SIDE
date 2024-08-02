package module.command.system

class System {

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

//catching fly...

fun checkSystem(command: String): Boolean {
    //TODO read json
    val systemCommandJsonAddress:String = "src/module/command/systemCommand.json"
    if (command == "exit") {
        //do something
        return true
    }
    return false
}