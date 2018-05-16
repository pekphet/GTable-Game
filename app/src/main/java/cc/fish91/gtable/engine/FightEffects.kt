package cc.fish91.gtable.engine

import cc.fish91.gtable.FightSceneFinalData
import cc.fish91.gtable.MonsterData
import cc.fish91.gtable.plugin.Math
import cc.fish91.gtable.plugin.ifTrue
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

        override fun getInfo(floor: Int) = "[战斗爆发] 体力值小于30%时爆发潜能，根据攻击力提升攻防，每次攻击根据防御力回复生命值"
    }

    object KNIGHTPSkill : IFightEffect {
        override fun onFight(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            monster.HP -= (((person.def + person.floorAppend.def + person.buff.tDef) * 0.3f) * if (Math.mil_percent(person.critical)) person.criticalDmg / 100f + 1f else 1f).toInt()
        }

        override fun onFightEnd(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            if (person.HP < person.HPLine * 0.5)
                person.buff.tDef += 1
        }

        override fun getInfo(floor: Int) = "[骑士惩戒] 每次攻击造成防御力30%的神圣伤害，体力值小于50%时每次攻击结束提升1点防御BUFF"
    }

    object ROGUEPSkill : IFightEffect {
        var tmp = 0
        var tmpHp = 0
        override fun onFight(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            tmp = monster.def * Math.rand(20, 120)
            tmpHp = person.HP
            monster.def -= tmp
        }

        override fun onFightEnd(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            monster.def += tmp
            if (Math.percent(20)) person.HP = tmpHp

        }

        override fun getInfo(floor: Int) = "[暗影瞬杀]攻击时随机无视目标护甲，对弱小的对手攻击结束时有20%几率复原体力值"
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

        override fun getInfo(floor: Int) = "[幽魂血爆] 每次攻击付出体力值给对方造成伤害，结束时根据敌人状况进行二次攻击或者回复体力"
    }


    /**** Monster Fight Effect ***************************************/
    object DcsPDef : IFightEffect {
        override fun onFight(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            person.def -= floor / 2
        }

        override fun onFightEnd(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            person.def += floor / 2
        }

        override fun getInfo(floor: Int) = "[恐惧] 无视对手${floor / 2}点防御"
    }

    object DscPAtk : IFightEffect {
        override fun onFight(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            person.atk -= floor / 2
        }

        override fun onFightEnd(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            person.atk += floor / 2
        }

        override fun getInfo(floor: Int) = "[坚韧] 无视对手${floor / 2}点攻击"

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
            if ((person.buff.tAtk) * -1 <= person.atk) {
                person.buff.tAtk -= 3
            }
        }

        override fun onFightEnd(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
        }

        override fun getInfo(floor: Int) = "[震慑] 每回合降低攻击力3点, 当前层有效"

    }

    object DscFA1Def : IFightEffect {
        override fun onFight(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            if ((person.buff.tDef) * -1 <= person.def) {
                person.buff.tDef -= 3
            }
        }

        override fun onFightEnd(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
        }

        override fun getInfo(floor: Int) = "[腐蚀] 每回合降低防御力3点，当前层有效"
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