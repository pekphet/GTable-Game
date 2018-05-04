package cc.fish91.gtable.view

import android.annotation.SuppressLint
import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import cc.fish91.gtable.*
import cc.fish91.gtable.engine.EquipEngine.getRareColor
import cc.fish91.gtable.resource.StaticData

class TaskView(val ctx: Context, val task: TaskEntity) {
    private val mView: View = LayoutInflater.from(ctx).inflate(R.layout.i_d_task, null)
    private val mTvInfo = mView.findViewById<TextView>(R.id.tv_i_d_task_info)
    private val mTvAward = mView.findViewById<TextView>(R.id.tv_i_d_task_award)
    private val mTvProg = mView.findViewById<TextView>(R.id.tv_i_d_task_prog)

    @SuppressLint("SetTextI18n")
    fun getView(onComplete: () -> Unit): View {
        mTvInfo.text = when (task.type) {
            TaskType.KILL_MONSTER -> Html.fromHtml(String.format(task.type.info, "${StaticData.getBaseMonster(task.monsterId).name}${if (task.isK) "Boss" else ""}", task.needValue))
            TaskType.UP_FLOORS -> String.format(task.type.info, task.needValue)
        }
        mTvAward.text = task.award!!.type.info
        if (task.award!!.type == TaskAwardType.Equip) {
            mTvAward.setTextColor(getRareColor(task.award!!.rare))
        }
        mTvProg.run {
            if (task.currentValue < task.needValue) {
                text = "${task.currentValue} / ${task.needValue}"
                setTextColor(Framework._C.resources.getColor(R.color.text_eq_less))
            } else {
                text = "获取奖励"
                setTextColor(Framework._C.resources.getColor(R.color.text_eq_more))
            }
        }
        if (task.currentValue >= task.needValue) {
            mView.setOnClickListener {
                onComplete()
                mView.setOnClickListener {}
            }
        }
        return mView
    }
}