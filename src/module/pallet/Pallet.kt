package module.pallet

import module.settings.Settings
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.awt.Color
import java.io.File

class Pallet(settings: Settings) {
    private val palletAddress: String = settings.getSettingOfString("pallet.palletJsonAddress")
    private val pallet: PalletCategory = PalletCategory("root", javaClass.getResource(palletAddress)!!.readText())
    fun getPallet(palletAddress: String): Color {
        return pallet.getPallet(palletAddress)
    }
}

class PalletCategory {
    private var pallets: HashMap<String, Color> = HashMap()
    private var categories: HashMap<String, PalletCategory> = HashMap()
    private var name: String = ""

    constructor(name: String, jsonAddress: String) {
        this.name = name
        loadPallet(jsonAddress)
    }

    constructor(name: String, json: JSONObject) {
        this.name = name
        loadPallet(json)
    }

    private fun loadPallet(jsonAddress: String) {
        val palletJsonObject: JSONObject = JSONParser().parse(jsonAddress) as JSONObject
        loadPallet(palletJsonObject)
        println("COLORS:")
        palletsPrint(0)
    }

    private fun loadPallet(json: JSONObject) {
        for (pallet in json) {
            if (pallet.value is JSONObject) {
                categories[pallet.key.toString()] = PalletCategory(pallet.key.toString(), pallet.value as JSONObject)
            } else {
                if(pallet.value.toString().matches(Regex("(#)?[0-9a-fA-F]{6}"))){
                    pallets[pallet.key.toString()] = Color.decode(pallet.value.toString())
                }else if (pallet.value.toString().matches(Regex("(#)?[0-9a-fA-F]{8}"))){
                    val code = pallet.value.toString().replace("#", "")
                    val r = code.substring(0, 2).toInt(16)
                    val g = code.substring(2, 4).toInt(16)
                    val b = code.substring(4, 6).toInt(16)
                    val a = code.substring(6, 8).toInt(16)
                    pallets[pallet.key.toString()] = Color(r, g, b, a)
                }
            }
        }
    }

    private fun palletsPrint(indent: Int) {
        println("\t".repeat(indent) + ":${this.name}")
        for (pallet in pallets) {
            println("\t".repeat(indent + 1) + "${pallet.key}: ${pallet.value}")
        }
        for (category in categories) {
            category.value.palletsPrint(indent + 1)
        }
    }

    fun getPallet(palletAddress: String): Color {
        if (palletAddress.contains(".")) {
            val address = palletAddress.split(".")[0]
            return categories[address]?.getPallet(palletAddress.substring(address.length + 1)) ?: pallets["error"]
            ?: Color.RED
        } else {
            return pallets[palletAddress] ?: pallets["error"] ?: Color.RED
        }
    }
}