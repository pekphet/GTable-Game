package cc.fish91.gtable.localdata

import android.content.Context
import cc.fish91.gtable.*
import cc.fish91.gtable.engine.TaskEngine
import cc.fish91.gtable.localdata.SaveDataManager.PERSON_FILES
import cc.fish91.gtable.resource.StaticData
import com.google.gson.reflect.TypeToken

object PersonRecord {
    private const val PERSON_BASE_KEY = "base"
    private const val PERSON_BASE_BOUGHT = "base_bought"

    private const val PERSON_TASK = "task"

    fun SP_HANDLE() = Framework._C.getSharedPreferences(SaveDataManager.PERSON_FILES[SaveDataManager.checkedPosition], Context.MODE_PRIVATE)

    private var mHpLine = 0

    fun getPersonData(): PersonData = SP_HANDLE().getString(PERSON_BASE_KEY, "").run {
        if (this.isNullOrBlank())
            StaticData.getPersonInit("", RoleType.BEGINNER)
        else
            Framework._G.fromJson(this, PersonData::class.java)
    }

    fun getPersonDataNullable(slot: Int): PersonData? = Framework._C.getSharedPreferences(SaveDataManager.PERSON_FILES[slot], Context.MODE_PRIVATE).getString(PERSON_BASE_KEY, "").run {
        if (this.isNullOrBlank())
            null
        else
            Framework._G.fromJson(this, PersonData::class.java)
    }

    fun getBaseHPLine(): Int {
        if (mHpLine <= 0)
            mHpLine = getPersonData().HP
        return mHpLine
    }

    @Synchronized
    fun storePersonData(p: PersonData) = SP_HANDLE().edit()
            .putString(PERSON_BASE_KEY, Framework._G.toJson(p))
            .apply()

    fun personDataLevelUP() = getPersonData().let {
        StaticData.getLvGrow().run {
            it.level++
            it.exp = 0
            it.HP += this.HP
            it.atk += this.atk
            it.def += this.def
            mHpLine += this.HP
            storePersonData(it)
        }
    }

    /****Person Bought*********/
    @Synchronized
    fun storePersonBoughtData(b: PersonBought) = SP_HANDLE().edit()
            .putString(PERSON_BASE_BOUGHT, Framework._G.toJson(b))
            .apply()

    fun getPersonBought(): PersonBought = SP_HANDLE().getString(PERSON_BASE_BOUGHT, "").run {
        if (this.isNullOrBlank())
            PersonBought(0, 0, 0)
        else
            Framework._G.fromJson(this, PersonBought::class.java)
    }

    @Synchronized
    fun abilityBought(pb: PersonBought, p: PersonData, statusType: Int) {
        when (statusType) {
            1 -> {          //atk
                p.gold -= StaticData.getUPFee(pb.atkLv)
                pb.atkLv++
                storePersonData(p)
                storePersonBoughtData(pb)
            }
            2 -> {          //def
                p.gold -= StaticData.getUPFee(pb.defLv)
                pb.defLv++
                storePersonData(p)
                storePersonBoughtData(pb)
            }
            3 -> {          //hp
                p.gold -= StaticData.getUPFee(pb.hpLv)
                pb.hpLv++
                storePersonData(p)
                storePersonBoughtData(pb)
            }
        }
    }

    /****Person Task*********/

    @Synchronized
    fun storeTasks(tasks: List<TaskEntity>) = SP_HANDLE().edit()
            .putString(PERSON_TASK, Framework._G.toJson(tasks))
            .apply()

    @Synchronized
    fun getTasks() = SP_HANDLE().getString(PERSON_TASK, "").run {
        if (this.isNullOrBlank())
            listOf<TaskEntity>()
        else
            Framework._G.fromJson(this, object : TypeToken<List<TaskEntity>>() {}.type)
    }

    @Synchronized
    fun storeTask(task: TaskEntity): Boolean {
        getTasks().run {
            if (size >= TaskEngine.TASK_COUNT)
                return false
            else {
                storeTasks(this.toMutableList().apply { add(task) })
                return true
            }
        }
    }


    fun clearData() {
        SP_HANDLE().edit()
                .putString(PERSON_BASE_BOUGHT, "")
                .apply()
        SP_HANDLE().edit()
                .putString(PERSON_TASK, "")
                .apply()
        SP_HANDLE().edit()
                .putString(PERSON_BASE_KEY, "")
                .apply()
    }

    /****ROLE TYPE!*****************/
    fun getEnabledRoleType1() = arrayOf(RoleType.FIGHTER, RoleType.ROGUE, RoleType.KNIGHT, RoleType.NEC)

    fun changeRoleType1(name: String, type: RoleType, gold: Int) {
        storePersonData(StaticData.getPersonInit(name, type, gold, 90))
    }

}