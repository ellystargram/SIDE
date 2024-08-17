package module

import module.codespace.CodeSpace
import module.commandbar.CommandBar
import module.pallet.Pallet
import module.settings.Settings
import module.handlebar.HandleBar
import javax.swing.JFrame

class SIDE : JFrame() {
    private val settings: Settings = Settings()
    private val pallet = Pallet(settings)
    val handleBar = HandleBar(settings, pallet)
    val codeSpace = CodeSpace(settings, pallet)
    var projectName: String? = null
    private val commandBarFiled = CommandBar(settings, pallet, this)

    init {
        title = settings.getSettingOfString("version")
        setLocationRelativeTo(null)
        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(settings.getSettingOfInt("general.width"), settings.getSettingOfInt("general.height"))
        if (settings.getSettingOfBoolean("general.isFullscreen")) {
            isUndecorated = true
            extendedState = MAXIMIZED_BOTH
        }
        contentPane.background = pallet.getPallet("ide.background")
        handleBar.addMovingListener(this)
        add(commandBarFiled, "South")
        add(handleBar, "North")
        add(codeSpace, "Center")

        isVisible = true

//        pallet.getPallet("asdf")
//        pallet.getPallet("asdf.asdf")
    }


}

fun main() {
    val side = SIDE()
}