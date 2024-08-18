package module.engine.json

import module.engine.exception.NoFileException
import module.engine.exception.UnknownJsonTypeException
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.File

class JsonObject {
    constructor(jsonAddress: String) {
        val jsonRaw =
            javaClass.getResource(jsonAddress)?.readText() ?: throw NoFileException("No such file: $jsonAddress")
        val heavyJsonObject: JSONObject = JSONParser().parse(jsonRaw) as JSONObject
        loadJson(heavyJsonObject)
    }

    constructor(jsonObject: JSONObject) { //heavy
        loadJson(jsonObject)
    }

    constructor(jsonFile: File) {
        val jsonRaw = if (jsonFile.exists()) jsonFile.readText() else throw NoFileException("No such file: $jsonFile")
        val heavyJsonObject: JSONObject = JSONParser().parse(jsonRaw) as JSONObject
        loadJson(heavyJsonObject)
    }

    private val primitiveMap = mutableMapOf<String, String>()
    private val arrayMap = mutableMapOf<String, JsonArray>()
    private val objectMap = mutableMapOf<String, JsonObject>()

    private fun loadJson(jsonObject: JSONObject) {
        for ((key, value) in jsonObject) {
            key as String
            when (value) {
                is String -> primitiveMap[key] = value as String
                is JSONObject -> objectMap[key] = JsonObject(value as JSONObject)
                is JSONArray -> arrayMap[key] = JsonArray(value as JSONArray)
                else -> throw UnknownJsonTypeException(value!!::class.simpleName)
            }
        }
    }

    fun getPrimitiveAsString(key: String): String? = primitiveMap[key]
    fun getPrimitiveAsInt(key: String): Int? = primitiveMap[key]?.toInt()
    fun getPrimitiveAsDouble(key: String): Double? = primitiveMap[key]?.toDouble()
    fun getPrimitiveAsBoolean(key: String): Boolean? = primitiveMap[key]?.toBoolean()

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