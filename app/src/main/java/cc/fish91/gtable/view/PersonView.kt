package cc.fish91.gtable.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import cc.fish91.gtable.*
import cc.fish91.gtable.resource.StaticData


class PersionView(val ctx: Context, val fd: FightSceneFinalData) : LoadableView<PersonData> {
    private val view: View = LayoutInflater.from(ctx).inflate(R.layout.v_persion, null)
    private val mTvFloor = view.findViewById<TextView>(R.id.tv_vperson_floor)
    private val mTvHp = view.findViewById<TextView>(R.id.tv_vperson_hp)
    private val mTvAtk = view.findViewById<TextView>(R.id.tv_vperson_atk)
    private val mTvDef = view.findViewById<TextView>(R.id.tv_vperson_def)
    private val mTvGold = view.findViewById<TextView>(R.id.tv_vperson_gold)
    private val mTvKeys = view.findViewById<TextView>(R.id.tv_vperson_keys)
    private val mTvLv = view.findViewById<TextView>(R.id.tv_vperson_lv)
    private val mTvExp = view.findViewById<TextView>(R.id.tv_vperson_exp)
    private val mImgEqW = view.findViewById<ImageView>(R.id.img_vperson_eqw)
    private val mImgEqA = view.findViewById<ImageView>(R.id.img_vperson_eqa)
    private val mImgEqR = view.findViewById<ImageView>(R.id.img_vperson_eqr)

    @SuppressLint("SetTextI18n")
    override fun load(data: PersonData) {
        mTvHp.text = " HP: ${fd.HP}${fd.floorAppend.HP.run { if (this <= 0) "" else " +$this" }}"
        mTvAtk.text = "ATK: ${fd.atk + fd.floorAppend.atk}${fd.buff.tAtk.run { if (this == 0) "" else if (this < 0) " -$this" else " +$this" }}"
        mTvDef.text = "DEF: ${fd.def + fd.floorAppend.def}${fd.buff.tDef.run { if (this == 0) "" else if (this < 0) " -$this" else " +$this" }}"
        mTvGold.text = "GOLD: ${data.gold}"
        mTvKeys.text = "KEYS: ${fd.buff.keys}"
        mTvExp.text = "exp: ${data.exp} / ${StaticData.getLimitExp(data.level)}"
        mTvLv.text = "level: ${data.level}"
    }

    fun setFloor(floor: Int) {
        mTvFloor.text = "Floor: $floor"
    }

    override fun getView() = view

    fun flushEquip(map: Map<EquipPosition, Equip>) {
        map[EquipPosition.WEAPON]?.apply {
            mImgEqW.setImageResource(info.iconId)
            mImgEqW.setOnClickListener { Dialogs.ExDialogs.showEquip(ctx, this){} }
        }
        map[EquipPosition.ARMOR]?.apply {
            mImgEqA.setImageResource(info.iconId)
            mImgEqA.setOnClickListener { Dialogs.ExDialogs.showEquip(ctx, this){} }
        }
        map[EquipPosition.RING]?.apply {
            mImgEqR.setImageResource(info.iconId)
            mImgEqR.setOnClickListener { Dialogs.ExDialogs.showEquip(ctx, this){} }
        }
    }

}