package cc.fish91.gtable.engine

import cc.fish91.gtable.*
import cc.fish91.gtable.plugin.Math
import cc.fish91.gtable.plugin.getRand
import cc.fish91.gtable.resource.StaticData
import kotlin.math.roundToInt

object FloorEngine {    //Floor start with 0

    fun createMonsters(area: Int, floor: Int, count: Int) = mutableListOf<MonsterData>().apply {
        StaticData.getMonsterPool(area).let {
            for (i in 1..count) {
                this.add(createMonster(it.getRand(), floor))
            }
        }
    }

    private fun createMonster(id: Int, floor: Int) = StaticData.getBaseMonster(id).run {
        MonsterData(
                this.baseHP * (1 + floor) + Math.rand(floor),
                this.baseAtk * (1 + floor) + Math.rand(floor / 2),
                this.baseDef * (1 + floor) + Math.rand(floor / 2),
                Math.rand(this.baseExp * (floor + 5) / 3),
                Math.rand(this.baseGold * (floor + 5) / 3), id)
    }


    fun createGift(area: Int, floor: Int) = getGift(Gifts.values().getRand(), floor)

    private fun getGift(g: Gifts, floor: Int) = Gift(g, when (g) {
        Gifts.ATK_UP -> 1 + Math.rand(java.lang.Math.sqrt(floor.toDouble()).roundToInt() + 1) / 2
        Gifts.DEF_UP -> 1 + Math.rand(java.lang.Math.sqrt(floor.toDouble()).roundToInt() + 1) / 2
        Gifts.HP_ARMOR -> 1 + Math.rand(java.lang.Math.sqrt(floor.toDouble()).roundToInt() + 1)
    })

    fun createBuff(area: Int, floor: Int) = getBuff(BuffAbility.values().getRand(), floor)

    private fun getBuff(buffA: BuffAbility, floor: Int) = Buff(buffA, when (buffA) {
        BuffAbility.ATK -> 1 + Math.rand(java.lang.Math.sqrt(floor.toDouble()).roundToInt() + 1)
        BuffAbility.DEF -> 1 + Math.rand(java.lang.Math.sqrt(floor.toDouble()).roundToInt() + 1)
//        BuffAbility.ARMOR -> 1 + Math.rand(floor + 1) / 2
    })
}