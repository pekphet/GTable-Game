package cc.fish91.gtable.resource

import cc.fish91.gtable.*

object StaticData {

    /****Static Data**************/

    private val mMonsterMap = mutableMapOf(
            Pair(1, BaseMonster(2, 1, 1, "M-a", 2, 1, R.drawable.t_icon_monster_mashroom, Pair(0x1, 2))),
            Pair(2, BaseMonster(4, 1, 2, "M-b", 5, 3, R.drawable.t_icon_monster_steelpig, Pair(0x1001, 25))),
            Pair(3, BaseMonster(1, 4, 1, "M-c", 5, 6, R.drawable.t_icon_monster_star, Pair(0x2001, 30)))
    )

    private val mAreaMonsterMap = mutableMapOf(
            Pair(1, listOf(1, 2, 3))
    )

    private val mEquipInfoMap = mutableMapOf(
            Pair(0x0001, EquipInfo("短剑", EquipPosition.ARMOR, 2, R.drawable.t_icon_a_001, null)),
            Pair(0x1001, EquipInfo("布衣", EquipPosition.WEAPON, 2, R.drawable.t_icon_w_001, null)),
            Pair(0x2001, EquipInfo("蓝宝石戒指", EquipPosition.RING, 2, R.drawable.t_icon_r_001, null))
    )


    /****Monster****************/
    //  will add CACHE  later!!
    fun getBaseMonster(id: Int) = mMonsterMap[id]!!

    fun getMonsterPool(areaId: Int) = mAreaMonsterMap[areaId]!!


    /****Gift ********************/
    fun getGiftInfo(gift: Gifts) = when (gift) {
        Gifts.ATK_UP -> Pair(R.drawable.t_icon_atk, gift.desc)
        Gifts.DEF_UP -> Pair(R.drawable.t_icon_def, gift.desc)
        Gifts.HP_ARMOR -> Pair(R.drawable.t_icon_hpup, gift.desc)
    }

    /**** Buff**********************/
    fun getBuffInfo(buffA: BuffAbility) = when (buffA) {
        BuffAbility.ATK -> Pair(R.drawable.t_icon_buff_atk, buffA.desc)
        BuffAbility.DEF -> Pair(R.drawable.t_icon_buff_def, buffA.desc)
//        BuffAbility.ARMOR -> Pair(R.drawable.t_icon_buff_armor, buffA.desc)
    }


    /****Person Property********/

    fun getLvGrow(roleType: RoleType = RoleType.BEGINNER) = when (roleType) {
        RoleType.BEGINNER -> PersonData(20, 2, 2, 0, 0, 0, 0)
        else -> PersonData(20, 2, 2, 0, 0, 0, 0)
    }

    fun getUPFee(currentLevel: Int) = 500 * currentLevel + 200

    fun getUppedAtk(originAtk: Int, atkLv: Int) = (originAtk * (1 + 0.1 * atkLv) + atkLv).toInt()
    fun getUppedDef(originDef: Int, defLv: Int) = (originDef * (1 + 0.1 * defLv) + defLv).toInt()
    fun getUppedHP(originHP: Int, hpLv: Int) = (originHP * (1 + 0.1 * hpLv) + hpLv * 10).toInt()

    fun getLimitExp(level: Int) = level * level * 20

    fun statusUpCalc(person: PersonData, personBought: PersonBought) {
        person.let {
            it.HP = getUppedHP(person.HP, personBought.hpLv)
            it.atk = getUppedHP(person.atk, personBought.atkLv)
            it.def = getUppedHP(person.def, personBought.defLv)
        }
    }

    /****Equip Area**********************/

    fun getBaseEquipInfo(id: Int) = mEquipInfoMap[id]!!

}