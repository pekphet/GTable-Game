package cc.fish91.gtable

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.google.gson.Gson

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