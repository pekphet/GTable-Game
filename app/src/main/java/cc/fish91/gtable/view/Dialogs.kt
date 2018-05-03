package cc.fish91.gtable.view

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.*
import cc.fish91.gtable.*
import cc.fish91.gtable.engine.EquipEngine
import cc.fish91.gtable.engine.EquipEngine.getRareColor
import cc.fish91.gtable.plugin.dp2px
import cc.fish91.gtable.plugin.showNotEmpty
import cc.fish91.gtable.plugin.toMillicentKeep1
import cc.fish91.gtable.plugin.toPercentKeep1
import cc.fish91.gtable.resource.StaticData
import org.w3c.dom.Text

object Dialogs {

    fun show(activity: Activity, title: String = "", msg: String, hasCancel: Boolean, ok: () -> Unit, cancel: () -> Unit) {
        Dialog(activity, R.style.app_dialog).apply {
            setContentView(R.layout.d_common)
            findViewById<TextView>(R.id.tv_d_title).showNotEmpty(title)
            findViewById<TextView>(R.id.tv_d_content).text = msg
            findViewById<TextView>(R.id.tv_d_ok).setOnClickListener {
                ok()
                this.cancel()
            }
            if (hasCancel)
                findViewById<View>(R.id.tv_d_cancel).setOnClickListener {
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

    fun question(activity: Activity, msg: String, ok: () -> Unit) = show(activity, "", msg, true, ok) {}


    object ExDialogs {
        private val LinearLayoutParamsWW = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        fun showSelectors(activity: Activity, title: String, content: String, selectors: List<String>, callback: (String, Int) -> Unit) {
            Dialog(activity, R.style.app_dialog).apply {
                setContentView(R.layout.d_selector)
                var mTextColor = activity.resources.getColor(R.color.text_color_lv)
                var mTextSize = 0f
                findViewById<TextView>(R.id.tv_d_content).apply {
                    showNotEmpty(content)
                    mTextSize = this.textSize
                }
                findViewById<TextView>(R.id.tv_d_title).showNotEmpty(title)
                val rg = findViewById<RadioGroup>(R.id.rg_d_content).apply {
                    selectors.map {
                        addView(RadioButton(activity).apply {
                            setTextSize(TypedValue.COMPLEX_UNIT_PX, activity.dp2px(10f).toFloat())
                            setTextColor(mTextColor)
                            text = it
                        }, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    }
                    (getChildAt(0) as RadioButton).isChecked = true
                }
                findViewById<View>(R.id.tv_d_ok).setOnClickListener {
                    rg.indexOfChild(findViewById(rg.checkedRadioButtonId)).run { callback(selectors[this], this) }
                    cancel()
                }

            }.show()
        }


        @SuppressLint("SetTextI18n")
        fun showEquipCompare(activity: Activity, ori: Equip?, target: Equip, cmt: (Boolean) -> Unit) {
            Dialog(activity, R.style.app_dialog).apply {
                setContentView(R.layout.d_game_equip)
                findViewById<TextView>(R.id.tv_d_game_name).let {
                    it.text = "${if (target.level > 0) "+${target.level}" else ""} ${target.info.name}"
                    it.setTextColor(getRareColor(target.rare))
                }
                findViewById<ImageView>(R.id.img_d_game_title).setImageResource(target.info.iconId)
                findViewById<LinearLayout>(R.id.ll_d_game_equip_content).apply {
                    EquipEngine.getMainValue(target).let {
                        addView(getEquipInfoItem(activity, it.first, it.second,
                                EquipEngine.getMainValueNullable(it.first, ori?.info, ori?.level
                                        ?: 0) ?: 0))
                    }
                    target.exProperty.keys.toMutableSet().also {
                        if (ori != null)
                            it.addAll(ori.exProperty.keys)
                    }.forEach {
                        addView(getEquipInfoItem(activity, it, target.exProperty.getV(it)
                                ?: 0, ori?.exProperty?.getV(it) ?: 0), LinearLayoutParamsWW)
                    }
                }
                findViewById<View>(R.id.tv_d_ok).setOnClickListener {
                    cmt(true)
                    cancel()
                }
                findViewById<View>(R.id.tv_d_cancel).setOnClickListener {
                    cmt(false)
                    cancel()
                }
            }.show()
        }

        @SuppressLint("SetTextI18n")
        fun showEquip(context: Context, eq: Equip, clk: () -> Unit) {
            Dialog(context, R.style.app_dialog).apply {
                setContentView(R.layout.d_game_equip)
                findViewById<TextView>(R.id.tv_d_game_name).let {
                    it.text = "${if (eq.level > 0) "+${eq.level}" else ""} ${eq.info.name}"
                    it.setTextColor(getRareColor(eq.rare))
                }
                findViewById<ImageView>(R.id.img_d_game_title).setImageResource(eq.info.iconId)
                findViewById<LinearLayout>(R.id.ll_d_game_equip_content).apply {
                    EquipEngine.getMainValue(eq).let {
                        addView(getEquipInfoItem(context, it.first, it.second,
                                EquipEngine.getMainValueNullable(it.first, null, 0)))
                    }
                    eq.exProperty.keys.forEach {
                        addView(getEquipInfoItem(context, it, eq.exProperty.getV(it)
                                ?: 0, null), LinearLayoutParamsWW)
                    }
                }
                findViewById<TextView>(R.id.tv_d_ok).apply {
                    setOnClickListener {
                        clk()
                        cancel()
                    }
                    text = "关闭"
                }
                findViewById<View>(R.id.tv_d_cancel).visibility = View.GONE
                findViewById<View>(R.id.v_d_p).visibility = View.GONE
            }.show()
        }

        @SuppressLint("SetTextI18n")
        private fun getEquipInfoItem(ctx: Context, ep: EquipProperty, tar: Int, ori: Int?) = TextView(ctx).apply {
            var mOri = 0
            if (ori != null)
                mOri = ori
            else
                mOri = tar
            when {
                tar - mOri > 0 -> {
                    text = "${ep.description} ${getShowValueOfEqP(ep, tar)} (+${tar - mOri})"
                    setTextColor(resources.getColor(R.color.text_eq_more))
                }
                tar - mOri == 0 -> {
                    text = "${ep.description} ${getShowValueOfEqP(ep, tar)}"
                    setTextColor(resources.getColor(R.color.text_eq_equal))
                }
                tar - mOri < 0 -> {
                    text = "${ep.description} ${getShowValueOfEqP(ep, tar)} (-${mOri - tar})"
                    setTextColor(resources.getColor(R.color.text_eq_less))
                }
            }
            setTextSize(TypedValue.COMPLEX_UNIT_PX, Framework._C.dp2px(12f).toFloat())
        }

        private fun getShowValueOfEqP(ep: EquipProperty, value: Int) = when (ep) {
            EquipProperty.CRITICAL_DMG -> "+$value%"
            EquipProperty.ATK_PC, EquipProperty.DEF_PC, EquipProperty.HP_PC, EquipProperty.CRITICAL, EquipProperty.MISS -> "+${value.toMillicentKeep1()}"
            else -> "$value"
        }

        fun showMonster(activity: Activity, monster: MonsterData, floor: Int, fightCK: () -> Unit) {
            Dialog(activity, R.style.app_dialog).apply {
                val base = StaticData.getBaseMonster(monster.mId)
                setContentView(R.layout.d_game_monster)
                findViewById<ImageView>(R.id.img_d_game_title).setImageResource(base.iconId)
                findViewById<TextView>(R.id.tv_d_game_name).text = base.name
                findViewById<View>(R.id.tv_d_ok).setOnClickListener {
                    fightCK()
                    cancel()
                }
                findViewById<View>(R.id.tv_d_cancel).setOnClickListener { cancel() }
                findViewById<LinearLayout>(R.id.ll_d_game_monster_content).apply {
                    addView(getInfoText(activity, "攻击力: ${monster.atk}"), LinearLayoutParamsWW)
                    addView(getInfoText(activity, "防御力: ${monster.def}"), LinearLayoutParamsWW)
                    addView(getInfoText(activity, "生命值: ${monster.HP}"), LinearLayoutParamsWW)
                    addView(getInfoText(activity, "可掉落物品: ${StaticData.getBaseEquipInfo(base.drop.first).name}"), LinearLayoutParamsWW)
                    addView(getInfoText(activity, "装备暴率: ${(1.0 / base.drop.second).toPercentKeep1()}"), LinearLayoutParamsWW)
                }
                findViewById<TextView>(R.id.tv_game_monster_ex).apply {
                    if (base.exEffectClz == null) {
                        visibility = View.GONE
                    } else {
                        visibility = View.VISIBLE
                        text = base.exEffectClz?.objectInstance?.getInfo(floor) ?: ""
                    }
                }
            }.show()
        }

        private fun getInfoText(ctx: Context, text: String) = TextView(ctx).apply {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, Framework._C.dp2px(12f).toFloat())
            setTextColor(resources.getColor(R.color.text_color_lv))
            this.text = text
        }

        fun showTasks(activity: Activity, tasks: List<TaskEntity>, onTaskComplete: (TaskEntity, Boolean) -> Unit) {
            Dialog(activity, R.style.app_dialog).apply {
                setContentView(R.layout.d_tasks)
                findViewById<View>(R.id.tv_d_ok).setOnClickListener { dismiss() }
                findViewById<LinearLayout>(R.id.ll_d_task).let { ll ->
                    for (t in tasks)
                        ll.addView(TaskView(activity, t).getView {
                            dismiss()
                            onTaskComplete(t, t.isK)
                        })
                }
            }.show()
        }
    }
}