package cc.fish91.gtable

import cc.fish91.gtable.plugin.Math
import com.google.gson.Gson
import org.junit.Test

import org.junit.Assert.*
import java.time.Instant
import kotlin.reflect.KClass

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
        val bClz: KClass<B> = B::class

        val bObj = bClz.objectInstance!!
        B.ii = 22

        println(bObj.ii)



    }

    data class AA(var title: String, val content: MutableMap<String, String>, val exC: MutableMap<String, List<String>>)

    object B {
        var ii = 11
    }
}
