package module.codespace

import module.pallet.Pallet
import module.settings.Settings
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.awt.BorderLayout
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextPane
import javax.swing.border.EmptyBorder
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.text.StyledDocument

class CodeSpace(private val settings: Settings, private val pallet: Pallet) : JPanel() {
    private var codeMode = settings.getSettingOfString("codeSpace.mode")
    private val codeEditor: JTextPane = JTextPane()
    private val codeArea: StyledDocument = codeEditor.styledDocument
    private val codeHorizontalScrollPane: JScrollPane = JScrollPane(codeEditor)
    private val codeVerticalScrollPane: JScrollPane = JScrollPane(codeHorizontalScrollPane)
    private val lineNumberDisplay = LineNumberDisplay(codeEditor)
    private val codeSyntaxChecker = CodeSyntaxChecker(settings, pallet)
    val documentListener = object : DocumentListener {
        override fun insertUpdate(e: DocumentEvent) {
            updateLineNumber()
            codeSyntaxChecker.highlightSyntax(codeArea, this)
        }

        override fun removeUpdate(e: DocumentEvent) {
            updateLineNumber()
            codeSyntaxChecker.highlightSyntax(codeArea, this)
        }

        override fun changedUpdate(e: DocumentEvent) {
            updateLineNumber()
            codeSyntaxChecker.highlightSyntax(codeArea, this)
        }

        private fun updateLineNumber() {
            lineNumberDisplay.updateLineNumbers()
            try {
                lineNumberDisplay.caretPosition = codeEditor.caretPosition
            } catch (_: Exception) {
            }
        }
    }

    init {
        layout = BorderLayout()
        add(codeVerticalScrollPane)
        codeVerticalScrollPane.setRowHeaderView(lineNumberDisplay)
        lineNumberDisplay.font = settings.getSettingOfFont("codeSpace")
        codeEditor.font = settings.getSettingOfFont("codeSpace")
        codeEditor.caretColor = pallet.getPallet("codeSpace.cursor")
        codeEditor.border = EmptyBorder(10, 10, 10, 10)
        codeHorizontalScrollPane.border = EmptyBorder(0, 0, 0, 0)
        codeVerticalScrollPane.border = EmptyBorder(0, 0, 0, 0)
        setEngine()
        setPallet()
        setMouseWheelListener()
        codeModeChange(codeMode)
    }

    private fun setEngine() {
        codeArea.addDocumentListener(documentListener)
    }

    private fun setPallet() {
        background = pallet.getPallet("codeSpace.background")
        foreground = pallet.getPallet("codeSpace.foreground")
        codeEditor.background = pallet.getPallet("codeSpace.codeEditor.background")
        codeEditor.foreground = pallet.getPallet("codeSpace.codeEditor.foreground")
        lineNumberDisplay.background = pallet.getPallet("codeSpace.lineNumber.background")
        lineNumberDisplay.foreground = pallet.getPallet("codeSpace.lineNumber.foreground")
    }

    private fun setMouseWheelListener() {
        codeEditor.addMouseWheelListener {
            val mouseWheelSensitive = settings.getSettingOfDouble("codeSpace.mouseWheelSensitive")
            val scrollAmount = it.unitsToScroll * mouseWheelSensitive
            codeVerticalScrollPane.verticalScrollBar.value += scrollAmount.toInt()
        }
    }

    private fun codeModeChange(codeMode: String) {
        // change the code mode
        val codeModeJsonLines = javaClass.getResource("modes/$codeMode.json")?.readText() ?: return
        val codeModeJsonObject = JSONParser().parse(codeModeJsonLines) as JSONObject
        codeSyntaxChecker.loadCodeSyntaxes(codeModeJsonObject)
    }
}
