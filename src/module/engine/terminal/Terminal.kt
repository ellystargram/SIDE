package module.engine.terminal

import module.display.terminalPane.TerminalTabPane
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent

interface Terminal{
    var terminalTab: TerminalTabPane.Tab?
    fun setTab(tab: TerminalTabPane.Tab){
        terminalTab = tab
        tab.executeButton.addActionListener {
            val command = tab.prompt.text
            executeCommand(command)
            tab.prompt.text = ""
        }
        tab.prompt.addKeyListener(object: KeyAdapter(){
            override fun keyPressed(e: KeyEvent?) {
                super.keyPressed(e)
                if(e?.keyCode == KeyEvent.VK_ENTER){
                    val command = tab.prompt.text
                    executeCommand(command)
                    tab.prompt.text = ""
                }
            }
        })
    }
    fun executeCommand(command:String)
    fun close()
    fun open()
}