package cc.fish91.gtable.net

import android.util.Log
import cc.fish91.gtable.*
import cc.fish91.gtable.base.net.NetCore
import cc.fish91.gtable.localdata.EquipRecord
import cc.fish91.gtable.localdata.PersonRecord
import com.lenovo.lcui.base.net.Requester

object NetManager {

    const val BASE_API_PROD = "http://api.fishgame.com.cn:14000/v1"
    const val BASE_API_TEST = "http://111.229.9.170:14000/v1"
    const val CODE_SUCCESS = 0
    val BASE_API = BASE_API_TEST

    fun checkVersion(hasNewCallback: (wikiUrl: String, downloadUrl: String) -> Unit) {
        Requester<NetEntity.UpdateResp>().run {
            url("$BASE_API/app/update")
            mType = NetEntity.UpdateResp::class.java
            urlParam("version", Framework._C.packageManager.getPackageInfo(Framework._C.packageName, 0).versionCode)
            Get(Framework._C, {
                Log.e("net", this?.errMsg ?: "")
                if (this != null) {
                    if (this.hasNew) {
                        hasNewCallback(wikiUrl, downloadUrl)
                    }
                }
            }) {}
        }
    }

    fun commitFightResult(floor: Int) {
        Requester<NetEntity.BaseResp>().run {
            val p = PersonRecord.getPersonData()
            url("$BASE_API/result/upload")
            mType = NetEntity.BaseResp::class.java
            body("floor", "$floor")
            body("roleType", p.roleType)
            body("name", p.name)
            body("info", Framework._G.toJson(NetEntity.FightResultData(floor, p,
                    EquipRecord.getEqW(), EquipRecord.getEqA(), EquipRecord.getEqR())))
            Post(Framework._C, {}) {}
        }
    }

    fun loadTopList(roleType: RoleType, success: (Array<NetEntity.FightResultData>) -> Unit, failed: (String) -> Unit) {
        Requester<NetEntity.TopListResp>().run {
            url("$BASE_API/result/list")
            mType = NetEntity.TopListResp::class.java
            urlParam("type", roleType.name)
            Get(Framework._C, {
                if (this != null && this.errCode == CODE_SUCCESS)
                    success(resultList)
                else if (this != null)
                    failed(errMsg)
                else
                    failed("json error!")
            }) {
                failed(this)
            }
        }
    }

    fun checkName(name: String, ck: (Boolean) -> Unit) {
        Requester<NetEntity.BaseResp>().run {
            url("$BASE_API/account/check")
            mType = NetEntity.BaseResp::class.java
            urlParam("name", name)
            try {
                Get(Framework._C, {
                    if (this != null && this.errCode == CODE_SUCCESS) {
                        postPerson(name, { ck(true) }) { ck(false) }
                    }
                }) {
                    if (this == NetCore.IO_EXCEPTION) {
                        ck(true)
                    } else {
                        ck(false)
                    }
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    private fun postPerson(name: String, ck: () -> Unit, failed: (String) -> Unit) {
        Requester<NetEntity.BaseResp>().run {
            url("$BASE_API/account/new")
            mType = NetEntity.BaseResp::class.java
            body("name", name)
            Post(Framework._C, {
                if (this != null && this.errCode == CODE_SUCCESS) ck()
                else if (this != null) failed(this.errMsg)
                else failed("server error!")
            }) {
                failed(this)
            }
        }
    }

    fun storeInfoEquip(floor: Int) {
        Requester<NetEntity.BaseResp>().run {
            url("$BASE_API/account/store")
            mType = NetEntity.BaseResp::class.java
            jsonBody(Framework._G.toJson(NetEntity.FightResultData(floor, PersonRecord.getPersonData(),
                    EquipRecord.getEqW(), EquipRecord.getEqA(), EquipRecord.getEqR())))
            Post(Framework._C, {}) {}
        }
    }

    fun loadExchangeCode(code: String, result: (NetEntity.ExchangeCodeResp) -> Unit, failed: (String) -> Unit) {
        Requester<NetEntity.ExchangeCodeResp>().run {
            url("$BASE_API/award/code")
            mType = NetEntity.ExchangeCodeResp::class.java
            urlParam("code", code)
            urlParam("name", PersonRecord.getPersonData().name)
            Get(Framework._C, {
                if (this != null && this.errCode == CODE_SUCCESS)
                    result(this)
                else
                    failed(this?.errMsg ?: "")
            }) { failed(this) }
        }
    }

}

class NetEntity {
    data class UpdateResp(val errCode: Int, val errMsg: String, val hasNew: Boolean, var downloadUrl: String = "", val wikiUrl: String)
    data class BaseResp(val errCode: Int, val errMsg: String)
    data class ExchangeCodeResp(var errCode: Int, val errMsg: String, val awardType: ExchangeCodeType, val value: Int = 0, val eid: Int = 0, val rare: Int = 0)
    data class TopListResp(val errCode: Int, val errMsg: String, val resultList: Array<FightResultData>)


    data class FightResultData(val floor: Int, val p: PersonData, val eqW: Equip?, val eqA: Equip?, val eqR: Equip?)
}