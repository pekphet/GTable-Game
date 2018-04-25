package cc.fish91.gtable

import cc.fish91.gtable.plugin.Math
import com.google.gson.Gson
import org.junit.Test

import org.junit.Assert.*
import java.time.Instant

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    val mArray = arrayOf(0, 0, 0, 0)
    @Test
    fun test() {
        val a = AA("text",
                mutableMapOf(Pair("12", "A"), Pair("14", "F")),
                mutableMapOf(Pair("x12", listOf("A", "B", "C")), Pair("x14", listOf("D", "F"))))
        println(Gson().toJson(a))
    }

    data class AA(var title: String, val content: MutableMap<String, String>, val exC: MutableMap<String, List<String>>)
}
