//import extensioneer.isNull
//import extensioneer.isNullWithResult
//import extensioneer.notNull
//import extensioneer.notNullWithResult
//import org.junit.Assert.assertEquals
//import org.junit.Test
//
//class BaseTest {
//    @Test
//    fun testIsNull() {
//        val nullVar: Any? = null
//        var isNull = false
//        nullVar.isNull {
//            isNull = true
//        }
//        assertEquals(true, isNull)
//    }
//
//    @Test
//    fun testNotNull() {
//        val nullableVar: String? = "testString"
//        var isNull = true
//        nullableVar.notNull {
//            isNull = false
//        }
//        assertEquals(false, isNull)
//    }
//
//    @Test
//    fun testNotNullWithResult() {
//        var expectedResult: String? = null
//        var actualResult: String? = expectedResult.notNullWithResult {
//            return@notNullWithResult this
//        }
//        assertEquals(null, actualResult)
//
//        expectedResult = "testResult"
//        actualResult = expectedResult.notNullWithResult {
//            return@notNullWithResult this
//        }
//        assertEquals(expectedResult, expectedResult)
//    }
//
//    @Test
//    fun testIsNullWithResult() {
//        var expectedResult: String? = null
//        var actualResult: String? = expectedResult.isNullWithResult {
//            return@isNullWithResult this
//        }
//        assertEquals(actualResult, null)
//
//        expectedResult = "testResult"
//        actualResult = expectedResult.isNullWithResult {
//            return@isNullWithResult this
//        }
//        assertEquals(actualResult, null)
//    }
//}