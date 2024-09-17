package module.display.terminalbar

import module.SIDE
import module.display.terminalbar.tab.TerminalTab
import module.engine.terminal.getBasicShellName
import java.awt.BorderLayout
import java.awt.GridLayout
import javax.swing.JButton
import javax.swing.JComboBox
import javax.swing.JLabel
import javax.swing.JPanel

class TabController(private val side: SIDE):JPanel() {
    val tabList = mutableListOf<TerminalTab>()
    val tabComboBox = JComboBox<String>()
    val addShellTerminalButton = JButton("+")
    val removeShellTerminalButton = JButton("-")

    init {
        layout = GridLayout(1, 5)
        val tabsLabel = JLabel("Tabs:")
        tabsLabel.horizontalAlignment = JLabel.RIGHT
        add(tabsLabel)
        add(tabComboBox)
        val shellTabLabel = JLabel("${getBasicShellName()[0]} Tab:")
        shellTabLabel.horizontalAlignment = JLabel.RIGHT
        add(shellTabLabel)
        add(addShellTerminalButton)
        add(removeShellTerminalButton)

        addShellTerminalButton.addActionListener {
            val terminalTab = TerminalTab(side, findNoOverlappingName(getBasicShellName()[0]))
            terminalTab.activateShell()
            side.ideWindow?.terminalBar?.add(terminalTab, BorderLayout.CENTER)
            tabList.add(terminalTab)
            tabComboBox.addItem(terminalTab.tabName)
            tabComboBox.selectedIndex = tabList.size - 1
        }

        removeShellTerminalButton.addActionListener {
            val tabIndex = tabComboBox.selectedIndex
            removeTab(tabList[tabIndex])
        }

        tabComboBox.addActionListener {
            val tabIndex = tabComboBox.selectedIndex

            tabList.forEachIndexed { index, terminalTab ->
                terminalTab.isVisible = index == tabIndex
            }
            updateUI()
            revalidate()
        }

        applyPallet()
    }

    fun updateComboBox() {
        tabComboBox.removeAllItems()
        tabList.forEach { tabComboBox.addItem(it.tabName) }
    }

    fun addTab(tab: TerminalTab) {
        tabList.add(tab)
        side.ideWindow?.terminalBar?.add(tab, BorderLayout.CENTER)
        updateComboBox()
    }

    fun removeTab(tab: TerminalTab) {
        if (tab.isClosable == true){
            val tabIndex = tabList.indexOf(tab)
            side.ideWindow?.terminalBar?.remove(tab)
            tabList.remove(tab)
            updateComboBox()
            tabComboBox.selectedIndex = tabIndex-1
        }
    }

    private fun applyPallet(){
        val pallet = side.pallet!!
        background = pallet.getPallet("ideWindow.terminalBar.controller.background")
        foreground = pallet.getPallet("ideWindow.terminalBar.controller.foreground")
    }

    private fun findNoOverlappingName(name: String): String {
        var newName = name
        var i = 1
        while (tabList.any { it.tabName == newName }) {
            newName = "$name($i)"
            i++
        }
        return newName
    }
}