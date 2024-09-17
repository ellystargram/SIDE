package module.display

import module.SIDE
import module.display.filebar.FileBar
import module.display.terminalbar.TerminalBar
import javax.swing.JFrame

class Window(private val side: SIDE) : JFrame() {
    private val settings = side.settingsJsonObject!!
    private var fileBar: FileBar? = null
    var terminalBar: TerminalBar? = null

    init {
        title = settings.getPrimitiveAsString("ideWindow.title")
        val width = settings.getPrimitiveAsInt("ideWindow.width")
        val height = settings.getPrimitiveAsInt("ideWindow.height")
        setSize(width, height)
        defaultCloseOperation = EXIT_ON_CLOSE
        isVisible = true

        addFileBar()
        addTerminalBar()

        palletApply()
    }

    private fun palletApply() {
        val pallet = side.pallet!!
        contentPane.background = pallet.getPallet("ideWindow.background")
        foreground = pallet.getPallet("ideWindow.foreground")
    }

    private fun addFileBar() {
        if (settings.getPrimitiveAsBoolean("ideWindow.fileBar.enabled")) {
            fileBar = FileBar(side)
            add(fileBar!!, settings.getPrimitiveAsString("ideWindow.fileBar.location"))
        }
    }

    private fun addTerminalBar() {
        if (settings.getPrimitiveAsBoolean("ideWindow.terminalBar.enabled")) {
            terminalBar = TerminalBar(side)
            add(terminalBar!!, settings.getPrimitiveAsString("ideWindow.terminalBar.location"))
        }
    }
}