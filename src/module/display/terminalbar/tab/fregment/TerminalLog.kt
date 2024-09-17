package module.display.terminalbar.tab.fregment

import module.SIDE
import java.awt.Font
import javax.swing.JScrollPane
import javax.swing.JTextArea

class TerminalLog(private val side: SIDE): JScrollPane() {
    private val settings = side.settingsJsonObject!!
    var terminalOutput = JTextArea()
    init {
        terminalOutput.isEditable = false
        terminalOutput.lineWrap = true
        terminalOutput.wrapStyleWord = true

        add(terminalOutput)

        applyPallet()
    }

    fun applyPallet() {
        val pallet = side.pallet!!
        background = pallet.getPallet("ideWindow.terminalBar.tab.log.background")
        foreground = pallet.getPallet("ideWindow.terminalBar.tab.log.foreground")
        terminalOutput.background = pallet.getPallet("ideWindow.terminalBar.tab.log.background")
        terminalOutput.foreground = pallet.getPallet("ideWindow.terminalBar.tab.log.foreground")
    }

    fun setFont() {
        val fontName = settings.getPrimitiveAsString("ideWindow.terminalBar.tab.log.font.name")
        val fontSize = settings.getPrimitiveAsInt("ideWindow.terminalBar.tab.log.font.size")
        terminalOutput.font = Font(fontName, Font.PLAIN, fontSize)
    }
}