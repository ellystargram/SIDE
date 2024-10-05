package module.engine.json

import module.engine.exception.NoFileException
import module.engine.exception.UnknownJsonTypeException
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.File

class JsonArray {
    constructor(jsonAddress: String) {
        var jsonFile: File? = null
        if (jsonAddress.startsWith("/")) {
            jsonFile = File(javaClass.getResource(jsonAddress)!!.path)
        } else {
            jsonFile = File(jsonAddress)
        }
//        val jsonFile = File(jsonAddress)
//        val jsonRaw =
//            javaClass.getResource(jsonAddress)?.readText() ?: throw NoFileException("No such file: $jsonAddress")
        var jsonRaw =
            if (jsonFile.exists()) jsonFile.readText() else throw NoFileException("No such file: $jsonAddress")
        if (!jsonRaw.startsWith("[") || !jsonRaw.endsWith("]")) {
            jsonFile.writeText("[]")
            jsonRaw = "[]"
        }
        val heavyJsonArray = JSONParser().parse(jsonRaw) as JSONArray
        loadJson(heavyJsonArray)
    }

    constructor(jsonArray: JSONArray) {
        loadJson(jsonArray)
    }

    constructor(jsonFile: File) {
        var jsonRaw = if (jsonFile.exists()) jsonFile.readText() else throw NoFileException("No such file: $jsonFile")
        if (!jsonRaw.startsWith("[") || !jsonRaw.endsWith("]")) {
            jsonFile.writeText("[]")
            jsonRaw = "[]"
        }
        val heavyJsonArray = JSONParser().parse(jsonRaw) as JSONArray
        loadJson(heavyJsonArray)
    }

    private val primitiveList = mutableListOf<String>()
    private val arrayList = mutableListOf<JsonArray>()
    private val objectList = mutableListOf<JsonObject>()

    private fun loadJson(jsonArray: JSONArray) {
        for ((index, value) in jsonArray.withIndex()) {
            when (value) {
                is String -> primitiveList.add(value)
                is JSONArray -> arrayList.add(JsonArray(value))
                is JSONObject -> objectList.add(JsonObject(value))
                else -> throw UnknownJsonTypeException(value!!::class.simpleName)
            }
        }
    }

    fun saveJson(file: File) {
        val newJsonRaw = getSubJsonString()
        file.writeText(newJsonRaw)
    }

    fun getSubJsonString(): String {
        val heavyJsonArray = JSONArray()
        for (value in primitiveList) {
            heavyJsonArray.add(value)
        }
        for (value in arrayList) {
            heavyJsonArray.add(JSONParser().parse(value.getSubJsonString()) as JSONArray)
        }
        for (value in objectList) {
            heavyJsonArray.add(JSONParser().parse(value.getSubJsonString()) as JSONObject)
        }
        return formatJsonString(heavyJsonArray.toJSONString())
    }

    private fun formatJsonString(jsonString: String, indentFactor: Int = 4): String {
        val indent = " ".repeat(indentFactor)
        val formattedJson = StringBuilder()

        var level = 0
        var inQuote = false

        for (char in jsonString) {
            when (char) {
                '{', '[' -> {
                    formattedJson.append(char)
                    if (!inQuote) {
                        level++
                        formattedJson.append("\n")
                        formattedJson.append(indent.repeat(level))
                    }
                }

                '}', ']' -> {
                    if (!inQuote) {
                        level--
                        formattedJson.append("\n")
                        formattedJson.append(indent.repeat(level))
                    }
                    formattedJson.append(char)
                }

                '"' -> {
                    formattedJson.append(char)
                    inQuote = !inQuote
                }

                ',' -> {
                    formattedJson.append(char)
                    if (!inQuote) {
                        formattedJson.append("\n")
                        formattedJson.append(indent.repeat(level))
                    }
                }

                else -> formattedJson.append(char)
            }
        }
        return formattedJson.toString()
    }

    fun getPrimitiveList(): List<String> = primitiveList
    fun getArrayList(): List<JsonArray> = arrayList
    fun getObjectList(): List<JsonObject> = objectList

//    constructor(jsonAddress: String){
//        val jsonRaw = javaClass.getResource(jsonAddress)?.readText() ?: throw NoFileException("No such file: $jsonAddress")
////        val jsonArray = Json.parseToJsonElement(jsonRaw).jsonArray
//        val jsonArray = JSONParser().parse(jsonRaw) as JSONArray
//        loadJson(jsonArray)
//    }
//    constructor(jsonElement: JsonElement){
//        val jsonArray = jsonElement.jsonArray
//        loadJson(jsonArray)
//    }
//    constructor(jsonArray: JsonArray){
//        loadJson(jsonArray)
//    }
//
//    private val primitiveList = mutableListOf<String>()
//    private val arrayList = mutableListOf<JsonArray>()
//    private val objectList = mutableListOf<JsonObject>()
//
//    private fun loadJson(jsonArray: JsonArray){
//        for((index, value) in jsonArray.withIndex()){
//            when(value) {
//                is JsonPrimitive -> primitiveList.add(value.content)
//                is JsonArray -> arrayList.add(JsonArray(value))
//                is JsonObject -> objectList.add(JsonObject(value))
//                else -> throw UnknownJsonTypeException(value::class.simpleName)
//            }
//        }
//    }
//
//    fun getPrimitiveList(): List<String> = primitiveList
//    fun getArrayList(): List<JsonArray> = arrayList
//    fun getObjectList(): List<JsonObject> = objectList
}