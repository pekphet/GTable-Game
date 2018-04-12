package cc.fish91.gtable.view

import android.app.Activity
import android.app.Dialog
import android.view.View
import android.widget.TextView
import cc.fish91.gtable.R
import cc.fish91.gtable.plugin.showNotEmpty

object Dialogs {

    fun show(activity: Activity, title: String = "", msg: String, hasCancel: Boolean, ok: () -> Unit, cancel: () -> Unit) {
        Dialog(activity, R.style.app_dialog).apply {
            setContentView(R.layout.d_common)
            findViewById<TextView>(R.id.tv_d_title).showNotEmpty(title)
            findViewById<TextView>(R.id.tv_d_content).text = msg
            findViewById<TextView>(R.id.tv_d_ok).setOnClickListener{
                ok()
                this.cancel()
            }
            if (hasCancel)
                findViewById<View>(R.id.tv_d_cancel).setOnClickListener{
                    cancel()
                    this.cancel()
                }
            else {
                findViewById<View>(R.id.tv_d_cancel).visibility = View.GONE
                findViewById<View>(R.id.v_d_p).visibility = View.GONE
            }
        }.show()
    }

    fun show(activity: Activity, title: String = "", msg: String, ok: () -> Unit) = show(activity, title, msg, false, ok) {}

    fun question(activity: Activity, msg: String, ok:()->Unit) = show(activity, "", msg, true, ok){}
}