package module.codespace

import javax.swing.JTextArea
import javax.swing.JTextPane
import javax.swing.border.EmptyBorder

class LineNumberDisplay(val textPane: JTextPane):JTextArea() {
    init {
        isEditable = false
        border = EmptyBorder(10, 10, 10, 10)
        isFocusable = false
        alignmentX = RIGHT_ALIGNMENT
        updateLineNumbers()
    }
    fun updateLineNumbers(){
        text = getLineNumbers()
    }

    private fun getLineNumbers(): String {
        val lines = textPane.text.split("\n")
        val lineCount = lines.size
        val maxDigits = lineCount.toString().length
        val lineNumber = StringBuilder()
        for (i in 1..lineCount) {
            lineNumber.append(i.toString().padStart(maxDigits, ' '))
            lineNumber.append("\n")
        }
        return lineNumber.toString()
    }

}