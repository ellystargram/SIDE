package module.display.menuPane

import module.SIDE
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem

class MenuPane(private val side: SIDE) : JMenuBar() {
//    private val fileMenu = Menu(side, "File")
//    private val pluginMenu = Menu(side, "Plugin")
    val menus = mutableMapOf<String, Menu>()
    init {
//        add(fileMenu)
//        add(pluginMenu)
    }

    override fun add(c: JMenu?): JMenu {
        if (c is Menu) {
            menus.put(c.menuName, c)
        }
        return super.add(c)
    }

    class Menu(private val side: SIDE, val menuName:String) : JMenu(menuName) {
        val items = mutableMapOf<String, MenuItem>()
        init {

        }
        override fun add(c: JMenuItem?): JMenuItem {
            if (c is MenuItem) {
                items.put(c.itemName, c)
            }
            return super.add(c)
        }
        class MenuItem(private val side: SIDE, val itemName:String) : JMenuItem(itemName) {
            init {

            }
        }
    }
}