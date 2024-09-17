package module.engine.json

import module.engine.exception.NoFileException
import module.engine.exception.NoKeyException
import module.engine.exception.UnknownJsonTypeException
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.File

class JsonObject {
    constructor(jsonAddress: String) {
        var jsonFile: File? = null
        if (jsonAddress.startsWith("/")) {
            jsonFile = File(javaClass.getResource(jsonAddress)!!.path)
        } else {
            jsonFile = File(jsonAddress)
        }
        var jsonRaw =
            if (jsonFile.exists()) jsonFile.readText() else throw NoFileException("No such file: $jsonAddress")
        if (!jsonRaw.startsWith("{") || !jsonRaw.endsWith("}")) {
            jsonFile.writeText("{}")
            jsonRaw = "{}"
        }
        val heavyJsonObject: JSONObject = JSONParser().parse(jsonRaw) as JSONObject
        loadJson(heavyJsonObject)
    }

    constructor(jsonObject: JSONObject) { //heavy
        loadJson(jsonObject)
    }

    constructor(jsonFile: File) {
        var jsonRaw = if (jsonFile.exists()) jsonFile.readText() else throw NoFileException("No such file: $jsonFile")
        if (!jsonRaw.startsWith("{") || !jsonRaw.endsWith("}")) {
            jsonFile.writeText("{}")
            jsonRaw = "{}"
        }
        val heavyJsonObject: JSONObject = JSONParser().parse(jsonRaw) as JSONObject
        loadJson(heavyJsonObject)
    }

    val primitiveMap = mutableMapOf<String, String>()
    val arrayMap = mutableMapOf<String, JsonArray>()
    val objectMap = mutableMapOf<String, JsonObject>()

    private fun loadJson(jsonObject: JSONObject) {
        for ((key, value) in jsonObject) {
            key as String
            when (value) {
                is String -> primitiveMap[key] = value as String
                is Number -> primitiveMap[key] = value.toString()
                is Boolean -> primitiveMap[key] = value.toString()
                is JSONObject -> objectMap[key] = JsonObject(value as JSONObject)
                is JSONArray -> arrayMap[key] = JsonArray(value as JSONArray)
                else -> throw UnknownJsonTypeException(value!!::class.simpleName)
            }
        }
    }

    fun saveJson(jsonFile: File) {
        val newJsonRaw = getSubJsonString()
//        val heavyJsonObject = JSONObject()
//        for ((key, value) in primitiveMap) {
//            heavyJsonObject[key] = value
//        }
//        for ((key, value) in objectMap) {
//            heavyJsonObject[key] = JSONObject(value.getSubJsonString())
//        }
//        for ((key, value) in arrayMap) {
//            heavyJsonObject[key] = value
//        }
//        jsonFile.writeText(formatJsonString(newJsonRaw))
        jsonFile.writeText(newJsonRaw)
    }

    fun getSubJsonString(): String{
        val heavyJsonObject = JSONObject()
        for ((key, value) in primitiveMap) {
            heavyJsonObject[key] = value
        }
        for ((key, value) in objectMap) {
            heavyJsonObject[key] = JSONParser().parse(value.getSubJsonString()) as JSONObject
        }
        for ((key, value) in arrayMap) {
            heavyJsonObject[key] = JSONParser().parse(value.getSubJsonString()) as JSONArray
        }
//        return heavyJsonObject.toJSONString()
        return formatJsonString(heavyJsonObject.toJSONString())
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

    //    fun getPrimitiveAsString(key: String): String? = primitiveMap[key]
//    fun getPrimitiveAsInt(key: String): Int? = primitiveMap[key]?.toInt()
//    fun getPrimitiveAsDouble(key: String): Double? = primitiveMap[key]?.toDouble()
//    fun getPrimitiveAsBoolean(key: String): Boolean? = primitiveMap[key]?.toBoolean()
    fun getPrimitiveAsString(key: String): String {
        if (!key.contains(".")) {
            primitiveMap[key]?.let { return it } ?: throw NoKeyException("No such key: $key")
        }
        return objectMap[key.substringBefore(".")]?.getPrimitiveAsString(key.substringAfter("."))
            ?: throw NoKeyException("No such key: $key")
    }

    fun getPrimitiveAsInt(key: String): Int {
        if (!key.contains(".")) {
            primitiveMap[key]?.let { return it.toInt() } ?: throw NoKeyException("No such key: $key")
        }
        return objectMap[key.substringBefore(".")]?.getPrimitiveAsInt(key.substringAfter("."))
            ?: throw NoKeyException("No such key: $key")
    }

    fun getPrimitiveAsDouble(key: String): Double {
        if (!key.contains(".")) {
            primitiveMap[key]?.let { return it.toDouble() } ?: throw NoKeyException("No such key: $key")
        }
        return objectMap[key.substringBefore(".")]?.getPrimitiveAsDouble(key.substringAfter("."))
            ?: throw NoKeyException("No such key: $key")
    }

    fun getPrimitiveAsBoolean(key: String): Boolean {
        if (!key.contains(".")) {
            primitiveMap[key]?.let { return it.toBoolean() } ?: throw NoKeyException("No such key: $key")
        }
        return objectMap[key.substringBefore(".")]?.getPrimitiveAsBoolean(key.substringAfter("."))
            ?: throw NoKeyException("No such key: $key")
    }

    fun getArray(key: String): JsonArray? = arrayMap[key]
    fun getObject(key: String): JsonObject? = objectMap[key]

//    constructor(jsonAddress: String){
//        val jsonRaw = javaClass.getResource(jsonAddress)?.readText() ?: throw NoFileException("No such file: $jsonAddress")
//        val jsonElement = Json.parseToJsonElement(jsonRaw)
//        val jsonObject = jsonElement.jsonObject
//        loadJson(jsonObject)
//    }
//    constructor(jsonElement: JsonElement){
//        val jsonObject = jsonElement.jsonObject
//        loadJson(jsonObject)
//    }
//    constructor(jsonObject: JsonObject){
//        loadJson(jsonObject)
//    }
//
//    private val primitiveMap = mutableMapOf<String, String>()
//    private val arrayMap = mutableMapOf<String, JsonArray>()
//    private val objectMap = mutableMapOf<String, JsonObject>()
//
//    private fun loadJson(jsonObject: JsonObject){
//        for((key, value) in jsonObject){
//            when(value){
//                is JsonPrimitive -> primitiveMap[key] = value.content
//                is JsonArray -> arrayMap[key] = JsonArray(value)
//                is JsonObject -> objectMap[key] = JsonObject(value)
//                else -> throw UnknownJsonTypeException(value::class.simpleName)
//            }
//        }
//    }
//
//    fun getPrimitiveAsString(key: String): String? = primitiveMap[key]
//    fun getPrimitiveAsInt(key: String): Int? = primitiveMap[key]?.toInt()
//    fun getPrimitiveAsDouble(key: String): Double? = primitiveMap[key]?.toDouble()
//    fun getPrimitiveAsBoolean(key: String): Boolean? = primitiveMap[key]?.toBoolean()
//
//    fun getArray(key: String): JsonArray? = arrayMap[key]
//    fun getObject(key: String): JsonObject? = objectMap[key]

}