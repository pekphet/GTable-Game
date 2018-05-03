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