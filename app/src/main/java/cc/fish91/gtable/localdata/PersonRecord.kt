package cc.fish91.gtable.localdata

import android.content.Context
import cc.fish91.gtable.Framework
import cc.fish91.gtable.PersonData
import cc.fish91.gtable.resource.StaticData

object PersonRecord {
    private const val PERSON_FILE = "person"
    private const val PERSON_BASE_KEY = "base"
    private val SP_HANDLE by lazy { Framework._C.getSharedPreferences(PERSON_FILE, Context.MODE_PRIVATE) }
    fun getPersonData(): PersonData = SP_HANDLE.getString(PERSON_BASE_KEY, "").run {
        if (this.isBlank())
            PersonData(100, 2, 2, 0, 1, 0)
        else
            Framework._G.fromJson(this, PersonData::class.java)
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
        }
    }

    fun getPersonHPLine() = getPersonData().HP
}