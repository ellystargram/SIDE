package module.display.terminalbar

import module.SIDE
import module.display.terminalbar.tab.TerminalTab
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JPanel

class TerminalBar(private val side: SIDE): JPanel() {
    private val settings = side.settingsJsonObject!!
    private val tabController = TabController(side)
    init {
        val preferredWidth = settings.getPrimitiveAsInt("ideWindow.terminalBar.preferredWidth")
        val preferredHeight = settings.getPrimitiveAsInt("ideWindow.terminalBar.preferredHeight")
        preferredSize = Dimension(preferredWidth, preferredHeight)

        layout = BorderLayout()
        add(tabController, BorderLayout.NORTH)

        val ideTerminal = TerminalTab(side, "IDE Terminal")
        ideTerminal.isClosable = false
//        tabController.addTab(ideTerminal)
        tabController.tabList.add(ideTerminal)
        tabController.tabComboBox.addItem(ideTerminal.tabName)
        tabController.tabComboBox.selectedIndex = 0
        add(ideTerminal, BorderLayout.CENTER)

        applyPallet()
    }

    private fun applyPallet() {
        val pallet = side.pallet!!
        background = pallet.getPallet("ideWindow.terminalBar.background")
        foreground = pallet.getPallet("ideWindow.terminalBar.foreground")
    }
}