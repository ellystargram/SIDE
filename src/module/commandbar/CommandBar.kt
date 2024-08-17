package module.commandbar

import module.SIDE
import module.commandbar.exceptions.DebugException
import module.commandbar.exceptions.FileException
import module.commandbar.exceptions.InvalidCommandException
import module.commandbar.system.System

import module.pallet.Pallet
import module.settings.Settings
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Font
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.time.format.DateTimeFormatter
import javax.swing.*

class CommandBar(val settings: Settings, private val pallet: Pallet, private val mainSide:SIDE) : JPanel() {
    private val commandField = JTextField(settings.getSettingOfString("commandBar.prompt.text"))
    private val executeButton = JButton(settings.getSettingOfString("commandBar.executeButton.text"))
    private val enterPanel = JPanel()
    var commandHistory: ArrayList<String> = ArrayList()
    val historyList: JList<String> = JList(commandHistory.toTypedArray())
    val historyPane = JScrollPane(historyList)

    private val systemCommand: System = System(settings)

    init {
        layout = BorderLayout()
        enterPanel.layout = BorderLayout()
        enterPanel.add(commandField, BorderLayout.CENTER)
        enterPanel.add(executeButton, BorderLayout.EAST)
        add(enterPanel, BorderLayout.SOUTH)
//        add(historyPane, BorderLayout.CENTER)

        executeButton.addActionListener { executeButtonAction() }
        executeButton.preferredSize = Dimension(settings.getSettingOfInt("commandBar.executeButton.width"), settings.getSettingOfInt("commandBar.executeButton.height"))
        executeButton.font = Font(settings.getSettingOfString("commandBar.executeButton.font"), Font.BOLD, settings.getSettingOfInt("commandBar.executeButton.fontSize"))
        commandField.font = settings.getSettingOfFont("commandBar.prompt")
        commandField.preferredSize = Dimension(settings.getSettingOfInt("commandBar.prompt.width"), settings.getSettingOfInt("commandBar.prompt.height"))
        historyList.font = settings.getSettingOfFont("commandBar.history")
        historyPane.preferredSize = Dimension(settings.getSettingOfInt("commandBar.history.width"), settings.getSettingOfInt("commandBar.history.height"))
        historyList.layoutOrientation = JList.VERTICAL
        historyList.visibleRowCount = settings.getSettingOfInt("commandBar.history.max")
        historyList.addListSelectionListener {
            historyPane.verticalScrollBar.value = historyPane.verticalScrollBar.maximum
        }
        commandField.addKeyListener(object : KeyAdapter() {
            override fun keyPressed(e: KeyEvent) {
                super.keyPressed(e)
                if (e.keyCode == KeyEvent.VK_ENTER) {
                    executeButtonAction()
                }
            }
        })
        commandField.addFocusListener(object : FocusAdapter() {
            override fun focusGained(e: FocusEvent) {
                super.focusGained(e)
                add(historyPane, BorderLayout.CENTER)
                historyList.setListData(commandHistory.toTypedArray())
                SwingUtilities.invokeLater {
                    historyPane.verticalScrollBar.value = historyPane.verticalScrollBar.maximum
                }
                historyPane.isVisible = true
                //커맨드 입력줄 그거 만들었다니까?
                repaint()
                updateUI() //로그만 출력한거 아니야? ㅇㅇ 근데 이제 처리부를 만들어서 입력만 해주면 끝임 뭐만들게?
            } //이제 그럼 커맨드 입력부는 다 만들었는데 이걸 어떻게 처리할까? 여기 코드에 넣기엔 너무 드러워질거 같음

            override fun focusLost(e: FocusEvent?) {
                super.focusLost(e)
                remove(historyPane)// 포커스를 읽으면 기록이 안보이게 하고싶은데, 허엿게 있었던 자리에 남아있네?
                historyPane.isVisible = false

                repaint()
                updateUI()
            }
        })

        setPallet()
    }


    private fun setPallet() {
        //command.button
        executeButton.background = pallet.getPallet("commandBar.button.background")
        executeButton.foreground = pallet.getPallet("commandBar.button.foreground")
        executeButton.isOpaque = true
        //command.prompt
        commandField.background = pallet.getPallet("commandBar.prompt.background")
        commandField.foreground = pallet.getPallet("commandBar.prompt.foreground")
        commandField.isOpaque = true
        //list
        historyList.background = pallet.getPallet("commandBar.list.background")
        historyList.foreground = pallet.getPallet("commandBar.list.foreground")
    }

    private fun executeButtonAction() {
        val command = commandField.text
        var success = false
        var exceptioned = false
        var exceptionDescription = ""
        var exceptionCommand = ""

        try {
            if (systemCommand.performSystemCommand(command, mainSide)) success = true
        } catch (e:InvalidCommandException){
            exceptioned = true
            exceptionDescription = e.description
            exceptionCommand = e.problemCommand
        } catch (e:FileException){
            exceptioned = true
            exceptionDescription = e.description
            exceptionCommand = e.problemCommand
        } catch (e: Exception) {
            e.printStackTrace()
            exceptioned = true
            exceptionDescription = e.message.toString()
            exceptionCommand = command
        }

        val time = DateTimeFormatter.ofPattern("HH:mm:ss").format(java.time.LocalDateTime.now())

        if (success) commandHistory.add("<$time>: $command")
        else if (exceptioned) commandHistory.add("E <$time>: `$exceptionCommand` $exceptionDescription")
        else commandHistory.add("E <$time>: `$command`Command not found")

        historyList.setListData(commandHistory.toTypedArray())
        SwingUtilities.invokeLater {
            historyPane.verticalScrollBar.value = historyPane.verticalScrollBar.maximum
        }
        //
        commandField.text = ""

        repaint()
    }
}