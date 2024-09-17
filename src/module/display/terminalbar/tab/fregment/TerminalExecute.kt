package module.display.terminalbar.tab.fregment

import module.SIDE
import java.awt.BorderLayout
import java.awt.Font
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JTextField

class TerminalExecute(private val side: SIDE): JPanel() {
    private val settings = side.settingsJsonObject!!
    var commandPrompt = JTextField()
    val executeButton = JButton("Execute")
    init {
        layout = BorderLayout()
        add(commandPrompt, BorderLayout.CENTER)
        add(executeButton, BorderLayout.EAST)

        executeButton.addActionListener {
            val command = commandPrompt.text
            commandPrompt.text = ""
        }

        applyPallet()
        setFont()
    }

    private fun applyPallet() {
        val pallet = side.pallet!!
        background = pallet.getPallet("ideWindow.terminalBar.tab.prompt.background")
        foreground = pallet.getPallet("ideWindow.terminalBar.tab.prompt.foreground")
        commandPrompt.background = pallet.getPallet("ideWindow.terminalBar.tab.prompt.background")
        commandPrompt.foreground = pallet.getPallet("ideWindow.terminalBar.tab.prompt.foreground")
    }

    private fun setFont() {
        val fontName = settings.getPrimitiveAsString("ideWindow.terminalBar.tab.prompt.font.name")
        val fontSize = settings.getPrimitiveAsInt("ideWindow.terminalBar.tab.prompt.font.size")
        commandPrompt.font = Font(fontName, Font.PLAIN, fontSize)

        val buttonFontName = settings.getPrimitiveAsString("ideWindow.terminalBar.tab.executeButton.font.name")
        val buttonFontSize = settings.getPrimitiveAsInt("ideWindow.terminalBar.tab.executeButton.font.size")
        executeButton.font = Font(buttonFontName, Font.PLAIN, buttonFontSize)
    }
}