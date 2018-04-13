package cc.fish91.gtable.engine

import cc.fish91.gtable.*
import cc.fish91.gtable.plugin.Math
import cc.fish91.gtable.plugin.getRand
import cc.fish91.gtable.resource.StaticData

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
                this.baseHP * (1 + floor / 2) + Math.rand(floor),
                this.baseAtk * (1 + floor / 2) + Math.rand(floor / 2),
                this.baseDef * (1 + floor / 2) + Math.rand(floor / 2),
                this.baseExp * (1 + floor / 2) + Math.rand(floor),
                Math.rand(this.baseGold * 3) + baseGold * floor,
                id)
    }


    fun createGift(area: Int, floor: Int) = getGift(Gifts.values().getRand(), floor)

    private fun getGift(g: Gifts, floor: Int) = Gift(g, when (g) {
        Gifts.ATK_UP -> 1 + Math.rand(floor / 3)
        Gifts.DEF_UP -> 1 + Math.rand(floor / 3)
        Gifts.HP_RESTORE -> floor + Math.rand(floor * 2)
    })

    fun createBuff(area: Int, floor: Int) = getBuff(BuffAbility.values().getRand(), floor)

    private fun getBuff(buffA: BuffAbility, floor: Int) = Buff(buffA, when (buffA) {
        BuffAbility.ATK -> 1 + Math.rand(floor + 1) / 2
        BuffAbility.DEF -> 1 + Math.rand(floor + 1) / 2
//        BuffAbility.HP -> floor + Math.rand(floor)
    })
}