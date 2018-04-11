package cc.fish91.gtable

import org.junit.Test

import org.junit.Assert.*

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

    @Test
    fun test() {
        val pp = PP("231", 2, 0.5)
        println(pp)
        changePP(pp)
        println(pp)
        changePP(pp)
        println(pp)
    }

    fun changePP(pp: PP) {
        pp.t1 = "123"
        pp.t2 += 5
        pp.t3 += 0.11
    }

}


data class PP(var t1: String, var t2: Int, var t3: Double)