package cc.fish91.gtable.engine

import cc.fish91.gtable.FightSceneFinalData
import cc.fish91.gtable.MonsterData
import cc.fish91.gtable.plugin.Math
import cc.fish91.gtable.plugin.limitAtLeast
import cc.fish91.gtable.plugin.limitAtMost

object FightEffects {


    /****Person Fight Effect*******************/
    object DcsMDef : IFightEffect {
        override fun getInfo(floor: Int) = "[减防] 降低目标${floor / 2}点防御"

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

        override fun getInfo(floor: Int) = "[暗杀] 无视怪物20%防御"

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
            if (person.HP * 1f / person.HPLine <= 0.3) {
                adder = (person.atk * 0.3).toInt()
                person.atk += adder
                person.def += adder
            }
        }

        override fun onFightEnd(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            person.atk -= adder
            person.def -= adder
            if (adder > 0) person.HP += (person.def * 0.02).toInt()
        }

        override fun getInfo(floor: Int) = "[格斗家爆发] 体力值小于30%时爆发潜能，根据攻击力提升攻防，每次攻击根据防御力回复生命值"
    }

    object KNIGHTPSkill : IFightEffect {
        override fun onFight(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            monster.HP -= (person.def * 0.3).toInt() * if (Math.mil_percent(person.critical)) person.criticalDmg / 100 else 1
        }

        override fun onFightEnd(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            if (person.HP < person.HPLine * 0.5)
                person.buff.tDef += 1
        }

        override fun getInfo(floor: Int) = "[骑士精神] 每次攻击造成防御力30%的神圣伤害，体力值小于50%时每次攻击结束提升1点防御BUFF"
    }

    object ROGUEPSkill : IFightEffect {
        override fun onFight(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            if (monster.HP > person.HP)
                monster.HP -= (monster.HP * 0.1).toInt()
        }

        override fun onFightEnd(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            if (monster.HP < person.HP && Math.percent(2))
                monster.HP = -1
        }

        override fun getInfo(floor: Int) = "[暗杀] 攻击强大对手时削减对方体力值，对弱小的对手攻击结束时有几率秒杀"
    }

    object NECPSkill : IFightEffect {
        override fun onFight(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            person.HP -= (person.HPLine * 0.05).toInt()
            if (person.HP < monster.HP)
                monster.HP -= person.HPLine - person.HP

        }

        override fun onFightEnd(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            if (person.HP < monster.HP) {
                person.HP = (person.HP + (person.HPLine * 0.08).toInt()).limitAtMost(person.HPLine)
            } else {
                monster.HP -= person.HPLine - person.HP
            }
        }

        override fun getInfo(floor: Int) = "[血爆] 每次攻击付出体力值给对方造成伤害，结束时根据敌人状况进行二次攻击或者回复体力"
    }


    /**** Monster Fight Effect ***************************************/
    object DcsPDef : IFightEffect {
        override fun onFight(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            person.def -= floor / 2
        }

        override fun onFightEnd(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            person.def += floor / 2
        }

        override fun getInfo(floor: Int) = "[恐惧] 降低对手${floor / 2}点防御"
    }

    object DscPAtk : IFightEffect {
        override fun onFight(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            person.atk -= floor / 2
        }

        override fun onFightEnd(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            person.atk += floor / 2
        }

        override fun getInfo(floor: Int) = "[坚韧] 降低对手${floor / 2}点攻击"

    }

    object CutP10HP : IFightEffect {
        override fun onFight(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            person.HP -= person.HP / 10
        }

        override fun onFightEnd(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
        }

        override fun getInfo(floor: Int) = "[弱敌] 每回合直接减少对方10%HP"

    }

    object ClearPArmor : IFightEffect {
        override fun onFight(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            person.floorAppend.HP = 0
        }

        override fun onFightEnd(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
        }

        override fun getInfo(floor: Int) = "[击破] 攻击时去掉对手护盾"

    }

    object DscFA5Atk : IFightEffect {
        override fun onFight(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            person.floorAppend.atk -= 5
        }

        override fun onFightEnd(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
        }

        override fun getInfo(floor: Int) = "[震慑] 每回合降低攻击力5点，每10层清空"

    }

    object DscFA1Def : IFightEffect {
        override fun onFight(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            person.floorAppend.def -= 1
        }

        override fun onFightEnd(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
        }

        override fun getInfo(floor: Int) = "[腐蚀] 每回合降低防御力1点，每10层清空"
    }

    object CopyPAtk : IFightEffect {

        override fun onFight(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            monster.atk = person.atk
        }

        override fun onFightEnd(person: FightSceneFinalData, monster: MonsterData, floor: Int) {

        }

        override fun getInfo(floor: Int) = "[镜像攻击] 攻击力变为对方攻击力"
    }

    object CopyPDef : IFightEffect {

        override fun onFight(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            monster.def = person.def
        }

        override fun onFightEnd(person: FightSceneFinalData, monster: MonsterData, floor: Int) {

        }

        override fun getInfo(floor: Int) = "[镜像防御] 防御力变为对方防御力"
    }

    object RebP10Atk : IFightEffect {
        override fun onFight(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            person.HP -= (person.atk * 10 / 100).limitAtLeast(1)
        }

        override fun onFightEnd(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
        }

        override fun getInfo(floor: Int) = "[反弹] 反弹对方攻击力10%的伤害，无视防御"

    }


}