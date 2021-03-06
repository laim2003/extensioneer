package extensioneer.persistence

import extensioneer.org.json.forEach
import org.json.JSONArray
import org.json.JSONObject
import java.lang.RuntimeException
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaType
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
    val innererSubClass = innererSubClass()
}
class innererSubClass {
    var lslls = "sdjfkskldfj"
}

val testJSON = """
{
   "sub":{
      "class":"extensioneer.persistence.SubClass",
      "value":{
         "sss":{
            "class":"int",
            "value":3
         },
         "name":{
            "class":"java.lang.String",
            "value":"dfsdd"
         },
         "innererSubClass":{
            "class":"extensioneer.persistence.innererSubClass",
            "value":{
               "lslls":{
                  "class":"java.lang.String",
                  "value":"sdjfkskldfj"
               }
            }
         },
         "list":{
            "class":"java.lang.Void",
            "value":"null"
         }
      }
   },
   "list2":{
      "class":"java.util.List<java.lang.String>",
      "value":[
         "",
         ""
      ]
   },
   "subss":{
      "class":"extensioneer.persistence.SubClass",
      "value":{
         "sss":{
            "class":"int",
            "value":3
         },
         "name":{
            "class":"java.lang.String",
            "value":"dfsdd"
         },
         "innererSubClass":{
            "class":"extensioneer.persistence.innererSubClass",
            "value":{
               "lslls":{
                  "class":"java.lang.String",
                  "value":"sdjfkskldfj"
               }
            }
         },
         "list":{
            "class":"java.lang.Void",
            "value":"null"
         }
      }
   },
   "list":{
      "class":"java.lang.String[]",
      "value":[
         "one",
         "tow",
         "thre"
      ]
   },
   "map":{
      "class":"java.util.Map<java.lang.String, java.lang.Integer>",
      "value":{
         "o":2
      }
   }
}
""".trimIndent()

@ExperimentalStdlibApi
fun main() {
    val test = TestClass()
    println(test.convertToJsonObject())
}
@ExperimentalStdlibApi
fun Any.convertToJsonObject(): JSONObject {
    return writeClassToJson()
}

@ExperimentalStdlibApi
fun Any.writeClassToJson() : JSONObject {
    val classJson = JSONObject()
    return classJson.put(this::class.qualifiedName, this.writeClassToJsonIterable())
}

@ExperimentalStdlibApi
private fun Any.writeClassToJsonIterable(classJSON: JSONObject = JSONObject()): JSONObject {
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
            if(it.returnType.javaType.typeName == "java.lang.Void"|| it.getter.call(this) == null) {
                classJSON.put(it.name, mapOf("class" to it.returnType.javaType.typeName, "value" to "null"))
            } else {
                val value = it.getter.call(this)
                classJSON.put(it.name, mapOf("class" to it.returnType.javaType.typeName, "value" to value!!.writeClassToJsonIterable()))
            }
        } else {
            classJSON.put(it.name, mapOf("class" to it.returnType.javaType.typeName, "value" to it.getter.call(this)))
        }
    }
    return classJSON
}

@Throws(RuntimeException::class)
inline fun <reified returnType> String.readJsonToClass(): returnType {
    val returnClass = returnType::class.java
    val classJson = JSONObject(this)
    classJson.forEach {
        val value = it.second
        if (value is JSONObject) {
            val fieldInfo = value
            val fieldClassInstance = Class.forName(fieldInfo.getString("class")).getDeclaredConstructor().newInstance()

            println(fieldClassInstance)

            returnClass.getField(it.first).set(null, fieldInfo.get("value"))
        }
    }
    return returnClass.getDeclaredConstructor().newInstance()
}

fun <T> Array<T>.generateJsonArray(): JSONArray {
    val returnArray = JSONArray()
    forEach {
        returnArray.put(it)
    }
    return returnArray
}