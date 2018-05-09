package cc.fish91.gtable.resource

import cc.fish91.gtable.*
import cc.fish91.gtable.engine.FightEffects
import cc.fish91.gtable.engine.IFightEffect
import cc.fish91.gtable.plugin.getRand

object StaticData {
    /****CONSTANTS**************/
    const val DEFAULT_MONSTER_AREA = 7
    const val CHANGE_ROLE_TYPE_1 = 30
    const val AREA_SPLIT_FLOORS = 30
    const val KEEP_EQUIP_FLOORS = 40

    const val GET_SP_WEIGHT = 30

    const val EQ_RARE_TOP = 6
    const val EQ_RARE_WHITE = 0
    const val EQ_RARE_BLUE = 1
    const val EQ_RARE_RARE = 2
    const val EQ_RARE_ART = 3
    const val EQ_RARE_EPIC = 4
    const val EQ_RARE_SUIT = 5
    const val EQ_RARE_SINGLE_SP = 6


    /****Static Data**************/

    private val mMonsterMap = mutableMapOf(
            Pair(1, BaseMonster(2, 1, 1, "蓝蜗牛", 5, 6, R.drawable.t_icon_monster_bluewn, arrayOf(Pair(0x0001, 15)), FightEffects.CutP10HP::class)),
            Pair(2, BaseMonster(4, 1, 2, "红蜗牛", 8, 6, R.drawable.t_icon_monster_redwn, arrayOf(Pair(0x1001, 15)), FightEffects.DcsPDef::class)),
            Pair(3, BaseMonster(1, 3, 1, "花蘑菇", 2, 3, R.drawable.t_icon_monster_mashroom, arrayOf(Pair(0x2001, 20)), FightEffects.DscPAtk::class)),

            Pair(4, BaseMonster(3, 2, 2, "绿水灵", 8, 6, R.drawable.t_icon_monster_gball, arrayOf(Pair(0x2002, 25)), FightEffects.ClearPArmor::class)),
            Pair(5, BaseMonster(2, 3, 3, "蓝水灵", 8, 6, R.drawable.t_icon_monster_bball, arrayOf(Pair(0x0002, 20)), FightEffects.DscFA1Def::class)),
            Pair(6, BaseMonster(4, 2, 5, "钢猪猪", 2, 3, R.drawable.t_icon_monster_steelpig, arrayOf(Pair(0x1002, 20)), FightEffects.RebP10Atk::class)),

            Pair(7, BaseMonster(2, 0, 2, "星光精灵", 2, 3, R.drawable.t_icon_monster_star, arrayOf(Pair(0x2003, 30)), FightEffects.CopyPAtk::class)),
            Pair(8, BaseMonster(4, 3, 4, "月光精灵", 5, 6, R.drawable.t_icon_monster_moon, arrayOf(Pair(0x1003, 25)), FightEffects.ClearPArmor::class)),
            Pair(9, BaseMonster(3, 4, 5, "日光精灵", 5, 6, R.drawable.t_icon_monster_sun, arrayOf(Pair(0x0003, 25)), FightEffects.DscFA5Atk::class))

    )

    private val mSPMonsters = mapOf(
            Pair(0x3001, BaseMonster(10, 10, 10, "魂·蓝蜗牛", 20, 20, R.drawable.t_icon_monster_bluewn,
                    arrayOf(Pair(0x3001, 2)), FightEffects.CutP10HP::class)),
            Pair(0x3002, BaseMonster(11, 14, 8, "魂·红蜗牛", 20, 20, R.drawable.t_icon_monster_redwn,
                    arrayOf(Pair(0x3002, 2)), FightEffects.DcsPDef::class)),
            Pair(0x3003, BaseMonster(10, 14, 7, "魂·花蘑菇", 20, 20, R.drawable.t_icon_monster_mashroom,
                    arrayOf(Pair(0x3003, 2)), FightEffects.DscPAtk::class)),
            Pair(0x3004, BaseMonster(12, 9, 10, "魂·绿水灵", 20, 20, R.drawable.t_icon_monster_gball,
                    arrayOf(Pair(0x3001, 2)), FightEffects.CutP10HP::class)),
            Pair(0x3005, BaseMonster(13, 11, 7, "魂·蓝水灵", 20, 20, R.drawable.t_icon_monster_bball,
                    arrayOf(Pair(0x3002, 2)), FightEffects.DcsPDef::class)),
            Pair(0x3006, BaseMonster(12, 6, 12, "魂·钢猪猪", 20, 20, R.drawable.t_icon_monster_steelpig,
                    arrayOf(Pair(0x3003, 2)), FightEffects.DscPAtk::class)),
            Pair(0x3007, BaseMonster(8, 12, 11, "魂·星光精灵", 20, 20, R.drawable.t_icon_monster_star,
                    arrayOf(Pair(0x3001, 2)), FightEffects.ClearPArmor::class)),
            Pair(0x3008, BaseMonster(8, 10, 12, "魂·月光精灵", 20, 20, R.drawable.t_icon_monster_moon,
                    arrayOf(Pair(0x3002, 2)), FightEffects.CopyPAtk::class)),
            Pair(0x3009, BaseMonster(8, 12, 12, "魂·日光精灵", 20, 20, R.drawable.t_icon_monster_sun,
                    arrayOf(Pair(0x3003, 2)), FightEffects.CopyPDef::class))
    )

    private val mAreaMonsterMap = mutableMapOf(
            Pair(1, listOf(1, 2, 3)),
            Pair(2, listOf(2, 3, 5)),
            Pair(3, listOf(3, 4, 5)),
            Pair(4, listOf(4, 5, 6)),
            Pair(5, listOf(5, 6, 7)),
            Pair(6, listOf(6, 7, 8)),
            Pair(7, listOf(7, 8, 9))
    )

    private val mEquipInfoMap = mutableMapOf(
            Pair(0x0001, EquipInfo(0x0001, "布衣", EquipPosition.ARMOR, 2, R.drawable.t_icon_a_001, 50, 0, null)),
            Pair(0x1001, EquipInfo(0x1001, "短剑", EquipPosition.WEAPON, 2, R.drawable.t_icon_w_001, 50, 0, null)),
            Pair(0x2001, EquipInfo(0x2001, "蓝宝石戒指", EquipPosition.RING, 2, R.drawable.t_icon_r_001, 50, 0, null)),
            Pair(0x0002, EquipInfo(0x0002, "木甲", EquipPosition.ARMOR, 4, R.drawable.t_icon_a_002, 70, 12, null)),
            Pair(0x1002, EquipInfo(0x1002, "长剑", EquipPosition.WEAPON, 4, R.drawable.t_icon_w_002, 70, 18, null)),
            Pair(0x2002, EquipInfo(0x2002, "蓝魂戒指", EquipPosition.RING, 4, R.drawable.t_icon_r_002, 70, 6, null)),
            Pair(0x0003, EquipInfo(0x0003, "铁甲", EquipPosition.ARMOR, 8, R.drawable.t_icon_a_003, 100, 36, null)),
            Pair(0x1003, EquipInfo(0x1003, "巨剑", EquipPosition.WEAPON, 8, R.drawable.t_icon_w_003, 100, 30, null)),
            Pair(0x2003, EquipInfo(0x2003, "灵犀之心", EquipPosition.RING, 8, R.drawable.t_icon_r_003, 100, 24, null))
    )

    private val mSPEquipInfoMap = mutableMapOf(
            Pair(0x4001, EquipInfo(0x4001, "刺心咒甲", EquipPosition.ARMOR, 10, R.drawable.t_icon_a_003, 120, 0, null)),
            Pair(0x4101, EquipInfo(0x4101, "刺心咒刃", EquipPosition.WEAPON, 10, R.drawable.t_icon_w_003, 120, 0, null)),
            Pair(0x4201, EquipInfo(0x4201, "刺心咒戒", EquipPosition.RING, 10, R.drawable.t_icon_r_003, 120, 0, null)),
            Pair(0x4002, EquipInfo(0x4002, "圣御战甲", EquipPosition.ARMOR, 10, R.drawable.t_icon_a_003, 120, 0, null)),
            Pair(0x4102, EquipInfo(0x4102, "圣御佩剑", EquipPosition.WEAPON, 10, R.drawable.t_icon_w_003, 120, 0, null)),
            Pair(0x4202, EquipInfo(0x4202, "圣御魂戒", EquipPosition.RING, 10, R.drawable.t_icon_r_003, 120, 0, null)),
            Pair(0x4003, EquipInfo(0x4003, "狂暴披风", EquipPosition.ARMOR, 10, R.drawable.t_icon_a_003, 120, 0, null)),
            Pair(0x4103, EquipInfo(0x4103, "狂暴拳套", EquipPosition.WEAPON, 10, R.drawable.t_icon_w_003, 120, 0, null)),
            Pair(0x4203, EquipInfo(0x4203, "狂暴之戒", EquipPosition.RING, 10, R.drawable.t_icon_r_003, 120, 0, null)),
            Pair(0x4004, EquipInfo(0x4004, "祭祀长袍", EquipPosition.ARMOR, 10, R.drawable.t_icon_a_003, 120, 0, null)),
            Pair(0x4104, EquipInfo(0x4104, "血之法杖", EquipPosition.WEAPON, 10, R.drawable.t_icon_w_003, 120, 0, null)),
            Pair(0x4204, EquipInfo(0x4204, "暗影魔戒", EquipPosition.RING, 10, R.drawable.t_icon_r_003, 120, 0, null)),

            Pair(0x3001, EquipInfo(0x3001, "能量战甲", EquipPosition.ARMOR, 14, R.drawable.t_icon_a_002, 150, 0, null)),
            Pair(0x3002, EquipInfo(0x3002, "妖刀-瞬斩", EquipPosition.WEAPON, 14, R.drawable.t_icon_w_002, 150, 0, Pair(ExEffect.CUT_20, 20))),
            Pair(0x3003, EquipInfo(0x3003, "闪耀之戒", EquipPosition.RING, 14, R.drawable.t_icon_r_002, 150, 0, null))
    )


    /****Monster****************/
    //  will add CACHE  later!!
    fun getBaseMonster(id: Int) = mMonsterMap[id]?: mSPMonsters[id]!!

    fun getMonsterPool(areaId: Int) = mAreaMonsterMap[areaId]
            ?: mAreaMonsterMap[DEFAULT_MONSTER_AREA]!!

    fun getDropEquipsOfArea(areaId: Int) = getMonsterPool(areaId).map {
        mMonsterMap[it]?.drop?.get(0)?.first ?: 0x0001
    }

    fun getRandSPMonsterId() = mSPMonsters.keys.toList().getRand()
    fun getRandSPEquip() = mSPEquipInfoMap.keys.filter { it >= 0x4000 }.toList().getRand()

    /****Gift ********************/
    fun getGiftInfo(gift: Gifts) = when (gift) {
        Gifts.ATK_UP -> Pair(R.drawable.t_icon_atk, gift.desc)
        Gifts.DEF_UP -> Pair(R.drawable.t_icon_def, gift.desc)
        Gifts.HP_ARMOR -> Pair(R.drawable.t_icon_buff_armor, gift.desc)
    }

    /**** Buff**********************/
    fun getBuffInfo(buffA: BuffAbility) = when (buffA) {
        BuffAbility.ATK -> Pair(R.drawable.t_icon_buff_atk, buffA.desc)
        BuffAbility.DEF -> Pair(R.drawable.t_icon_buff_def, buffA.desc)
//        BuffAbility.ARMOR -> Pair(R.drawable.t_icon_buff_armor, buffA.desc)
    }


    /****Person Property********/
    fun getPersonInit(roleType: RoleType, gold: Int = 0) = when (roleType) {
        RoleType.BEGINNER -> PersonData(100, 2, 2, 0, 1, gold, roleType = roleType)
        RoleType.FIGHTER -> PersonData(200, 40, 20, 0, 1, gold, roleType = roleType, ex = ExPerson(200, 350, 150))
        RoleType.NEC -> PersonData(150, 22, 22, 0, 1, gold, roleType = roleType, ex = ExPerson(150, 150, 170))
        RoleType.KNIGHT -> PersonData(250, 20, 40, 0, 1, gold, roleType = roleType, ex = ExPerson(100, 150, 120))
        RoleType.ROGUE -> PersonData(150, 25, 20, 0, 1, gold, roleType = roleType, ex = ExPerson(300, 350, 300))
        else -> PersonData(100, 2, 2, 0, 1, 0)
    }

    fun getLvGrow(roleType: RoleType = RoleType.BEGINNER) = when (roleType) {
        RoleType.BEGINNER -> PersonData(20, 2, 2, 0, 0, 0, 0)
        RoleType.FIGHTER -> PersonData(40, 3, 3, 0, 0, 0, 0)
        RoleType.NEC -> PersonData(30, 3, 4, 0, 0, 0, 0)
        RoleType.KNIGHT -> PersonData(60, 1, 4, 0, 0, 0, 0)
        RoleType.ROGUE -> PersonData(20, 6, 2, 0, 0, 0, 0)
        else -> PersonData(20, 2, 2, 0, 0, 0, 0)
    }

    fun getUPFee(currentLevel: Int) = 300 * currentLevel + 100

    fun getUppedAtk(originAtk: Int, atkLv: Int) = (originAtk * (1 + 0.02 * atkLv) + atkLv * 2).toInt()
    fun getUppedDef(originDef: Int, defLv: Int) = (originDef * (1 + 0.02 * defLv) + defLv * 2).toInt()
    fun getUppedHP(originHP: Int, hpLv: Int) = (originHP * (1 + 0.02 * hpLv) + hpLv * 20).toInt()

    fun getLimitExp(level: Int) = level * level * 15

    fun statusUpCalc(person: PersonData, personBought: PersonBought) {
        person.let {
            it.HP = getUppedHP(person.HP, personBought.hpLv)
            it.atk = getUppedHP(person.atk, personBought.atkLv)
            it.def = getUppedHP(person.def, personBought.defLv)
        }
    }

    /****Equip Area**********************/

    fun getBaseEquipInfo(id: Int) = mEquipInfoMap[id]?: mSPEquipInfoMap[id]?: mEquipInfoMap[0x0001]!!

    fun getAllEquips() = mEquipInfoMap.keys

}