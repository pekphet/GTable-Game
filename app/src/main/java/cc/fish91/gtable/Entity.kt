package cc.fish91.gtable


data class FloorMeta(var isOpened: Boolean = false, var isNearMonster: Boolean = false, var status: FloorStatus = FloorStatus.IDLE, var exId: Int = -1)
data class PersonData(var HP: Int, var atk: Int, var def: Int, var exp: Int, var level: Int, var gold: Int, var maxFloor: Int = 1)
data class MonsterData(var HP: Int, var atk: Int, var def: Int, var exp: Int, var gold: Int, var mId: Int)

data class Buff(var ability: BuffAbility, var value: Int)
data class Gift(var giftType: Gifts, var value: Int)

data class BaseMonster(var baseHP: Int, var baseAtk: Int, var baseDef: Int, var name: String, var baseExp: Int, var baseGold: Int, var exEffectId: Int = 0)

data class FloorBuff(var tAtk: Int, var tDef: Int, var keys: Int)