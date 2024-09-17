package module.display.discard

import module.SIDE
import java.awt.BorderLayout
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JTextField

class TerminalExecuteField(private val side:SIDE):JPanel() {
    private val settings = side.settingsJsonObject!!
    val terminalInput = JTextField()
    val terminalExecuteButton = JButton("Execute")

    init {
        layout = BorderLayout()
        add(terminalInput, BorderLayout.CENTER)
        add(terminalExecuteButton, BorderLayout.EAST)

    }
}