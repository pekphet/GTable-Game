package cc.fish91.gtable

import cc.fish91.gtable.engine.FightEffects
import cc.fish91.gtable.engine.IFightEffect
import kotlin.reflect.KClass

enum class RoleType(val info: String, val pSkill: KClass<out IFightEffect>) {
    BEGINNER("初心者", FightEffects.NANSkill::class),
    KNIGHT("骑士", FightEffects.KNIGHTPSkill::class),
    ROGUE("暗杀者", FightEffects.ROGUEPSkill::class),
    FIGHTER("格斗家", FightEffects.FighterPSkill::class),
    HUNTER("猎人", FightEffects.NANSkill::class),
    NEC("死灵术士", FightEffects.NECPSkill::class),
}

enum class FloorStatus {
    IDLE,
    MONSTER,
    GIFT,
    STAIR_UP,
    STAIR_DN,
    MONSTER_K,
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
    HP_RESTORE("回复力"),
    ATK("攻击力"),
    DEF("防御力"),
    HP("体力值"),
    ATK_PC("攻击力追加"),
    DEF_PC("防御力追加"),
    HP_PC("体力值追加"),
    CRITICAL("暴击率"),
    CRITICAL_DMG("暴伤率"),
    MISS("闪避率")

}

enum class EquipPosition(val desc: String) {
    ARMOR("武器"),
    WEAPON("防具"),
    RING("戒指"),
}

enum class ExEffect {
    ATK_UP,
    DEF_UP,
    HP_UP,
    ATK_UP_PC,
    DEF_UP_PC,
    HP_UP_PC,
    CRITICAL_UP,
    CRITICAL_DMG_UP,
    HP_RESTORE,
    CUT_10,
    CUT_20,
    KILL,
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