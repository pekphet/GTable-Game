package cc.fish91.gtable.engine

import cc.fish91.gtable.*
import cc.fish91.gtable.plugin.AddableMutableMap
import cc.fish91.gtable.plugin.Math
import cc.fish91.gtable.plugin.getRand
import cc.fish91.gtable.resource.StaticData

object EquipEngine {
    const val CHANGE_LEVEL_COUNT = 5
    const val CHANGE_RARE_COUNT = 2
    fun create(id: Int, rare: Int, floor: Int) = Math.rand(rare + (floor % CHANGE_LEVEL_COUNT) / CHANGE_RARE_COUNT).let {
        Equip(
                floor / CHANGE_LEVEL_COUNT,
                StaticData.getBaseEquipInfo(id), it).apply {
            for (i in 0..it)
                EquipProperty.values().getRand().let { exProperty.add(it, getExValue(it, StaticData.getBaseEquipInfo(id), floor / CHANGE_LEVEL_COUNT)) }
        }
    }

    private fun getExValue(ep: EquipProperty, info: EquipInfo, level: Int) = info.run {
        when (ep) {
            EquipProperty.ATK -> this.baseProperty * (level + 1)
            EquipProperty.ATK_PC -> (level + 1)
            EquipProperty.DEF -> this.baseProperty * (level + 1)
            EquipProperty.DEF_PC -> level
            EquipProperty.HP -> this.baseProperty * (level + 1) * 10
            EquipProperty.HP_PC -> (level + 1)
            EquipProperty.MISS -> (level + 1) * 5
            EquipProperty.CRITICAL -> (level + 1) * 5
            EquipProperty.CRITICAL_DMG -> (level + 1) * 2
            EquipProperty.HP_RESTORE -> (level + 1)
        }
    }

    fun getMainValueNullable(ep: EquipProperty, info: EquipInfo?, level: Int) = info?.let { getExValue(ep, info, level) }

    fun getMainValue(equip: Equip) = when (equip.info.position) {
        EquipPosition.WEAPON -> EquipProperty.ATK
        EquipPosition.ARMOR -> EquipProperty.DEF
        EquipPosition.RING -> EquipProperty.HP
    }.run { Pair(this, getExValue(this, equip.info, equip.level)) }


    private fun calcEquipEX(vararg mEquips: Equip?) = AddableMutableMap<EquipProperty>().apply {
        mEquips.map {
            if (it != null) {
                getMainValue(it).let { add(it.first, it.second) }           //main property
                it.exProperty.forEach { add(it.key, it.value.value) }       //ex properties
            }
        }
    }


    fun calcEquipsAppend(person: PersonData, vararg mEquips: Equip?) = PersonData(0, 0, 0, 0, 0, 0, 0).apply {
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