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
    var subss = SubClass()
}
class SubClass {
    var list = null
    var name = "dfsdd"
    val sss = 0x003
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
            &&!propertyReturnType.isSubtypeOf(typeOf<Map<*, *>>())
            &&!propertyReturnType.isSubtypeOf(typeOf<String>())
            &&!propertyReturnType.isSubtypeOf(typeOf<Int>())
            &&!propertyReturnType.isSubtypeOf(typeOf<Number>())
            &&!propertyReturnType.isSubtypeOf(typeOf<Char>())
            &&!propertyReturnType.isSubtypeOf(typeOf<Short>())
            &&!propertyReturnType.isSubtypeOf(typeOf<Long>())
            &&!propertyReturnType.isSubtypeOf(typeOf<Float>())
            &&!propertyReturnType.isSubtypeOf(typeOf<Double>())
            &&!propertyReturnType.isSubtypeOf(typeOf<Boolean>())) {
            println("field is not a collection")
            it.getter.call(this).isNull {
                classJSON.put(it.name, "null")
            }.notNull {
                println(this)
                classJSON.put(it.name, this.generateClassJSON())
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