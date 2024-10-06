package module.display.terminalPane

import module.SIDE
import module.display.menuPane.MenuPane
import module.engine.terminal.SIDETerminal
import module.engine.terminal.SystemTerminal
import module.engine.terminal.Terminal
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Font
import javax.swing.*

class TerminalTabPane(private val side: SIDE) : JPanel() {
    private val settings = side.settingsJsonObject!!
    val tabMenuPane = MenuPane(side)
    private val tabPanel: TabPanel = TabPanel(side)

    init {
        val preferredWidth = settings.getPrimitiveAsInt("ideWindow.terminalTabPane.preferredWidth")
        val preferredHeight = settings.getPrimitiveAsInt("ideWindow.terminalTabPane.preferredHeight")
        preferredSize = Dimension(preferredWidth, preferredHeight)

        layout = BorderLayout()
        val tabControllerLocation = settings.getPrimitiveAsString("ideWindow.terminalTabPane.controller.location")
        add(tabMenuPane, tabControllerLocation)

        val tabMenu = MenuPane.Menu(side, "Tab")
        val closeTabMenuItem = MenuPane.Menu.MenuItem(side, "close Tab")
        closeTabMenuItem.addActionListener {
            tabPanel.removeTerminalTab()
        }
        tabMenu.add(closeTabMenuItem)
        tabMenuPane.add(tabMenu)

        tabMenuPane.add(MenuPane.Menu(side, "IDE"))

        val systemMenu = MenuPane.Menu(side, "System")
        val addSystemTerminalMenuItem = MenuPane.Menu.MenuItem(side, "open System Terminal")
        addSystemTerminalMenuItem.addActionListener {
            tabPanel.addSystemTerminalTab("System Terminal")
        }
        systemMenu.add(addSystemTerminalMenuItem)
        tabMenuPane.add(systemMenu)

        add(tabPanel, BorderLayout.CENTER)

        val ideTerminalTab = Tab(side, "SIDE Terminal")
        ideTerminalTab.isClosable = false
        val ideTerminal = SIDETerminal()
        ideTerminal.setTab(ideTerminalTab)
        ideTerminalTab.terminal = ideTerminal

        tabPanel.addTab(ideTerminalTab.tabName, ideTerminalTab)

        applyPallet()
    }

    private fun applyPallet() {
//        tabController.applyPallet()
        tabPanel.applyPallet()
    }

    inner class TabPanel(private val side: SIDE) : JTabbedPane() {
        private val settings = side.settingsJsonObject!!
        fun addSystemTerminalTab(tabName: String) {
            val systemTerminalTab = Tab(side, tabName)
            val systemTerminal = SystemTerminal()
            systemTerminal.setTab(systemTerminalTab)
            systemTerminalTab.terminal = systemTerminal
            addTab(tabName, systemTerminalTab)
        }

        fun removeTerminalTab() {
            val selectedTabIndex = selectedIndex
            if ((getComponentAt(selectedTabIndex) as Tab).isClosable) {
                (getComponentAt(selectedTabIndex) as Tab).close()
                removeTabAt(selectedTabIndex)
            }
        }

        fun applyPallet(){
            val pallet = side.pallet!!
            background = pallet.getPallet("ideWindow.terminalTabPane.tabPanel.background")
            foreground = pallet.getPallet("ideWindow.terminalTabPane.tabPanel.foreground")
            isOpaque = true
        }
    }

    inner class Tab(private val side: SIDE, val tabName: String) : JPanel() {
        private val settings = side.settingsJsonObject!!
        val log: JTextArea = JTextArea()
        val logScrollPane: JScrollPane = JScrollPane(log)
        val prompt: JTextField = JTextField()
        val executeButton: JButton = JButton("execute")
        val enterSidePanel: JPanel = JPanel()
        var isClosable: Boolean = true
        var terminal: Terminal? = null

        init {
            log.isEditable = false

            layout = BorderLayout()
            add(logScrollPane, BorderLayout.CENTER)
            add(enterSidePanel, BorderLayout.SOUTH)

            enterSidePanel.layout = BorderLayout()
            enterSidePanel.add(prompt, BorderLayout.CENTER)
            enterSidePanel.add(executeButton, BorderLayout.EAST)

            setFont()

            applyPallet()
        }

        fun close() {
            terminal?.close()
        }

        private fun setFont() {
            val logFontSize = settings.getPrimitiveAsInt("ideWindow.terminalTabPane.tab.log.font.size")
            val logFontName = settings.getPrimitiveAsString("ideWindow.terminalTabPane.tab.log.font.name")
            log.font = Font(logFontName, Font.PLAIN, logFontSize)

            val promptFontSize = settings.getPrimitiveAsInt("ideWindow.terminalTabPane.tab.prompt.font.size")
            val promptFontName = settings.getPrimitiveAsString("ideWindow.terminalTabPane.tab.prompt.font.name")
            prompt.font = Font(promptFontName, Font.PLAIN, promptFontSize)

            val executeButtonFontSize =
                settings.getPrimitiveAsInt("ideWindow.terminalTabPane.tab.executeButton.font.size")
            val executeButtonFontName =
                settings.getPrimitiveAsString("ideWindow.terminalTabPane.tab.executeButton.font.name")
            executeButton.font = Font(executeButtonFontName, Font.PLAIN, executeButtonFontSize)
        }

        private fun applyPallet(){
            val pallet = side.pallet!!
            background = pallet.getPallet("ideWindow.terminalTabPane.tab.background")
            foreground = pallet.getPallet("ideWindow.terminalTabPane.tab.foreground")
            log.background = pallet.getPallet("ideWindow.terminalTabPane.tab.log.background")
            log.foreground = pallet.getPallet("ideWindow.terminalTabPane.tab.log.foreground")
            prompt.background = pallet.getPallet("ideWindow.terminalTabPane.tab.prompt.background")
            prompt.foreground = pallet.getPallet("ideWindow.terminalTabPane.tab.prompt.foreground")
            executeButton.background = pallet.getPallet("ideWindow.terminalTabPane.tab.executeButton.background")
            executeButton.foreground = pallet.getPallet("ideWindow.terminalTabPane.tab.executeButton.foreground")
        }
    }
}
