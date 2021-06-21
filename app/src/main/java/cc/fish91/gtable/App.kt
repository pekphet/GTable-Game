package cc.fish91.gtable

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.annotation.StringRes
import com.google.gson.Gson
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

class App : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        Framework.sApplicationContext = base!!
    }
}

@SuppressLint("StaticFieldLeak")
object Framework {
    lateinit var sApplicationContext: Context
    val _H = Handler(Looper.getMainLooper())
    val _C by lazy { sApplicationContext }
    val _G by lazy { Gson() }
}
fun resString(@StringRes id: Int) = Framework._C.resources.getString(id)
fun resString(@StringRes id: Int, vararg args: Any) = Framework._C.resources.getString(id, args)