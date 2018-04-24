package cc.fish91.gtable.engine

import cc.fish91.gtable.FightSceneFinalData
import cc.fish91.gtable.MonsterData
import cc.fish91.gtable.PersonData

interface IFightEffect {            //NOT FOR EQUIP EX EFFECT, ONLY FOR ROLE/MONSTER
    fun onFight(person: FightSceneFinalData, monster: MonsterData)      //Usually, do fight effects on every Fight
    fun onFightEnd(person: FightSceneFinalData, monster: MonsterData)   //Usually, restore the temp effect
}