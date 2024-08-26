package module

import module.display.Window
import module.engine.exception.NoFileException
import module.engine.json.JsonObject
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.File

class SIDE(var settingsJsonObject: JsonObject? = null) {
    var ideWindow: Window? = null
    init {
        val defaultSettingsJsonRaw = javaClass.getResource("/resources/json/settings.json")?.readText()
            ?: throw NoFileException("No such file: settings.json")
        val defaultSettingsJsonObject = JsonObject(JSONParser().parse(defaultSettingsJsonRaw) as JSONObject)
        if (settingsJsonObject != null) { // custom settings
            println("<IDE is launching with custom settings>")
            // check custom settings integrity
            checkSettingsIntegrityAndFix(settingsJsonObject!!, defaultSettingsJsonObject)
        } else { // default settings
            println("<IDE is launching with default settings>")
            settingsJsonObject = defaultSettingsJsonObject
        }

        ideWindowInit()
    }

    private fun ideWindowInit(){
        ideWindow = Window(settingsJsonObject!!)
    }

    private fun checkSettingsIntegrityAndFix(custom: JsonObject, default: JsonObject) {
        // check custom settings integrity
        // check if custom settings has all the keys that default settings has
        // check if custom settings has all the values that default settings has
        // check if custom settings has all the keys that default settings has
        // check if custom settings has all the keys that default settings has

        for ((key, value) in default.primitiveMap) {
            if (!custom.primitiveMap.containsKey(key)) {
                println("Warning!: No such key: $key in custom settings. Adding default value: $value")
                custom.primitiveMap[key] = value
            }
        }
        for ((key, value) in default.objectMap) {
            if (!custom.objectMap.containsKey(key)) {
                println("Warning!: No such key: $key in custom settings. Adding default value: $value")
                custom.objectMap[key] = value
            }
        }
        for ((key, value) in default.arrayMap) {
            if (!custom.arrayMap.containsKey(key)) {
                println("Warning!: No such key: $key in custom settings. Adding default value: $value")
                custom.arrayMap[key] = value
            }
        }

        custom.saveJson(File("settings.json"))
    }
}

fun main() {
    var settingsJsonObject: JsonObject? = null
    try {
        val settingsJsonFile = File("settings.json")
        settingsJsonObject = JsonObject(settingsJsonFile)
    } catch (e: NoFileException) {
        println("custom settings load failed")
        println("Warning!: ${e.message}")
    } catch (e: Exception) {
        println("ERROR!: ${e.message}")
        e.printStackTrace()
    }


    try {
        val side = SIDE(settingsJsonObject)
    } catch (e: Exception) {
        println("ERROR!: ${e.message}")
        e.printStackTrace()
    }
}


/*
if (getSettingOfString("$settingAddress.$fontNameKey").startsWith("/")) {
            //custom font
            val ttfFile = javaClass.getResource(getSettingOfString("$settingAddress.$fontNameKey"))!!.openStream()
            val deriveFont = Font.createFont(Font.TRUETYPE_FONT, ttfFile)
                .deriveFont(getSettingOfInt("$settingAddress.$fontSizeKey").toFloat())
            val ge: GraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment()
            ge.registerFont(deriveFont)
            return deriveFont
        }
 */