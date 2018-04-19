package cc.fish91.gtable.engine

import cc.fish91.gtable.FightSceneFinalData
import cc.fish91.gtable.MonsterData
import cc.fish91.gtable.PersonData

interface IFightEffect {            //NOT FOR EQUIP EX EFFECT, ONLY FOR ROLE/MONSTER
    fun onFight(person: FightSceneFinalData, monster: MonsterData)
    fun onFightEnd(person: FightSceneFinalData, monster: MonsterData)
}