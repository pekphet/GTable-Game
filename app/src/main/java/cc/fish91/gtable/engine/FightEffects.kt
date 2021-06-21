package cc.fish91.gtable.engine

import android.content.Context
import cc.fish91.gtable.FightSceneFinalData
import cc.fish91.gtable.MonsterData
import cc.fish91.gtable.R
import cc.fish91.gtable.plugin.Math
import cc.fish91.gtable.plugin.ifTrue
import cc.fish91.gtable.plugin.limitAtLeast
import cc.fish91.gtable.plugin.limitAtMost
import cc.fish91.gtable.resString

object FightEffects {


    /****Person Fight Effect*******************/
    object DcsMDef : IFightEffect {
        override fun getInfo(floor: Int) = resString(R.string.effect_dec_def, floor / 2)

        override fun onFight(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            monster.def -= floor / 2
        }

        override fun onFightEnd(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            monster.def += floor / 2
        }
    }

    object DcsMDef20Pc : IFightEffect {
        var decreased = 0
        override fun onFight(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            decreased = monster.def * 20 / 100
            monster.def -= decreased
        }

        override fun onFightEnd(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            monster.def += decreased
        }

        override fun getInfo(floor: Int) = resString(R.string.effect_dec_def_p20)

    }

    /****Role Type P-Skills*********/
    object NANSkill : IFightEffect {
        override fun onFight(person: FightSceneFinalData, monster: MonsterData, floor: Int) {

        }

        override fun onFightEnd(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
        }

        override fun getInfo(floor: Int): String = "无技能"
    }

    object FighterPSkill : IFightEffect {
        var adder = 0
        override fun onFight(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            adder = 0
            if (person.HP * 1f / person.HPLine <= 0.3) {
                adder = (person.atk * 0.3).toInt()
                person.floorAppend.atk += adder
                person.floorAppend.def += adder
            }
        }

        override fun onFightEnd(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            person.floorAppend.atk -= adder
            person.floorAppend.def -= adder
            if (adder > 0) person.HP += (person.def * 0.05).toInt().limitAtMost(person.HPLine)
        }

        override fun getInfo(floor: Int) = resString(R.string.tr_fi_pssk_fighter_crash)
    }

    object KNIGHTPSkill : IFightEffect {
        override fun onFight(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            monster.HP -= (((person.def + person.floorAppend.def + person.buff.tDef) * 0.3f) * if (Math.mil_percent(person.critical)) person.criticalDmg / 100f + 1f else 1f).toInt()
        }

        override fun onFightEnd(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            if (person.HP < person.HPLine * 0.5)
                person.buff.tDef += 1
        }

        override fun getInfo(floor: Int) = resString(R.string.tr_kn_pssk_holy_buff)
    }

    object ROGUEPSkill : IFightEffect {
        var tmp = 0
        var tmpHp = 0
        override fun onFight(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            tmp = monster.def * Math.rand(20, 120) / 100
            tmpHp = person.HP
            monster.def -= tmp
        }

        override fun onFightEnd(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            monster.def += tmp
            if (Math.percent(20)) person.HP = tmpHp

        }

        override fun getInfo(floor: Int) = resString(R.string.tr_ro_pssk_shadow_kill)
    }

    object NECPSkill : IFightEffect {
        override fun onFight(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            person.HP -= (person.HP * 0.05).toInt()
            if (person.HP < monster.HP)
                monster.HP -= ((person.HPLine - person.HP) * if (Math.mil_percent(person.critical)) person.criticalDmg / 100f + 1f else 1f).toInt()
        }

        override fun onFightEnd(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            if (person.HP < monster.HP) {
                person.HP = (person.HP + (person.HPLine * 0.05).toInt()).limitAtMost(person.HPLine)
            } else if (monster.HP < 0) {
                person.floorAppend.HP += 1
            } else {
                monster.HP -= person.HPLine - person.HP
            }
        }

        override fun getInfo(floor: Int) = resString(R.string.tr_ne_pssk_blood_magic)
    }


    /**** Monster Fight Effect ***************************************/
    object DcsPDef : IFightEffect {
        override fun onFight(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            person.def -= floor / 2
        }

        override fun onFightEnd(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            person.def += floor / 2
        }

        override fun getInfo(floor: Int) = resString(R.string.m_effect_dec_def, floor / 2)
    }

    object DscPAtk : IFightEffect {
        override fun onFight(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            person.atk -= floor / 2
        }

        override fun onFightEnd(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            person.atk += floor / 2
        }

        override fun getInfo(floor: Int) = resString(R.string.m_effect_dec_atk, floor / 2)

    }

    object CutP10HP : IFightEffect {
        override fun onFight(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            person.HP -= person.HP / 10
        }

        override fun onFightEnd(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
        }

        override fun getInfo(floor: Int) = resString(R.string.m_effect_dec_hpp)

    }

    object ClearPArmor : IFightEffect {
        override fun onFight(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            person.floorAppend.HP = 0
        }

        override fun onFightEnd(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
        }

        override fun getInfo(floor: Int) = resString(R.string.m_effect_clear_shield)

    }

    object DscFA5Atk : IFightEffect {
        override fun onFight(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            if ((person.buff.tAtk) * -1 <= person.atk) {
                person.buff.tAtk -= 3
            }
        }

        override fun onFightEnd(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
        }

        override fun getInfo(floor: Int) = resString(R.string.m_effect_atkdn_evr)

    }

    object DscFA1Def : IFightEffect {
        override fun onFight(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            if ((person.buff.tDef) * -1 <= person.def) {
                person.buff.tDef -= 3
            }
        }

        override fun onFightEnd(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
        }

        override fun getInfo(floor: Int) = resString(R.string.m_effect_defdn_evr)
    }

    object CopyPAtk : IFightEffect {

        override fun onFight(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            monster.atk = person.atk
        }

        override fun onFightEnd(person: FightSceneFinalData, monster: MonsterData, floor: Int) {

        }

        override fun getInfo(floor: Int) = resString(R.string.m_effect_image_atk)
    }

    object CopyPDef : IFightEffect {

        override fun onFight(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            monster.def = person.def
        }

        override fun onFightEnd(person: FightSceneFinalData, monster: MonsterData, floor: Int) {

        }

        override fun getInfo(floor: Int) = resString(R.string.m_effect_image_def)
    }

    object RebP10Atk : IFightEffect {
        override fun onFight(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            person.HP -= (person.atk * 10 / 100).limitAtLeast(1)
        }

        override fun onFightEnd(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
        }

        override fun getInfo(floor: Int) = resString(R.string.m_effect_reflect_p10)

    }


}