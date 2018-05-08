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
    private val mImgKey = view.findViewById<ImageView>(R.id.img_vperson_keys)
    private val mTvLv = view.findViewById<TextView>(R.id.tv_vperson_lv)
    private val mTvExp = view.findViewById<TextView>(R.id.tv_vperson_exp)
    private val mImgEqW = view.findViewById<ImageView>(R.id.img_vperson_eqw)
    private val mImgEqA = view.findViewById<ImageView>(R.id.img_vperson_eqa)
    private val mImgEqR = view.findViewById<ImageView>(R.id.img_vperson_eqr)

    @SuppressLint("SetTextI18n")
    override fun load(data: PersonData) {
        mTvHp.text = " HP: ${fd.HP}/${fd.HPLine} ${fd.floorAppend.HP.run { if (this <= 0) "" else " +$this" }}"
        mTvAtk.text = "ATK: ${fd.atk + fd.floorAppend.atk}${fd.buff.tAtk.run { if (this == 0) "" else if (this < 0) " -$this" else " +$this" }}"
        mTvDef.text = "DEF: ${fd.def + fd.floorAppend.def}${fd.buff.tDef.run { if (this == 0) "" else if (this < 0) " -$this" else " +$this" }}"
        mTvGold.text = "GOLD: ${data.gold}"
        mImgKey.visibility = if (fd.buff.keys > 0) View.VISIBLE else View.GONE
        mTvExp.text = "Exp: ${data.exp} / ${StaticData.getLimitExp(data.level)}"
        mTvLv.text = "[${data.roleType.info}] Lv: ${data.level}"
    }

    fun setFloor(floorMsg: String) {
        mTvFloor.text = floorMsg
    }

    override fun getView() = view

    fun flushEquip(map: Map<EquipPosition, Equip>, isSuit: Boolean = false) {
        map[EquipPosition.WEAPON]?.apply {
            mImgEqW.setImageResource(StaticData.getBaseEquipInfo(info.id).iconId)
            mImgEqW.setOnClickListener { Dialogs.ExDialogs.showEquip(ctx, this, isSuit) {} }
        }
        map[EquipPosition.ARMOR]?.apply {
            mImgEqA.setImageResource(StaticData.getBaseEquipInfo(info.id).iconId)
            mImgEqA.setOnClickListener { Dialogs.ExDialogs.showEquip(ctx, this, isSuit) {} }
        }
        map[EquipPosition.RING]?.apply {
            mImgEqR.setImageResource(StaticData.getBaseEquipInfo(info.id).iconId)
            mImgEqR.setOnClickListener { Dialogs.ExDialogs.showEquip(ctx, this, isSuit) {} }
        }
    }

    fun setOnQuestClick(clk: () -> Unit) {
        view.findViewById<ImageView>(R.id.img_vperson_quest).setOnClickListener {
            clk()
        }
    }

    fun setOnPersonClick(clk: () -> Unit, longClk: () -> Unit) {
        view.findViewById<ImageView>(R.id.img_vperson_person).setOnClickListener {
            clk()
        }
        view.findViewById<View>(R.id.img_vperson_person).setOnLongClickListener {
            longClk()
            true
        }
    }

}