package extensioneer.persistence

import extensioneer.isNull
import extensioneer.notNull
import org.json.JSONArray
import org.json.JSONObject
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType
import kotlin.reflect.typeOf

class TestClass {
    val list = arrayOf("one", "tow", "thre")
    var list2 = listOf(String(), String())
    var map = mapOf("o" to 2)
    var sub = SubClass()
}
class SubClass {
    var list = arrayOf("ddod","fdfs")
}

@ExperimentalStdlibApi
fun main() {
    val test = TestClass()
    println(test.convertToJsonObject())
}
@ExperimentalStdlibApi
fun Any.convertToJsonObject(): JSONObject {
    return generateClassJSON()
}

@ExperimentalStdlibApi
private fun Any.generateClassJSON(): JSONObject {
    val classJSON = JSONObject()
    val objectProperties = this::class.memberProperties

    objectProperties.forEach {
        val propertyReturnType = it.returnType
        println(propertyReturnType)
        if(!propertyReturnType.isSubtypeOf(typeOf<Collection<*>>())
            &&!propertyReturnType.isSubtypeOf(typeOf<Array<*>>())
            &&!propertyReturnType.isSubtypeOf(typeOf<Map<*, *>>())) {
            println("field is not a collection")
            it.isNull {
                classJSON.put(it.name, "null")
            }.notNull {
                println(getter.call(this@generateClassJSON))
                classJSON.put(it.name, getter.call(this@generateClassJSON)!!.generateClassJSON())
            }
        } else {
            classJSON.put(it.name, it.getter.call(this))
        }
    }
    return classJSON
}

fun <T> Array<T>.generateJsonArray(): JSONArray {
    val returnArray = JSONArray()
    forEach {
        returnArray.put(it)
    }
    return returnArray
}