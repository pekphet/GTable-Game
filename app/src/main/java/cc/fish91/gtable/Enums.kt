package cc.fish91.gtable

enum class RoleType(val info: String) {
    BEGINNER("初心者"),
    KNIGHT("骑士"),
    ROGUE("暗杀者"),
    FIGHTER("格斗家"),
    HUNTER("猎人"),
    NEC("死灵术士"),
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

enum class BuffAbility {
    ATK,
    DEF,
    ARMOR,
}

enum class Gifts {
    HP_RESTORE,
    ATK_UP,
    DEF_UP,
}

enum class EquipProperty(val description: String) {
    HP_RESTORE("生命回复"),
    ATK("攻击力"),
    DEF("防御力"),
    HP("生命值"),
    ATK_PC("攻击力追加"),
    DEF_PC("防御力追加"),
    HP_PC("生命值追加"),
    CRITICAL("暴击率"),
    CRITICAL_DMG("暴击伤害比率"),
    MISS("闪避率")

}

enum class EquipPosition {
    ARMOR,
    WEAPON,
    RING,
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