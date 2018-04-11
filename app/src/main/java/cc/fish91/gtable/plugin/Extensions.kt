package cc.fish91.gtable.plugin

import android.app.Activity
import android.widget.Toast
import cc.fish91.gtable.MonsterData

fun <E> List<E>.getRand() = this[Math.rand(size - 1)]
fun <E> Array<E>.getRand() = this[Math.rand(size - 1)]

fun MonsterData.changeToKing(): MonsterData {
    this.HP *= 2.toInt()
    this.atk *= 2.toInt()
    this.def *= 2.toInt()
    this.gold *= 2.toInt()
    this.exp *= 2.toInt()
    return this
}

fun Activity.toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()