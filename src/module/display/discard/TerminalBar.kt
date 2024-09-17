package module.display.discard

import module.SIDE
import java.awt.BorderLayout
import javax.swing.JButton
import javax.swing.JComboBox
import javax.swing.JPanel

class TerminalBar(private val side: SIDE) : JPanel() {
    private val settings = side.settingsJsonObject!!
    private val terminalTabCombo = JComboBox<String>()
    private val createNewShellTabButton = JButton("New Shell")
    private val terminalTabList = mutableListOf<TerminalTab>()
    private val terminalPanel = JPanel()

    init {
        val preferredWidth = settings.getPrimitiveAsInt("ideWindow.terminalBar.preferredWidth")
        val preferredHeight = settings.getPrimitiveAsInt("ideWindow.terminalBar.preferredHeight")
        preferredSize = java.awt.Dimension(preferredWidth, preferredHeight)

        layout = BorderLayout()
        terminalTabList.add(TerminalTab(side, "IDE Terminal"))
        terminalTabList.add(TerminalTab(side, "OS Terminal"))
        terminalTabList.add(TerminalTab(side, "Shell Terminal"))

        terminalTabCombo.addActionListener {
            val selectedItemIndex = terminalTabCombo.selectedIndex
            viewTerminalTab(selectedItemIndex)
        }

        updateTerminalTabCombo()
        terminalTabCombo.selectedIndex = 0

        add(terminalTabCombo, BorderLayout.NORTH)
        add(terminalPanel, BorderLayout.CENTER)
        terminalPanel.layout = BorderLayout()
    }

    fun viewTerminalTab(name: String) {
        terminalPanel.removeAll()
        terminalTabList.forEach {
            it.tabVisible = it.tabName == name
            if (it.tabVisible) {
                terminalPanel.add(it, BorderLayout.CENTER)
            }
        }
        updateUI()
        revalidate()
    }

    fun viewTerminalTab(index: Int) {
        terminalPanel.removeAll()
        terminalTabList.forEachIndexed { i, it ->
            it.tabVisible = i == index
            if (it.tabVisible) {
                terminalPanel.add(it, BorderLayout.CENTER)
            }
        }
        updateUI()
        revalidate()
    }

    private fun updateTerminalTabCombo() {
//        val currentTabIndex = terminalTabCombo.selectedIndex
//        val currentTabName = terminalTabCombo.selectedItem as String
        terminalTabCombo.removeAllItems()
        terminalTabList.forEach { terminalTabCombo.addItem(it.tabName) }
//        terminalTabCombo.selectedIndex = currentTabIndex
//        if (currentTabName != terminalTabCombo.selectedItem) {
//            viewTerminalTab(terminalTabCombo.selectedItem as String)
//        }
    }

//    private val settings = side.settingsJsonObject!!
//    private val terminalOutput = JTextArea()
////    private val terminalInput = JTextField()
//    private val terminalInput = TerminalExecuteField(side)
//    private val terminal = Terminal(terminalOutput)
//
//    init {
//        val preferredWidth = settings.getPrimitiveAsInt("ideWindow.terminalBar.preferredWidth")
//        val preferredHeight = settings.getPrimitiveAsInt("ideWindow.terminalBar.preferredHeight")
//        preferredSize = Dimension(preferredWidth, preferredHeight)
//        border = if (settings.getPrimitiveAsBoolean("ideWindow.terminalBar.border.enabled")) {
//            LineBorder(java.awt.Color.BLACK)
//        } else {
//            null
//        }
//        layout = BorderLayout()
//        add(terminalOutput, BorderLayout.CENTER)
//        add(terminalInput, BorderLayout.SOUTH)
//        terminalInput.terminalExecuteButton.addActionListener {
//            terminal.executeCommand(terminalInput.terminalInput.text)
//            terminalInput.terminalInput.text = ""
//        }
//        palletApply()
//    }
//
//    private fun palletApply() {
//        val pallet = side.pallet!!
//        background = pallet.getPallet("ideWindow.terminalBar.background")
//        foreground = pallet.getPallet("ideWindow.terminalBar.foreground")
//        border.let { border = LineBorder(pallet.getPallet("ideWindow.terminalBar.border")) }
//    }
}