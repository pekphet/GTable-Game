package cc.fish91.gtable


data class FloorMeta(var isOpened: Boolean = false, var isNearMonster: Boolean = false, var status: FloorStatus = FloorStatus.IDLE, var exId: Int = -1)
data class PersonData(var HP: Int, var atk: Int, var def: Int, var exp: Int, var level: Int, var gold: Int, var maxFloor: Int = 1, var ex: ExPerson = ExPerson(), var roleType: RoleType = RoleType.BEGINNER)
data class ExPerson(var miss: Int = 100, var critical: Int = 800, var critical_dmg: Int = 150, var restore: Int = 0, val ex: MutableList<Pair<ExEffect, Int>> = mutableListOf())

data class MonsterData(var HP: Int, var atk: Int, var def: Int, var exp: Int, var gold: Int, var mId: Int)

data class Buff(var ability: BuffAbility, var value: Int)
data class Gift(var giftType: Gifts, var value: Int)

data class BaseMonster(var baseHP: Int, var baseAtk: Int, var baseDef: Int, var name: String, var baseExp: Int, var baseGold: Int, var exEffectId: Int = 0)

data class FloorBuff(var tAtk: Int, var tDef: Int, var keys: Int)

data class PersonBought(var atkLv: Int, var defLv: Int, var hpLv: Int)

data class Equip(var level: Int, val info: EquipInfo, val exProperty: MutableList<Pair<EquipProperty, Int>> = mutableListOf())

data class EquipInfo(var name: String, var position: EquipPosition, var baseProperty: Int, var iconId: Int, var extra: Pair<ExEffect, Int>?)