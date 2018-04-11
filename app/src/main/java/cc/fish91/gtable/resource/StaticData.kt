package cc.fish91.gtable.resource

import cc.fish91.gtable.*

object StaticData {

    private val mMonsterMap = mutableMapOf(
            Pair(1, BaseMonster(2, 1, 1, "M-a", 2, 1)),
            Pair(2, BaseMonster(4, 1, 2, "M-b", 5, 3)),
            Pair(3, BaseMonster(1, 4, 1, "M-c", 5, 4))
    )

    private val mAreaMonsterMap = mutableMapOf(
            Pair(1, listOf(1, 2, 3))
    )


    //  will add CACHE  later!!
    fun getBaseMonster(id: Int) = mMonsterMap[id]!!

    fun getMonsterPool(areaId: Int) = mAreaMonsterMap[areaId]!!


    fun getGiftInfo(gift: Gifts) = when (gift) {
        Gifts.ATK_UP -> Pair("ATK", "make attack up")
        Gifts.DEF_UP -> Pair("DEF", "make defence up")
        Gifts.HP_RESTORE -> Pair("HP", "restore HP")
    }

    fun getBuffInfo(buffA: BuffAbility) = when (buffA) {
        BuffAbility.ATK -> Pair("Buff-ATK", "make attack up")
        BuffAbility.DEF -> Pair("Buff-DEF", "make defence up")
    }

}