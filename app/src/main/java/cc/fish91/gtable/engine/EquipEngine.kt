package cc.fish91.gtable.engine

import cc.fish91.gtable.Equip
import cc.fish91.gtable.EquipProperty
import cc.fish91.gtable.PersonData
import cc.fish91.gtable.plugin.AddableMutableMap
import cc.fish91.gtable.plugin.Math
import cc.fish91.gtable.plugin.getRand
import cc.fish91.gtable.resource.StaticData

object EquipEngine {
    const val CHANGE_LEVEL_COUNT = 5
    const val CHANGE_RARE_COUNT = 2
    fun create(id: Int, rare: Int, floor: Int) = Math.rand(rare + floor % CHANGE_LEVEL_COUNT / CHANGE_RARE_COUNT).let {
        Equip(
                floor / CHANGE_LEVEL_COUNT,
                StaticData.getBaseEquipInfo(id), it).apply {
            for (i in 0..it)
                EquipProperty.values().getRand().let { exProperty.add(it, getExValue(it, id, floor)) }
        }
    }

    private fun getExValue(ep: EquipProperty, id: Int, level: Int) = StaticData.getBaseEquipInfo(id).run {
        when (ep) {
            EquipProperty.ATK -> this.baseProperty * level
            EquipProperty.ATK_PC -> level
            EquipProperty.DEF -> this.baseProperty * level
            EquipProperty.DEF_PC -> level
            EquipProperty.HP -> this.baseProperty * level * 10
            EquipProperty.HP_PC -> level
            EquipProperty.MISS -> level * 5
            EquipProperty.CRITICAL -> level * 5
            EquipProperty.CRITICAL_DMG -> level * 2
            EquipProperty.HP_RESTORE -> level
        }
    }


    private fun calcEquipEX(vararg mEquips: Equip) = AddableMutableMap<EquipProperty>().apply {
        mEquips.map {
            it.exProperty.forEach {add(it.key, it.value.value)}
        }
    }


    fun calcEquipsAppend(person: PersonData, vararg mEquips: Equip) = PersonData(0, 0, 0, 0, 0, 0, 0).apply {
        calcEquipEX(*mEquips).forEach {
            when (it.key) {
                EquipProperty.ATK -> this.atk += it.value.value
                EquipProperty.ATK_PC -> this.atk += (person.atk * it.value.value / 100f).toInt()
                EquipProperty.DEF -> this.def += it.value.value
                EquipProperty.DEF_PC -> this.def += (person.def * it.value.value / 100f).toInt()
                EquipProperty.HP -> this.HP += 10 * it.value.value
                EquipProperty.HP_PC -> this.HP += (person.HP * it.value.value / 100f).toInt()
                EquipProperty.MISS -> this.ex.miss += it.value.value
                EquipProperty.CRITICAL -> this.ex.critical += it.value.value
                EquipProperty.CRITICAL_DMG -> this.ex.critical_dmg += it.value.value
                EquipProperty.HP_RESTORE -> this.ex.restore += it.value.value
            }
        }
    }
}