package cc.fish91.gtable.engine

import cc.fish91.gtable.*
import cc.fish91.gtable.plugin.*
import cc.fish91.gtable.resource.StaticData

object EquipEngine {
    const val CHANGE_LEVEL_COUNT = 5
    const val CHANGE_RARE_COUNT = 2
    const val EQUIP_LEVEL_LIMIT = 50

    fun create(id: Int, rare: Int, floor: Int, from: Int = 0, limit: Int = StaticData.EQ_RARE_EPIC) = Math.rand(from, (rare + (floor % CHANGE_LEVEL_COUNT) / CHANGE_RARE_COUNT).limitAtMost(limit)).let {
        StaticData.getBaseEquipInfo(id).let { info ->
            Equip(
                    Math.min(floor / CHANGE_LEVEL_COUNT, EQUIP_LEVEL_LIMIT + info.baseLevel),
                    info, it).apply {
                for (i in 0..it)
                    EquipProperty.values().getRand().let { exProperty.add(it, getExValue(it, info, (floor / CHANGE_LEVEL_COUNT).limitAtMost(EQUIP_LEVEL_LIMIT + info.baseLevel + 1))) }
            }
        }
    }

    fun createByRare(id: Int, level: Int, rare: Int) = create(id, rare, level * 5 + 4, rare, rare)

    private fun getExValue(ep: EquipProperty, info: EquipInfo, level: Int) = info.run {
        when (ep) {
            EquipProperty.ATK -> this.baseProperty * (level + 1)
            EquipProperty.ATK_PC -> (level + 1) * 7
            EquipProperty.DEF -> this.baseProperty * (level + 1)
            EquipProperty.DEF_PC -> (level + 1) * 7
            EquipProperty.HP -> this.baseProperty * (level + 1) * 10
            EquipProperty.HP_PC -> (level + 1) * 7
            EquipProperty.MISS -> (level + 1) * 2
            EquipProperty.CRITICAL -> (level + 1) * 2
            EquipProperty.CRITICAL_DMG -> (level + 1) * 2
            EquipProperty.HP_RESTORE -> ((level + 1) / 2).coerceAtLeast(1)
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
        addSuit(this, checkSuit(*mEquips))
    }

    fun checkSuit(vararg es: Equip?): Int {
        if (es.size == 3
                && es.filter { it?.rare == StaticData.EQ_RARE_SUIT }.size == 3
                && ((es[0]!!.info.id and 0xff) == (es[1]!!.info.id and 0xff)) && ((es[0]!!.info.id and 0xff) == (es[2]!!.info.id and 0xff))) {
            return es[0]!!.info.id and 0xff
        }
        return -1
    }

    fun hasSuit(vararg es: Equip?) = checkSuit(*es) != -1


    private fun addSuit(p: PersonData, suitId: Int) {
        when (suitId) {
            1 -> p.run {
                p.atk += (p.atk * 0.5).toInt()
                p.ex.critical += 300
                p.ex.critical_dmg += 200
                p.ex.miss += 200
            }
            2 -> p.run {
                p.def += (p.def * 0.5).toInt()
                p.atk -= (p.atk * 0.5).toInt()
                p.ex.miss += 200
                p.HP += (p.HP * 1.0).toInt()
            }
            3 -> p.run {
                p.atk += (p.atk * 0.7).toInt()
                p.def += (p.def * 0.7).toInt()
                p.ex.restore += 10
                p.ex.critical += 200
            }
            4 -> p.run {
                p.HP += (p.HP * 0.5).toInt()
                p.ex.restore -= 30
                p.ex.miss += 300
                p.ex.critical += 200
                p.ex.critical_dmg += 150
            }
        }
    }

    fun getRareColor(rare: Int) = Framework._C.resources.getColor(when (rare) {
        0 -> R.color.text_eq_rare0
        1 -> R.color.text_eq_rare1
        2 -> R.color.text_eq_rare2
        3 -> R.color.text_eq_rare3
        4 -> R.color.text_eq_rare4
        5 -> R.color.text_eq_rare5
        6 -> R.color.text_eq_rare6
        else -> R.color.text_eq_rare4
    })

    fun getSuitById(id: Int) = EquipSuit.values().filter { it.id == id }.run { if (size > 0) this[0] else null }

    fun getEquipEffectInfo(effect: Pair<ExEffect, Int>) = when (effect) {
        ExEffect.CRITICAL_UP, ExEffect.MISS_UP -> String.format(effect.first.info, effect.second.toMillicentKeep1())
        else -> String.format(effect.first.info, effect.second)
    }
}