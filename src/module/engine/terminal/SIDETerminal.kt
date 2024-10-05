package module.engine.terminal

import module.display.terminalPane.TerminalTabPane

class SIDETerminal:Terminal {
    override var terminalTab: TerminalTabPane.Tab? = null

    override fun setTab(tab: TerminalTabPane.Tab) {
        terminalTab = tab
        return
    }

    override fun executeCommand(command: String) {
        return
    }

    override fun close() {
        return
    }

    override fun open() {
        return
    }
}