package module.codespace

import module.pallet.Pallet
import module.settings.Settings
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import java.awt.Color
import javax.swing.SwingUtilities
import javax.swing.event.DocumentListener
import javax.swing.text.StyleConstants
import javax.swing.text.StyleContext
import javax.swing.text.StyledDocument

class CodeSyntaxChecker(val settings: Settings, private val pallet: Pallet) {
    val codeSyntaxes = HashMap<Long, CodeSyntax>()

    fun loadCodeSyntaxes(codeJsonObject: JSONObject) {
        settings.getSettingOfInt("codeSpace.codeEditor.fontSize")

        codeSyntaxes.clear()
        val codeSyntaxesArray = codeJsonObject["syntax"] as JSONArray

        // load code syntaxes from json
        var maxPriority = 0.toLong()
        var priorityCounter = 0.toLong()
        val codeSyntaxes = ArrayList<CodeSyntax>()

        for (codeSyntaxObject in codeSyntaxesArray) {
            val codeSyntax = codeSyntaxObject as JSONObject
            println(codeSyntax)
            val nameKey = "name"
            val priorityKey = "priority"
            val regexKey = "regex"
            val palletKey = "pallet"
            val name = codeSyntax[nameKey] as String
            val priority = codeSyntax[priorityKey] as Long?
            val regex = codeSyntax[regexKey] as String
            val palletName = codeSyntax[palletKey] as String?

            if (priority != null && priority > maxPriority) {
                maxPriority = priority
            }
            if (priority != null) {
                priorityCounter++
            }

            codeSyntaxes.add(CodeSyntax(this.pallet, name, priority, regex, palletName))
        }

        var addedSyntaxWithPriority = 0.toLong()
        var addedSyntaxWithoutPriority = 0.toLong()
        for (codeSyntax in codeSyntaxes) {
            if (codeSyntax.priority != null) {
                codeSyntax.priority = codeSyntaxes.size - priorityCounter + 1 + addedSyntaxWithPriority
                addedSyntaxWithPriority++
            } else {
                codeSyntax.priority = addedSyntaxWithoutPriority
                addedSyntaxWithoutPriority++
            }
            this.codeSyntaxes[codeSyntax.priority!!] = codeSyntax
        }

        val codeKeywordArray = codeJsonObject["keyword"] as JSONArray
        var codeKeywordCounter = 0
        for (codeKeywordObject in codeKeywordArray) {
            val codeKeyword = codeKeywordObject as JSONObject
            val nameKey = "name"
            val regexKey = "regex"
            val palletKey = "pallet"
            val priorityKey = "priority"
            val name = codeKeyword[nameKey] as String
            val palletName = codeKeyword[palletKey] as String?
            val priority = codeKeyword[priorityKey] as Long?
            val regex = codeKeyword[regexKey] as String

            val codeSyntax = CodeSyntax(this.pallet, name, priority, regex, palletName)
            this.codeSyntaxes[codeKeywordCounter + addedSyntaxWithoutPriority + addedSyntaxWithPriority + 1] =
                codeSyntax
            codeKeywordCounter++
        }

    }

    fun highlightSyntax(doc: StyledDocument, documentListener: DocumentListener) {
        val text = doc.getText(0, doc.length)
        val defaultStyle = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE)


        val styles = codeSyntaxes.values.associate { syntax ->
            syntax.name to doc.addStyle(syntax.name, defaultStyle).apply {
                StyleConstants.setForeground(this, syntax.color)
            }
        }

        SwingUtilities.invokeLater {
            doc.removeDocumentListener(documentListener)
            doc.setCharacterAttributes(0, text.length, defaultStyle, true)

//            codeSyntaxes.values.forEach { syntax ->
//                val matcher = syntax.regex.toPattern().matcher(text)
//                while (matcher.find()) {
//                    doc.setCharacterAttributes(
//                        matcher.start(),
//                        matcher.end() - matcher.start(),
//                        styles[syntax.name],
//                        false
//                    )
//                }
//
//            }
            val lines = text.split("\n")
            var startOffset = 0
            try {
                for (line in lines) {
                    codeSyntaxes.values.forEach { syntax ->
                        val matcher = syntax.regex.toPattern().matcher(line)
                        while (matcher.find()) {

                            doc.setCharacterAttributes(
                                startOffset + matcher.start(),
                                matcher.end() - matcher.start(),
                                styles[syntax.name],
                                false
                            )
                        }
                    }
                    startOffset += line.length + 1
                }

            } catch (e: Exception) {
                println("error $e.message")
            }
            doc.addDocumentListener(documentListener)
        }
    }
}

class CodeSyntax(
    pallet: Pallet,
    val name: String,
    var priority: Long?,
    regexPattern: String,
    palletName: String?
) {
    val regex = Regex(regexPattern)
    var color = Color.black

    init {
        if (palletName == null) {
            color = pallet.getPallet("codeSpace.codeEditor.foreground")
        } else if (palletName.startsWith("#")) {
            val colorCodeWithoutHash = palletName.replace("#", "")
            when (colorCodeWithoutHash.length) {
                6 -> {
                    color = Color.decode("#$colorCodeWithoutHash")
                }

                8 -> {
                    val red = colorCodeWithoutHash.substring(0, 2).toInt(16)
                    val green = colorCodeWithoutHash.substring(2, 4).toInt(16)
                    val blue = colorCodeWithoutHash.substring(4, 6).toInt(16)
                    val alpha = colorCodeWithoutHash.substring(6, 8).toInt(16)
                    color = Color(red, green, blue, alpha)
                }

                else -> {
                    color = pallet.getPallet("codeSpace.codeEditor.foreground")
                }
            }
        } else {
            color = pallet.getPallet("codeSpace.syntax.$palletName")
        }
    }
}