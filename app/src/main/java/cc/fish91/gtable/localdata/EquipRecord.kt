package cc.fish91.gtable.localdata

import android.content.Context
import cc.fish91.gtable.Equip
import cc.fish91.gtable.EquipPosition
import cc.fish91.gtable.Framework
import cc.fish91.gtable.resource.StaticData

object EquipRecord {
    private const val EQUIP_WORN_W = "worn_weapon"
    private const val EQUIP_WORN_A = "worn_armor"
    private const val EQUIP_WORN_R = "worn_ring"


    private fun SP_HANDLE() = Framework._C.getSharedPreferences(SaveDataManager.EQUIP_FILES[SaveDataManager.checkedPosition], Context.MODE_PRIVATE)

    private fun getEq(equipStr: String) = SP_HANDLE().getString(equipStr, "").run {
        if (this.isEmpty())
            null
        else
            Framework._G.fromJson(this, Equip::class.java)
    }

    fun getEqW() = getEq(EQUIP_WORN_W)
    fun getEqA() = getEq(EQUIP_WORN_A)
    fun getEqR() = getEq(EQUIP_WORN_R)

    fun saveEq(eq: Equip) {
        SP_HANDLE().edit().putString(
                when (eq.info.position) {
                    EquipPosition.WEAPON -> EQUIP_WORN_W
                    EquipPosition.ARMOR -> EQUIP_WORN_A
                    EquipPosition.RING -> EQUIP_WORN_R
                }, Framework._G.toJson(eq)).apply()
    }

}