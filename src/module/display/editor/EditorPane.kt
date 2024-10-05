package module.display.editor

import module.SIDE
import java.awt.Component
import java.io.File
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class EditorPane(private val side: SIDE) : JTabbedPane() {
    private val settings = side.settingsJsonObject!!
    private val GREETING_MESSAGE: String = settings.getPrimitiveAsString("ideWindow.editorPane.greetingMessage")
    private val GREETING_TAB_TITLE: String = settings.getPrimitiveAsString("ideWindow.editorPane.greetingTabTitle")
    private val NEW_FILE_TITLE: String = settings.getPrimitiveAsString("ideWindow.editorPane.newFileTitle")
    private val UNSAVED_FILE_TITLE_PREFIX: String =
        settings.getPrimitiveAsString("ideWindow.editorPane.unsavedFileTitlePrefix")
    val greetingPane: JPanel = JPanel()

    init {
        val greeting_label: JLabel = JLabel()
        greeting_label.text = GREETING_MESSAGE
        greetingPane.add(greeting_label, "Center")

        addTab(GREETING_TAB_TITLE, greetingPane)

        createNewFile()
    }

    private fun queryIndex(component: Component): Int {
        return indexOfComponent(component)
    }

    private fun setTitleAtTab(component: Component, title: String) {
        val index = queryIndex(component)
        setTitleAt(index, title)
    }

    private fun getTitleAtTab(component: EditorTab): String {
        return getTitleAt(queryIndex(component))
    }

    private fun destroyTab(component: EditorTab) {
        component.save()
        remove(component)
    }

    fun destroyTabAt(index: Int) {
        val component: EditorTab = getComponent(index) as EditorTab
        destroyTab(component)
    }

    fun createNewFile() {
        var tab = EditorTab()
        addTab("", tab)
        tab.postAddTab()
    }

    fun open(file: File) {
        var tab = EditorTab(file)
        addTab("", tab)
        tab.postAddTab()
    }

    inner class EditorTab(private var file: File? = null) : JPanel() {
        private var textArea = JTextArea()
        private var saved = false
        var title
            get() = getTitleAtTab(this)
            set(value) {
                setTitleAtTab(this, value)
            }

        init {
            textArea.document.addDocumentListener(object : DocumentListener {
                override fun changedUpdate(e: DocumentEvent?) {
                    saved = false;
                    title = getTitleStringByContext()
                }

                override fun insertUpdate(e: DocumentEvent?) {}
                override fun removeUpdate(e: DocumentEvent?) {}
            })

            if (file?.exists() == true) {
                textArea.text = file!!.readText()
                saved = true
            }
        }

        private fun getTitleStringByContext(): String {
            if (file?.exists() == true)
                if (saved)
                    return file!!.name
                else
                    return UNSAVED_FILE_TITLE_PREFIX + file!!.name
            else
                return NEW_FILE_TITLE
        }

        fun postAddTab() {
            title = getTitleStringByContext()
        }

        fun isSaved(): Boolean {
            return saved
        }

        fun save() {
            if (file == null) {
                val chooser = JFileChooser()
                if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    file = chooser.selectedFile
                    file!!.writeText(textArea.text)
                    saved = true
                }
            } else if (!saved) {
                file!!.writeText(textArea.text)
                saved = true
            } else {
                return
            }

            title = getTitleStringByContext()
        }


    }

}