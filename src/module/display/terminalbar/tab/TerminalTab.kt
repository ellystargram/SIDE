package module.display.terminalbar.tab

import module.SIDE
import module.display.terminalbar.tab.fregment.TerminalExecute
import module.display.terminalbar.tab.fregment.TerminalLog
import module.engine.terminal.Terminal
import java.awt.BorderLayout
import javax.swing.JPanel
import javax.swing.JTextArea

class TerminalTab(private val side: SIDE, var tabName: String) : JPanel() {
    private val settings = side.settingsJsonObject!!
    var isClosable = true
    val terminalLog = TerminalLog(side)
    val terminalExecute = TerminalExecute(side)
    var shell: Terminal? = null

    init {
        layout = BorderLayout()
        add(terminalLog, BorderLayout.CENTER)
        add(terminalExecute, BorderLayout.SOUTH)

        applyPallet()
    }

    fun activateShell(){
        val terminalOutput = JTextArea()
        shell = Terminal(terminalOutput)
        terminalLog.setViewportView(terminalOutput)
        terminalLog.terminalOutput = terminalOutput
        terminalLog.terminalOutput.isEditable = false
        terminalLog.applyPallet()
        terminalLog.setFont()
        terminalExecute.executeButton.addActionListener {
            val command = terminalExecute.commandPrompt.text
            shell?.executeCommand(command)
            terminalExecute.commandPrompt.text = ""
        }
    }

    private fun applyPallet() {
        val pallet = side.pallet!!
        background = pallet.getPallet("ideWindow.terminalBar.tab.background")
        foreground = pallet.getPallet("ideWindow.terminalBar.tab.foreground")
    }
}