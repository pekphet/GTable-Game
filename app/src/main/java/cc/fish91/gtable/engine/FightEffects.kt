package cc.fish91.gtable.engine

import cc.fish91.gtable.FightSceneFinalData
import cc.fish91.gtable.MonsterData

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

    object DscFA1Atk : IFightEffect {
        override fun onFight(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            person.floorAppend.atk -= 1
        }

        override fun onFightEnd(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
        }

        override fun getInfo(floor: Int) = "[震慑] 每回合降低攻击力1点，每10层清空"

    }

    object DscFA1Def : IFightEffect {
        override fun onFight(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
            person.floorAppend.def -= 1
        }

        override fun onFightEnd(person: FightSceneFinalData, monster: MonsterData, floor: Int) {
        }

        override fun getInfo(floor: Int) = "[腐蚀] 每回合降低防御力1点，每10层清空"
    }


}