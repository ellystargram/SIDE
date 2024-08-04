package module.settings

import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.awt.Font
import java.awt.GraphicsEnvironment

class Settings {
    private val settingsJsonAddress = javaClass.getResource("settings.json")!!.readText()
    private var rootSettings: SettingCategory = SettingCategory("root", settingsJsonAddress)

    fun getSettingOfString(settingAddress: String): String {
        return rootSettings.getSettingOfString(settingAddress)
    }

    fun getSettingOfInt(settingAddress: String): Int {
        return rootSettings.getSettingOfInt(settingAddress)
    }

    fun getSettingOfDouble(settingAddress: String): Double {
        return rootSettings.getSettingOfDouble(settingAddress)
    }

    fun getSettingOfBoolean(settingAddress: String): Boolean {
        return rootSettings.getSettingOfBoolean(settingAddress)
    }

    fun getSettingOfFont(settingAddress: String): Font {
        return rootSettings.getSettingOfFont(settingAddress)
    }
}

class SettingCategory {
    var settings: HashMap<String, String> = HashMap()
    var categories: HashMap<String, SettingCategory> = HashMap()
    var name: String = ""

    constructor(name: String, jsonAddress: String) {
        this.name = name
        loadSettings(jsonAddress)
    }

    constructor(name: String, json: JSONObject) {
        this.name = name
        loadSettings(json)
    }

    private fun loadSettings(jsonAddress: String) {
        val settingsJsonObject: JSONObject = JSONParser().parse(jsonAddress) as JSONObject
        loadSettings(settingsJsonObject)
        settingsPrint(0)
    }

    private fun loadSettings(json: JSONObject) {
        for (setting in json) {
            if (setting.value is JSONObject) {
                categories[setting.key.toString()] =
                    SettingCategory(setting.key.toString(), setting.value as JSONObject)
            } else {
                settings[setting.key.toString()] = setting.value.toString()
            }
        }
    }

    private fun settingsPrint(indent: Int) {
        println("\t".repeat(indent) + ":${this.name}")
        for (setting in settings) {
            println("\t".repeat(indent + 1) + "${setting.key}: ${setting.value}")
        }
        for (category in categories) {
            category.value.settingsPrint(indent + 1)
        }
    }

    fun getSettingOfString(settingAddress: String): String {
        if (settingAddress.contains(".")) {
            val category = settingAddress.split(".")[0]
            return categories[category]?.getSettingOfString(settingAddress.substring(category.length + 1)) ?: ""
        }
        return settings[settingAddress] ?: ""
    }

    fun getSettingOfInt(settingAddress: String): Int {
        if (settingAddress.contains(".")) {
            val category = settingAddress.split(".")[0]
            return categories[category]?.getSettingOfInt(settingAddress.substring(category.length + 1)) ?: 0
        }
        return settings[settingAddress]?.toInt() ?: 0
    }

    fun getSettingOfDouble(settingAddress: String): Double {
        if (settingAddress.contains(".")) {
            val category = settingAddress.split(".")[0]
            return categories[category]?.getSettingOfDouble(settingAddress.substring(category.length + 1)) ?: 0.0
        }
        return settings[settingAddress]?.toDouble() ?: 0.0
    }

    fun getSettingOfBoolean(settingAddress: String): Boolean {
        if (settingAddress.contains(".")) {
            val category = settingAddress.split(".")[0]
            return categories[category]?.getSettingOfBoolean(settingAddress.substring(category.length + 1)) ?: false
        }
        return settings[settingAddress]?.toBoolean() ?: false
    }

    fun getSettingOfFont(settingAddress: String): Font {
        val fontNameKey = "font"
        val fontSizeKey = "fontSize"
        if (getSettingOfString("$settingAddress.$fontNameKey").startsWith("/")) {
            //custom font
            val ttfFile = javaClass.getResource(getSettingOfString("$settingAddress.$fontNameKey"))!!.openStream()
            val deriveFont = Font.createFont(Font.TRUETYPE_FONT, ttfFile)
                .deriveFont(getSettingOfInt("$settingAddress.$fontSizeKey").toFloat())
            val ge: GraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment()
            ge.registerFont(deriveFont)
            return deriveFont
        }
        return Font(
            getSettingOfString("$settingAddress.$fontNameKey"),
            Font.PLAIN,
            getSettingOfInt("$settingAddress.$fontSizeKey")
        )
    }
}