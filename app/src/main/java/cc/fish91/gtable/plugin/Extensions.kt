package cc.fish91.gtable.plugin

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.TextView
import android.widget.Toast
import cc.fish91.gtable.FightSceneFinalData
import cc.fish91.gtable.MonsterData
import cc.fish91.gtable.PersonData


/****Math Ex*********/
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

/****View & Activity********/
fun Activity.toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

fun TextView.showNotEmpty(msg: String) {
    visibility = if (msg.isEmpty()) View.GONE else View.VISIBLE
    text = msg
}

fun Context.dp2px(dp: Float) = (resources.displayMetrics.density * dp + 0.5).toInt()

/****Strings****************/
fun Int.toMillicentKeep1() = String.format("%.1f", this/10f)
fun Int.max(max: Int) = if (this <= max) this else max

fun <E> Array<E>.makeStringArray(operator: E.() -> String) = map { it.operator() }

fun FightSceneFinalData.addPersonData(p: PersonData) {
    atk += p.atk
    def += p.def
    HP += p.HP
    critical += p.ex.critical
    criticalDmg += p.ex.critical_dmg
    miss += p.ex.miss
    restore += p.ex.restore
}
