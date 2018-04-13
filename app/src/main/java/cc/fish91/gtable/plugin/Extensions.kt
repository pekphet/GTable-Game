package cc.fish91.gtable.plugin

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.TextView
import android.widget.Toast
import cc.fish91.gtable.MonsterData

fun <E> List<E>.getRand() = this[Math.rand(size - 1)]
fun <E> Array<E>.getRand() = this[Math.rand(size - 1)]

fun MonsterData.changeToKing(): MonsterData {
    this.HP *= 4.toInt()
    this.atk *= 3.toInt()
    this.def *= 2.5.toInt()
    this.gold *= 3.toInt()
    this.exp *= 3.toInt()
    return this
}

fun Activity.toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

fun TextView.showNotEmpty(msg: String) {
    if (msg.isEmpty())
        visibility = View.GONE
    else
        text = msg
}

fun Context.dp2px(dp: Float) = (resources.displayMetrics.density * dp + 0.5).toInt()