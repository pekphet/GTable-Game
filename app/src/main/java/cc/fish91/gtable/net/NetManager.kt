package cc.fish91.gtable.net

import android.util.Log
import cc.fish.kfishhttp.Requester
import cc.fish91.gtable.Framework

object NetManager {

    const val BASE_API = "http://api.dogrid.cn:14000/v1"

    fun checkVersion(hasNewCallback: (wikiUrl: String, downloadUrl: String) -> Unit) {
        Requester<NetEntity.UpdateResp>().run {
            url("$BASE_API/app/update")
            mType = NetEntity.UpdateResp::class.java
            urlParam("version", Framework._C.packageManager.getPackageInfo(Framework._C.packageName, 0).versionCode)
            get(Framework._C, {
                Log.e("net", this?.errMsg ?: "")
                if (this != null) {
                    if (this.hasNew) {
                        hasNewCallback(wikiUrl, downloadUrl)
                    }
                }
            }) {}
        }
    }

}

class NetEntity {
    data class UpdateResp(val errCode: Int, val errMsg: String, val hasNew: Boolean, var downloadUrl: String = "", val wikiUrl: String)
}