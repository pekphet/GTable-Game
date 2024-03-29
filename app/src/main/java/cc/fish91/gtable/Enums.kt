package cc.fish91.gtable

import cc.fish91.gtable.engine.FightEffects
import cc.fish91.gtable.engine.IFightEffect
import kotlin.reflect.KClass

enum class RoleType(val info: String, val pSkill: KClass<out IFightEffect>) {
    BEGINNER(resString(R.string.role_beginner), FightEffects.NANSkill::class),
    KNIGHT(resString(R.string.role_knight), FightEffects.KNIGHTPSkill::class),
    ROGUE(resString(R.string.role_rogue), FightEffects.ROGUEPSkill::class),
    FIGHTER(resString(R.string.role_fighter), FightEffects.FighterPSkill::class),
    HUNTER("猎人", FightEffects.NANSkill::class),
    NEC(resString(R.string.role_nec), FightEffects.NECPSkill::class),
}

enum class FloorStatus {
    IDLE,
    MONSTER,
    GIFT,
    STAIR_UP,
    STAIR_DN,
    MONSTER_K,
    MONSTER_SP,
    BUFF,
    DROP,
}

enum class BuffAbility(val desc: String) {
    ATK("攻击提升"),
    DEF("防御提升"),
//    ARMOR("护甲提升"),
}

enum class Gifts(val desc: String) {
    HP_ARMOR("提升护盾"),
    ATK_UP("攻击提升"),
    DEF_UP("防御提升"),
}

enum class EquipProperty(val description: String) {
    HP_RESTORE(resString(R.string.attr_recover)),
    ATK(resString(R.string.attr_atk)),
    DEF(resString(R.string.attr_def)),
    HP(resString(R.string.attr_hp)),
    ATK_PC("攻击力追加"),
    DEF_PC("防御力追加"),
    HP_PC("体力值追加"),
    CRITICAL(resString(R.string.attr_cri)),
    CRITICAL_DMG(resString(R.string.attr_cratk)),
    MISS(resString(R.string.attr_miss))

}

enum class EquipPosition(val desc: String) {
    ARMOR("武器"),
    WEAPON("防具"),
    RING("戒指"),
}

enum class ExEffect(val info: String) {
    ATK_UP("攻击力提升%d点"),
    DEF_UP("防御力提升%d点"),
    HP_UP("体力值提升%d点"),
    ATK_UP_PC("攻击力提升%d%%"),
    DEF_UP_PC("防御力提升%d%%"),
    HP_UP_PC("体力值提升%d%%"),
    CRITICAL_UP("暴击率提升%s"),
    MISS_UP("闪避率提升%s"),
    CRITICAL_DMG_UP("暴伤率提升%d%%"),
    HP_RESTORE("回复力提升%d点"),
    CUT_10("攻击时%d%%几率削减10%%体力值"),
    CUT_20("攻击时%d%%几率削减20%%体力值"),
    KILL("攻击时%d%%几率秒杀"),
}

enum class TaskType(val info: String) {
    KILL_MONSTER("消灭<font color='#ffee33'>[%s]</font>%d只"),
    UP_FLOORS("到达%d层"),
}

enum class TaskAwardType(val info: String) {
    Equip("装备"),
    Gold("金币"),
    Exp("经验值"),
}

enum class EquipSuit(val id: Int, val info: String) {
    ROGUE_SUIT_1(1, "【刺心套装】\n增加50%攻击力\n增加30%暴击率\n增加200%暴击伤害\n增加20%闪避率"),
    KNIGHT_SUIT_1(2, "【圣御套装】\n提升50%防御力\n降低50%攻击力\n回避率提升20%\n体力值提升100%"),
    FIGHTER_SUIT_1(3, "【狂暴套装】\n提升70%攻击与防御力\n回复力提升10点\n暴击率提升20%"),
    NEC_SUIT_1(4, "【血祭套装】\n体力值提升50%\n回复力降低30点\n回避率提升30%\n暴击率提升20%\n暴击伤害提升150%"),
}

enum class ExchangeCodeType(val desc: String) {
    EQUIP_W("获取装备!"),
    EQUIP_A("获取装备!"),
    EQUIP_R("获取装备!"),
    GOLD("获取金币"),
    LV("打个怪～升级"),
    CHANGETYPE("打个怪～转职"),
    ERROR("兑换码错误"),
}
