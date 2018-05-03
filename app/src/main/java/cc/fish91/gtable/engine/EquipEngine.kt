package cc.fish91.gtable.engine

import cc.fish91.gtable.*
import cc.fish91.gtable.plugin.AddableMutableMap
import cc.fish91.gtable.plugin.Math
import cc.fish91.gtable.plugin.getRand
import cc.fish91.gtable.plugin.limitAtMost
import cc.fish91.gtable.resource.StaticData

object EquipEngine {
    const val CHANGE_LEVEL_COUNT = 5
    const val CHANGE_RARE_COUNT = 2
    const val EQUIP_LEVEL_LIMIT = 50

    fun create(id: Int, rare: Int, floor: Int, from: Int = 0, limit: Int = 4) = Math.rand(from, (rare + (floor % CHANGE_LEVEL_COUNT) / CHANGE_RARE_COUNT).limitAtMost(limit)).let {
        Equip(
                Math.min(floor / CHANGE_LEVEL_COUNT, EQUIP_LEVEL_LIMIT),
                StaticData.getBaseEquipInfo(id), it).apply {
            for (i in 0..it)
                EquipProperty.values().getRand().let { exProperty.add(it, getExValue(it, StaticData.getBaseEquipInfo(id), floor / CHANGE_LEVEL_COUNT)) }
        }
    }

    fun createByRare(id: Int, rare: Int) = create(id, rare, 0, rare)

    private fun getExValue(ep: EquipProperty, info: EquipInfo, level: Int) = info.run {
        when (ep) {
            EquipProperty.ATK -> this.baseProperty * (level + 1)
            EquipProperty.ATK_PC -> (level + 1)
            EquipProperty.DEF -> this.baseProperty * (level + 1)
            EquipProperty.DEF_PC -> (level + 1)
            EquipProperty.HP -> this.baseProperty * (level + 1) * 10
            EquipProperty.HP_PC -> (level + 1)
            EquipProperty.MISS -> (level + 1)
            EquipProperty.CRITICAL -> (level + 1)
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


    fun calcEquipsAppend(person: PersonData, vararg mEquips: Equip?) = PersonData(0, 0, 0, 0, 0, 0, 0,
            ExPerson(0, 0, 0, 0)).apply {
        calcEquipEX(*mEquips).forEach {
            when (it.key) {
                EquipProperty.ATK -> this.atk += it.value.value
                EquipProperty.ATK_PC -> this.atk += (person.atk * it.value.value / 1000f).toInt()
                EquipProperty.DEF -> this.def += it.value.value
                EquipProperty.DEF_PC -> this.def += (person.def * it.value.value / 1000f).toInt()
                EquipProperty.HP -> this.HP += it.value.value
                EquipProperty.HP_PC -> this.HP += (person.HP * it.value.value / 1000f).toInt()
                EquipProperty.MISS -> this.ex.miss += it.value.value
                EquipProperty.CRITICAL -> this.ex.critical += it.value.value
                EquipProperty.CRITICAL_DMG -> this.ex.critical_dmg += it.value.value
                EquipProperty.HP_RESTORE -> this.ex.restore += it.value.value
            }
        }
    }

    fun getRareColor(rare: Int) = Framework._C.resources.getColor(when (rare) {
        0 -> R.color.text_eq_rare0
        1 -> R.color.text_eq_rare1
        2 -> R.color.text_eq_rare2
        3 -> R.color.text_eq_rare3
        4 -> R.color.text_eq_rare4
        else -> R.color.text_eq_rare4
    })
}