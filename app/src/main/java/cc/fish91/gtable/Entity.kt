package cc.fish91.gtable

import cc.fish91.gtable.plugin.AddableMutableMap


data class FloorMeta(var isOpened: Boolean = false, var isNearMonster: Boolean = false, var status: FloorStatus = FloorStatus.IDLE, var exId: Int = -1)
data class PersonData(var HP: Int, var atk: Int, var def: Int, var exp: Int, var level: Int, var gold: Int, var maxFloor: Int = 1, var ex: ExPerson = ExPerson(), var roleType: RoleType = RoleType.BEGINNER)
data class ExPerson(var miss: Int = 100, var critical: Int = 100, var critical_dmg: Int = 150, var restore: Int = 0, val ex: MutableList<Pair<ExEffect, Int>> = mutableListOf())

data class MonsterData(var HP: Int, var atk: Int, var def: Int, var exp: Int, var gold: Int, var mId: Int)

data class Buff(var ability: BuffAbility, var value: Int)
data class Gift(var giftType: Gifts, var value: Int)

data class BaseMonster(var baseHP: Int, var baseAtk: Int, var baseDef: Int, var name: String, var baseExp: Int, var baseGold: Int, var iconId: Int, var drop: Pair<Int, Int>, var exEffectId: Int = 0)

data class FloorBuff(var tAtk: Int = 0, var tDef: Int = 0, var tArmor: Int = 0, var keys: Int = 0)

data class PersonBought(var atkLv: Int, var defLv: Int, var hpLv: Int)

data class Equip(var level: Int, val info: EquipInfo, var rare: Int, val exProperty: AddableMutableMap<EquipProperty> = AddableMutableMap())

data class EquipInfo(var name: String, var position: EquipPosition, var baseProperty: Int, var iconId: Int, var extra: Pair<ExEffect, Int>?)

data class FightSceneFinalData(var HP: Int = 0, var HPLine: Int = 0, var atk: Int = 0, var def: Int = 0, var critical: Int = 0, var criticalDmg: Int = 0, var miss: Int = 0, var restore: Int = 0, val buff: FloorBuff = FloorBuff(), val floorAppend: FloorAppend = FloorAppend())
data class FloorAppend(var HP: Int = 0, var atk: Int = 0, var def: Int = 0)