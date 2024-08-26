package module.engine.pallet

import module.SIDE
import module.engine.exception.NoFileException
import module.engine.json.JsonObject
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.File

class Pallet(private val side:SIDE) {
    private val settings = side.settingsJsonObject!!
    var palletJson: JsonObject? = null
    init {
        val palletSystemName = settings.getPrimitiveAsString("pallet.theme")
        val palletName = settings.getPrimitiveAsString("pallet.$palletSystemName.name")
        val palletJsonAddress = settings.getPrimitiveAsString("pallet.$palletSystemName.path")

        if (palletJsonAddress.startsWith("/resources/")) {
            val palletJsonRaw = javaClass.getResource(palletJsonAddress)?.readText() ?: throw NoFileException("No such file: $palletJsonAddress")
            val heavyPalletJson = JSONParser().parse(palletJsonRaw) as JSONObject
            palletJson = JsonObject(heavyPalletJson)
        }else{
            val palletJsonFile = File(palletJsonAddress)
//            var palletJsonRaw = palletJsonFile.readText()
//            if (!palletJsonRaw.startsWith("{")) {
//                palletJsonRaw = "{\n$palletJsonRaw\n}"
//            }
            //todo complete
        }
    }
    private fun checkPalletIntegrityAndFix(customPalletJsonFile:File){
        if (!customPalletJsonFile.exists() || !customPalletJsonFile.isFile) {
            throw NoFileException("No such file: ${customPalletJsonFile.absolutePath}")
        }
        val defaultPalletJsonRaw = javaClass.getResource("/resources/json/pallet/dark.json")?.readText() ?: throw NoFileException("No such file: dark.json")
        val defaultPalletJson = JsonObject(JSONParser().parse(defaultPalletJsonRaw) as JSONObject)
        val customPalletJsonRaw = customPalletJsonFile.readText()
        val customPalletJson = JsonObject(JSONParser().parse(customPalletJsonRaw) as JSONObject)

    }
}