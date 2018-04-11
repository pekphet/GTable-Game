package cc.fish91.gtable.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import cc.fish91.gtable.FloorBuff
import cc.fish91.gtable.PersonData
import cc.fish91.gtable.R


class PersionView(ctx: Context, val fb: FloorBuff) : LoadableView<PersonData> {
    private val view: View = LayoutInflater.from(ctx).inflate(R.layout.v_persion, null)
    private val mTvFloor = view.findViewById<TextView>(R.id.tv_vperson_floor)
    private val mTvHp = view.findViewById<TextView>(R.id.tv_vperson_hp)
    private val mTvAtk = view.findViewById<TextView>(R.id.tv_vperson_atk)
    private val mTvDef = view.findViewById<TextView>(R.id.tv_vperson_def)
    private val mTvGold = view.findViewById<TextView>(R.id.tv_vperson_gold)
    private val mTvKeys = view.findViewById<TextView>(R.id.tv_vperson_keys)
    override fun load(data: PersonData) {
        mTvHp.text = " HP: ${data.HP}"
        mTvAtk.text = "ATK: ${data.atk}${fb.tAtk.run { if (this == 0) "" else if (this < 0) " -$this" else " +$this" }}"
        mTvDef.text = "DEF: ${data.def}${fb.tDef.run { if (this == 0) "" else if (this < 0) " -$this" else " +$this" }}"
        mTvGold.text = "GOLD: ${data.gold}"
        mTvKeys.text = "KEYS: ${fb.keys}"
    }

    fun setFloor(floor: Int) {
        mTvFloor.text = "Floor: $floor"
    }

    override fun getView() = view

}