package cc.fish91.gtable

import cc.fish91.gtable.plugin.Math
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
        println((19/10f).toInt())
//        val ts = System.currentTimeMillis()
//        for (i in 1..10000) {
//            mArray[Math.chance(Pair(0, 20), Pair(1, 30), Pair(2, 40), Pair(3, 10))]++
//        }
//        println("use: ${System.currentTimeMillis() - ts}ms")
//        println("A:${mArray[0] / 10000.0}\nB:${mArray[1] / 10000.0}\nC:${mArray[2] / 10000.0}\nD:${mArray[3] / 10000.0}\n")
    }
}
