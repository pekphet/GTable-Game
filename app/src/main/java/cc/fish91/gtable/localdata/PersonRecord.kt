package cc.fish91.gtable.localdata

import android.content.Context
import cc.fish91.gtable.Framework
import cc.fish91.gtable.PersonBought
import cc.fish91.gtable.PersonData
import cc.fish91.gtable.TaskEntity
import cc.fish91.gtable.engine.TaskEngine
import cc.fish91.gtable.resource.StaticData
import com.google.gson.reflect.TypeToken

object PersonRecord {
    private const val PERSON_FILE = "person"
    private const val PERSON_BASE_KEY = "base"
    private const val PERSON_BASE_BOUGHT = "base_bought"

    private const val PERSON_TASK = "task"

    private val SP_HANDLE by lazy { Framework._C.getSharedPreferences(PERSON_FILE, Context.MODE_PRIVATE) }

    private var mHpLine = 0

    fun getPersonData(): PersonData = SP_HANDLE.getString(PERSON_BASE_KEY, "").run {
        if (this.isBlank())
            PersonData(100, 2, 2, 0, 1, 0)
        else
            Framework._G.fromJson(this, PersonData::class.java)
    }

    fun getBaseHPLine(): Int {
        if (mHpLine <= 0)
            mHpLine = getPersonData().HP
        return mHpLine
    }

    @Synchronized
    fun storePersonData(p: PersonData) = SP_HANDLE.edit()
            .putString(PERSON_BASE_KEY, Framework._G.toJson(p))
            .apply()

    fun personDataLevelUP() = getPersonData().let {
        StaticData.getLvGrow().run {
            it.level++
            it.exp = 0
            it.HP += this.HP
            it.atk += this.atk
            it.def += this.def
            storePersonData(it)
            mHpLine += this.HP
        }
    }

    /****Person Bought*********/
    @Synchronized
    fun storePersonBoughtData(b: PersonBought) = SP_HANDLE.edit()
            .putString(PERSON_BASE_BOUGHT, Framework._G.toJson(b))
            .apply()

    fun getPersonBought(): PersonBought = SP_HANDLE.getString(PERSON_BASE_BOUGHT, "").run {
        if (this.isBlank())
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
    fun storeTasks(tasks: List<TaskEntity>) = SP_HANDLE.edit()
            .putString(PERSON_TASK, Framework._G.toJson(tasks))
            .apply()

    @Synchronized
    fun getTasks() = SP_HANDLE.getString(PERSON_TASK, "").run {
        if (this.isBlank())
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

}