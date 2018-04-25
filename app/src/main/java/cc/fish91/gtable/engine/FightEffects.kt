package cc.fish91.gtable.engine

import cc.fish91.gtable.FightSceneFinalData
import cc.fish91.gtable.MonsterData

object FightEffects {
    class decreaseDef(val value: Int) : IFightEffect {
        override fun getInfo() = ""

        override fun onFight(person: FightSceneFinalData, monster: MonsterData) {
            monster.def -= value
        }

        override fun onFightEnd(person: FightSceneFinalData, monster: MonsterData) {
            monster.def -= value
        }
    }

    class decreaseDefPc(val valuePc: Int) : IFightEffect {
        var decreased = 0
        override fun onFight(person: FightSceneFinalData, monster: MonsterData) {
            decreased = monster.def * valuePc / 100
            monster.def -= decreased
        }

        override fun onFightEnd(person: FightSceneFinalData, monster: MonsterData) {
            monster.def += decreased
        }

        override fun getInfo() = ""

    }


}