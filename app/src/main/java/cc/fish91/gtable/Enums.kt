package cc.fish91.gtable

enum class RoleType(name: String) {
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

enum class EquipProperty(description: String) {
    HP_RESTORE(""),
    ATK(""),
    DEF(""),
    HP(""),
    ATK_PC(""),
    DEF_PC(""),
    HP_PC(""),
    CRITICAL(""),
    CRITICAL_DMG(""),
    MISS("")

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