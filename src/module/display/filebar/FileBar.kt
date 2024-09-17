package module.display.filebar

import module.SIDE
import java.awt.Color
import java.awt.Dimension
import javax.swing.JPanel
import javax.swing.border.LineBorder

class FileBar(private val side: SIDE) : JPanel() {
    private val settings = side.settingsJsonObject!!

    init {
        val preferredWidth = settings.getPrimitiveAsInt("ideWindow.fileBar.preferredWidth")
        val preferredHeight = settings.getPrimitiveAsInt("ideWindow.fileBar.preferredHeight")
        preferredSize = Dimension(preferredWidth, preferredHeight)
        border =
            if (settings.getPrimitiveAsBoolean("ideWindow.fileBar.border.enabled")) {
                LineBorder(Color.BLACK, settings.getPrimitiveAsInt("ideWindow.fileBar.border.width"))
            } else {
                null
            }

        palletApply()
    }

    private fun palletApply() {
        val pallet = side.pallet!!
        background = pallet.getPallet("ideWindow.fileBar.background")
        foreground = pallet.getPallet("ideWindow.fileBar.foreground")
        border?.let { border = LineBorder(pallet.getPallet("ideWindow.fileBar.border")) }
    }
}