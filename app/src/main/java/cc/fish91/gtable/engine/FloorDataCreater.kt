package cc.fish91.gtable.engine

import cc.fish91.gtable.FloorMeta
import cc.fish91.gtable.FloorStatus
import cc.fish91.gtable.plugin.Math
import cc.fish91.gtable.resource.StaticData
import java.util.*

object FloorDataCreater {
    private val mInitialFloorMeta = FloorMeta()
    private val mData = Collections.synchronizedList(List(35) { FloorMeta() })
    private val mUsedBounds = mutableListOf<Int>()
    fun create(floor: Int, success: List<FloorMeta>.() -> Unit) {
        cleanData()
        setFloor(FloorStatus.STAIR_UP)
        setFloor(FloorStatus.STAIR_DN)
        setFloor(FloorStatus.MONSTER_K)
        setFloor(FloorStatus.MONSTER, getMonsterCount(floor))
        setFloor(FloorStatus.GIFT, Math.rand(2, getGiftCount(floor)))
        setFloor(FloorStatus.BUFF, Math.rand(1, getBufCount(floor)))
        if (floor%StaticData.AREA_SPLIT_FLOORS == StaticData.AREA_SPLIT_FLOORS - 1) {
            setFloor(FloorStatus.MONSTER_SP)
        }
//        if (floor % 3 == 2) {
//            setFloor(FloorStatus.MONSTER_SP)
//        }

        mData.success()
    }

    fun cleanData() {
        mData.map { it.restore() }
        mUsedBounds.clear()
    }

    private fun FloorMeta.restore() {
        mInitialFloorMeta.let {
            isOpened = it.isOpened
            isNearMonster = it.isNearMonster
            status = it.status
            exId = it.exId
        }
    }

    fun getMonsterCount(floor: Int) = if (floor < 18) floor / 3 + 3 else 9
    private fun getGiftCount(floor: Int) = if (floor < 28) floor / 4 + 2 else 9
    private fun getBufCount(floor: Int) = if (floor < 20) floor / 4 + 1 else 6

    @Synchronized
    private fun setFloor(status: FloorStatus, times: Int = 1) {
        var mTimes = times
        try {
            if (mTimes >= 33)
                return
            while (mTimes > 0) {
                mData[rand()].let {
                    it.status = status
                    it.exId = --mTimes
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }


    private fun rand(): Int {
        var result: Int
        do {
            result = Math.rand(0, 34)
        } while (result in mUsedBounds)
        mUsedBounds.add(result)
        return result
    }
}
