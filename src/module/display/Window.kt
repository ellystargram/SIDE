package module.display

import module.SIDE
import javax.swing.JFrame

class Window(val side: SIDE) : JFrame() {
    private val settings = side.settingsJsonObject!!

    init {
        title = settings.getPrimitiveAsString("ideWindow.title")
        val width = settings.getPrimitiveAsInt("ideWindow.width")
        val height = settings.getPrimitiveAsInt("ideWindow.height")
        setSize(width, height)
        defaultCloseOperation = EXIT_ON_CLOSE
        isVisible = true
        palletApply()
    }

    private fun palletApply() {
        val pallet = side.pallet!!
        contentPane.background = pallet.getPallet("ideWindow.background")
        foreground = pallet.getPallet("ideWindow.foreground")
    }
}