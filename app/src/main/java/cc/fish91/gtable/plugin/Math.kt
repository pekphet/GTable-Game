package cc.fish91.gtable.plugin

import java.lang.Math
import java.util.*

object Math {
    private val mRand = Random()
    fun rand(from: Int, to: Int) = mRand.nextInt(to - from + 1) + from
    fun rand(size: Int) = rand(0, size)

    fun getNearBlocks(position: Int) = mutableListOf<Int>().apply {
        if (position > 4)
            add(position - 5)
        if (position % 5 != 0)
            add(position - 1)
        if (position % 5 != 4)
            add(position + 1)
        if (position < 5 * 6)
            add(position + 5)
    }

    fun getNear9Blocks(position: Int) = mutableListOf<Int>().apply {
        if (position > 4) {
            add(position - 5)
            if (position % 5 != 0)
                add(position - 6)
            if (position % 5 != 4)
                add(position - 4)
        }
        if (position < 5 * 6) {
            add(position + 5)
            if (position % 5 != 0)
                add(position + 4)
            if (position % 5 != 4)
                add(position + 6)
        }
        if (position % 5 != 0)
            add(position - 1)
        if (position % 5 != 4)
            add(position + 1)
    }

    fun forceMinus(a: Int, b: Int) = if (a <= b) 1 else a - b

    fun <T> chance(vararg member: Pair<T, Int>): T {
        rand(1, member.map { it.second }.reduce(Int::plus)).let {
            var resultVal = it
            for (i in member) {
                if (resultVal in 1..i.second)
                    return i.first
                else resultVal -= i.second
            }
            return member[0].first
        }
    }

    fun denominatorOf(den: Int, times: Int = 1) = rand(1, den) == times
    fun percent(weight: Int) = rand(1, 100) in 1..weight
    fun mil_percent(weight: Int) = rand(1, 1000) in 1..weight

    fun limitAdd(origin: Int, increase: Int, limit: Int) = (origin + increase).apply { if (this > limit) limit else this }
    fun min(origin: Int, sec: Int) = if (origin < sec) origin else sec

}