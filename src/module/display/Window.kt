package module.display

import module.SIDE
import module.display.editor.EditorPane
import module.display.filebar.FileBar
import module.display.menuPane.MenuPane
import module.display.terminalPane.TerminalTabPane
import javax.swing.JFrame

class Window(private val side: SIDE) : JFrame() {
    private val settings = side.settingsJsonObject!!
    private var fileBar: FileBar? = null
//    var terminalBar: TerminalBar? = null
    var terminalTabPane: TerminalTabPane? = null
    var editorPane: EditorPane? =null
    val menuPane = MenuPane(side)

    init {
        title = settings.getPrimitiveAsString("ideWindow.title")
        val width = settings.getPrimitiveAsInt("ideWindow.width")
        val height = settings.getPrimitiveAsInt("ideWindow.height")
        setSize(width, height)
        defaultCloseOperation = EXIT_ON_CLOSE
        isVisible = true

        jMenuBar = menuPane

        addFileBar()
        addTerminalBar()
        addEditorPane()

        palletApply()
    }

    private fun palletApply() {
        val pallet = side.pallet!!
        contentPane.background = pallet.getPallet("ideWindow.background")
        foreground = pallet.getPallet("ideWindow.foreground")
    }

    private fun addFileBar() {
        if (settings.getPrimitiveAsBoolean("ideWindow.fileBar.enabled")) {
            fileBar = FileBar(side)
            add(fileBar!!, settings.getPrimitiveAsString("ideWindow.fileBar.location"))
        }
    }

    private fun addTerminalBar() {
        if (settings.getPrimitiveAsBoolean("ideWindow.terminalTabPane.enabled")) {
//            terminalBar = TerminalBar(side)
            terminalTabPane = TerminalTabPane(side)
//            add(terminalBar!!, settings.getPrimitiveAsString("ideWindow.terminalBar.location"))
            add(terminalTabPane!!, settings.getPrimitiveAsString("ideWindow.terminalTabPane.location"))
        }//ok display 패키지에는 ui적인거, engine 패키지에 알고리듬 같은거
    }

    private fun addEditorPane() {
        if (settings.getPrimitiveAsBoolean("ideWindow.editorPane.enabled")) {
            editorPane = EditorPane(side)
            add(editorPane!!, settings.getPrimitiveAsString("ideWindow.editorPane.location"))
        }
    }
}