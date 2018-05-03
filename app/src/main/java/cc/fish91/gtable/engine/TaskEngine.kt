package cc.fish91.gtable.engine

import cc.fish91.gtable.TaskAward
import cc.fish91.gtable.TaskAwardType
import cc.fish91.gtable.TaskEntity
import cc.fish91.gtable.TaskType
import cc.fish91.gtable.engine.EquipEngine.EQUIP_LEVEL_LIMIT
import cc.fish91.gtable.plugin.*
import cc.fish91.gtable.resource.StaticData

object TaskEngine {
    const val K_MONSTER_TASK_CHANCE_PC = 10
    const val TASK_COUNT = 3

    fun create(pLevel: Int, startFloor: Int): TaskEntity {
        val type = getRandType()

        return when (type) {
            TaskType.KILL_MONSTER -> {
                TaskEntity(startFloor, getMonsterTaskNeed(pLevel), 0, TaskType.KILL_MONSTER,
                        StaticData.getMonsterPool(startFloor / 30 + 1).getRand(),
                        Math.percent(K_MONSTER_TASK_CHANCE_PC)).apply {
                    if (this.isK)
                        this.needValue = (this.needValue / 8).limitAtLeast(3)
                    award = createAward(this)
                }
            }

            TaskType.UP_FLOORS -> {
                TaskEntity(startFloor, getFloorTaskNeed() + startFloor, startFloor, TaskType.UP_FLOORS, 0, false).apply {
                    award = createAward(this)
                }
            }
        }
    }

    fun checkMonster(task: List<TaskEntity>, monsterId: Int, isK: Boolean) = task.map { check(it, -1, monsterId, isK) }
    fun checkFloor(task: List<TaskEntity>, floor: Int) = task.map { check(it, floor, -1, false) }

    private fun check(task: TaskEntity, floor: Int, monsterId: Int, isK: Boolean) {
        when (task.type) {
            TaskType.KILL_MONSTER -> if (monsterId == task.monsterId && isK == task.isK) task.currentValue++
            TaskType.UP_FLOORS -> if (floor > task.currentValue) task.currentValue++
        }
    }

    private fun createAward(task: TaskEntity): TaskAward {
        val type = getRandAwardType(task.type)
        return when (task.type) {
            TaskType.KILL_MONSTER -> when (type) {
                TaskAwardType.Equip -> TaskAward(StaticData.getBaseMonster(task.monsterId).drop.first,
                        Math.wave((task.startFloor / EquipEngine.CHANGE_LEVEL_COUNT + 2).limitAtMost(EQUIP_LEVEL_LIMIT), 2),
                        if (task.isK) 3 else 2, TaskAwardType.Equip)
                TaskAwardType.Gold -> TaskAward(task.needValue * 20, 0, 0, TaskAwardType.Gold)
                TaskAwardType.Exp -> TaskAward(task.needValue * 20, 0, 0, TaskAwardType.Exp)
            }
            TaskType.UP_FLOORS -> when (type) {
                TaskAwardType.Equip -> TaskAward(getEquipIdByFloor(task.startFloor),
                        Math.wave((task.startFloor / EquipEngine.CHANGE_LEVEL_COUNT + 2).limitAtMost(EQUIP_LEVEL_LIMIT), 2),
                        2, TaskAwardType.Equip)
                TaskAwardType.Gold -> TaskAward(task.needValue * 50, 0, 0, TaskAwardType.Gold)
                TaskAwardType.Exp -> TaskAward(task.needValue * 50, 0, 0, TaskAwardType.Exp)
            }
        }

    }

    private fun getRandType() = Math.chance(
            Pair(TaskType.KILL_MONSTER, 100),
            Pair(TaskType.UP_FLOORS, 30))

    private fun getRandAwardType(taskType: TaskType) =
            when (taskType) {
                TaskType.KILL_MONSTER -> Math.chance(
                        Pair(TaskAwardType.Equip, 100),
                        Pair(TaskAwardType.Gold, 40),
                        Pair(TaskAwardType.Exp, 20))
                TaskType.UP_FLOORS -> Math.chance(
                        Pair(TaskAwardType.Equip, 20),
                        Pair(TaskAwardType.Gold, 40),
                        Pair(TaskAwardType.Exp, 100))
            }


    private fun getMonsterTaskNeed(pLevel: Int) = Math.wavePc((5 + pLevel * 2).limitAtMost(60), 20)
    private fun getFloorTaskNeed() = Math.wave(7, 3)

    private fun getEquipIdByFloor(floor: Int): Int {
        if (floor <= 30) {
            return listOf(0x1, 0x1001, 0x2001).getRand()
        } else {
            return StaticData.getAllEquips().toList().getRand()
        }
    }

}