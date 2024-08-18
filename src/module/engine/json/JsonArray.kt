package module.engine.json

import module.engine.exception.NoFileException
import module.engine.exception.UnknownJsonTypeException
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.File

class JsonArray {
    constructor(jsonAddress:String){
        val jsonRaw = javaClass.getResource(jsonAddress)?.readText() ?: throw NoFileException("No such file: $jsonAddress")
        val heavyJsonArray = JSONParser().parse(jsonRaw) as JSONArray
        loadJson(heavyJsonArray)
    }
    constructor(jsonArray: JSONArray){
        loadJson(jsonArray)
    }
    constructor(jsonFile: File){
        val jsonRaw = if (jsonFile.exists()) jsonFile.readText() else throw NoFileException("No such file: $jsonFile")
        val heavyJsonArray = JSONParser().parse(jsonRaw) as JSONArray
        loadJson(heavyJsonArray)
    }

    private val primitiveList = mutableListOf<String>()
    private val arrayList = mutableListOf<JsonArray>()
    private val objectList = mutableListOf<JsonObject>()

    private fun loadJson(jsonArray: JSONArray){
        for((index, value) in jsonArray.withIndex()){
            when(value){
                is String -> primitiveList.add(value)
                is JSONArray -> arrayList.add(JsonArray(value))
                is JSONObject -> objectList.add(JsonObject(value))
                else -> throw UnknownJsonTypeException(value!!::class.simpleName)
            }
        }
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