package module.engine.pallet

import module.SIDE
import module.engine.exception.NoFileException
import module.engine.json.JsonObject
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.awt.Color
import java.io.File

class Pallet(side: SIDE) {
    private val settings = side.settingsJsonObject!!
    private var palletJson: JsonObject? = null

    init {
        val palletSystemName = settings.getPrimitiveAsString("pallet.theme")
//        val palletName = settings.getPrimitiveAsString("pallet.$palletSystemName.name")
        val palletJsonAddress = settings.getPrimitiveAsString("pallet.$palletSystemName.path")

        if (palletJsonAddress.startsWith("/resources/")) { // built-in pallet
            val palletJsonRaw = javaClass.getResource(palletJsonAddress)?.readText()
                ?: throw NoFileException("No such file: $palletJsonAddress")
            val heavyPalletJson = JSONParser().parse(palletJsonRaw) as JSONObject
            palletJson = JsonObject(heavyPalletJson)
        } else { // custom pallet
            val palletJsonFile = File(palletJsonAddress)
//            var palletJsonRaw = palletJsonFile.readText()
//            if (!palletJsonRaw.startsWith("{")) {
//                palletJsonRaw = "{\n$palletJsonRaw\n}"
//            }
//            if (!palletJsonFile.exists() || !palletJsonFile.isFile) {
//                throw NoFileException("No such file: ${palletJsonFile.absolutePath}")
//            }
//            var palletJsonRaw = palletJsonFile.readText()
//            if (!palletJsonRaw.startsWith("{") || !palletJsonRaw.endsWith("}")) {
//                palletJsonRaw = "{}"
//                palletJsonFile.writeText(palletJsonRaw)
//            }
            checkPalletIntegrityAndFix(palletJsonFile)
            val palletJsonRaw = palletJsonFile.readText()
            val heavyPalletJson = JSONParser().parse(palletJsonRaw) as JSONObject
            palletJson = JsonObject(heavyPalletJson)
        }
    }

    private fun checkPalletIntegrityAndFix(customPalletJsonFile: File) {
        if (!customPalletJsonFile.exists() || !customPalletJsonFile.isFile) {
            throw NoFileException("No such file: ${customPalletJsonFile.absolutePath}")
        }
        val defaultPalletJsonRaw = javaClass.getResource("/resources/json/pallet/dark.json")?.readText()
            ?: throw NoFileException("No such file: dark.json")
        val defaultPalletJson = JsonObject(JSONParser().parse(defaultPalletJsonRaw) as JSONObject)
        var customPalletJsonRaw = customPalletJsonFile.readText()
        if (!customPalletJsonRaw.startsWith("{") || !customPalletJsonRaw.endsWith("}")) {
            customPalletJsonRaw = "{}"
            customPalletJsonFile.writeText(customPalletJsonRaw)
        }
        val customPalletJson = JsonObject(JSONParser().parse(customPalletJsonRaw) as JSONObject)
        for ((key, value) in defaultPalletJson.primitiveMap) {
            if (!customPalletJson.primitiveMap.containsKey(key)) {
                println("Warning!: No such key: $key in custom pallet. Adding default value: $value")
                customPalletJson.primitiveMap[key] = value
            }
        }
        for ((key, value) in defaultPalletJson.objectMap) {
            if (!customPalletJson.objectMap.containsKey(key)) {
                println("Warning!: No such key: $key in custom pallet. Adding default value: $value")
                customPalletJson.objectMap[key] = value
            }
        }
        for ((key, value) in defaultPalletJson.arrayMap) {
            if (!customPalletJson.arrayMap.containsKey(key)) {
                println("Warning!: No such key: $key in custom pallet. Adding default value: $value")
                customPalletJson.arrayMap[key] = value
            }
        }
        customPalletJson.saveJson(customPalletJsonFile)
    }

    fun getPallet(pallet: String): Color {

        try {
            palletJson!!.getPrimitiveAsString(pallet).let {
                if (it.matches(Regex("#?[0-9a-fA-F]{6}"))) {
                    val removeSharp = it.removePrefix("#")
                    return Color.decode("0x$removeSharp")
                }
                if (it.matches(Regex("#?[0-9a-fA-F]{8}"))) {
//                return Color.decode(it)
                    val withoutSharp = it.removePrefix("#")
                    val alpha = withoutSharp.substring(0, 2).toInt(16)
                    val red = withoutSharp.substring(2, 4).toInt(16)
                    val green = withoutSharp.substring(4, 6).toInt(16)
                    val blue = withoutSharp.substring(6, 8).toInt(16)
                    return Color(red, green, blue, alpha)
                }
            }
        } catch (e: Exception) {
            println("Error: $e")
        }
        return getPallet("error")
    }
}