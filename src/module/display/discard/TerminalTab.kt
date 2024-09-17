package module.display.discard

import module.SIDE
import module.engine.terminal.Terminal
import java.awt.BorderLayout
import javax.swing.JPanel
import javax.swing.JTextArea

class TerminalTab(val side: SIDE, val tabName: String = "New Tab"): JPanel() {
    val terminalOutput = JTextArea()
    val terminalInputField = TerminalExecuteField(side)
    val terminal = Terminal(terminalOutput)

    var tabVisible = true
        set(value) {
            terminalOutput.isVisible = value
            terminalInputField.isVisible = value
            field = value
        }
        get() {
            return field
        }

    init {
        layout = BorderLayout()
        terminalOutput.isEditable = false
        add(terminalOutput, BorderLayout.CENTER)
        add(terminalInputField, BorderLayout.SOUTH)

        terminalInputField.terminalExecuteButton.addActionListener {
            terminal.executeCommand(terminalInputField.terminalInput.text)
            terminalInputField.terminalInput.text = ""
        }
    }

}