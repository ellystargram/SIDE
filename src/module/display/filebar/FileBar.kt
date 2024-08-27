package module.display.filebar

import module.SIDE
import java.awt.Dimension
import javax.swing.JPanel

class FileBar(val side:  SIDE): JPanel() {
    private val settings = side.settingsJsonObject!!
    init {
        println("FileBar is initializing")
        palletApply()
        val width = settings.getPrimitiveAsInt("ideWindow.fileBar.preferredWidth")
        val height = settings.getPrimitiveAsInt("ideWindow.fileBar.preferredHeight")

        preferredSize = Dimension(width, height)

        val location = settings.getPrimitiveAsString("ideWindow.fileBar.location")
        side.ideWindow!!.add(this,location)
    }
    private fun palletApply(){
//        println("FileBar is applying pallet")
        val pallet = side.pallet!!
        background = pallet.getPallet("ideWindow.fileBar.background")
        foreground = pallet.getPallet("ideWindow.fileBar.foreground")
    }
}