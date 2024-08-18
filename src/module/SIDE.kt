package module

import module.engine.exception.NoFileException
import module.engine.json.JsonObject
import java.io.File

class SIDE(var settingsJsonObject: JsonObject? = null) {
    init {
        if (settingsJsonObject != null) { // custom settings
            println("<IDE is launching with custom settings>")
            // check custom settings integrity
            val defaultSettingsJsonObject = JsonObject(
                javaClass.getResource("/resources/json/settings.json")?.readText()
                    ?: throw NoFileException("No such file: settings.json")
            )

        } else { // default settings
            println("<IDE is launching with default settings>")
            settingsJsonObject = JsonObject(
                javaClass.getResource("/resources/json/settings.json")?.readText()
                    ?: throw NoFileException("No such file: settings.json")
            )
        }

    }

    private fun checkSettingsIntegrity(custom: JsonObject, default: JsonObject) {
        // check custom settings integrity
        // check if custom settings has all the keys that default settings has
        // check if custom settings has all the values that default settings has
        // check if custom settings has all the keys that default settings has
        // check if custom settings has all the keys that default settings has
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
    }


    try {
        val side = SIDE(settingsJsonObject)
    } catch (e: Exception) {
        println("ERROR!: ${e.message}")
    }
}