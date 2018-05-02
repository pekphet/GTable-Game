package cc.fish91.gtable.engine

import cc.fish91.gtable.MonsterData

interface IMonsterAdder{
    fun adderOnce(monsterData: MonsterData)
    fun adderEvery(monsterData: MonsterData)
}