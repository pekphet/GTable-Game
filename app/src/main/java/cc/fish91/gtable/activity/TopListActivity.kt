package cc.fish91.gtable.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import cc.fish91.gtable.R
import cc.fish91.gtable.RoleType
import cc.fish91.gtable.engine.EquipEngine
import cc.fish91.gtable.net.NetEntity
import cc.fish91.gtable.net.NetManager
import cc.fish91.gtable.plugin.ifTrue
import cc.fish91.gtable.plugin.toast
import cc.fish91.gtable.resource.StaticData
import cc.fish91.gtable.view.Dialogs
import kotlinx.android.synthetic.main.a_toplist.*

class TopListActivity : Activity() {
    val mData: MutableList<NetEntity.FightResultData> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_toplist)
        rv_toplist.layoutManager = LinearLayoutManager(this)
        rv_toplist.adapter = mAdapter
        tv_tl_ft.setOnClickListener(mSelClk)
        tv_tl_knt.setOnClickListener(mSelClk)
        tv_tl_nec.setOnClickListener(mSelClk)
        tv_tl_rog.setOnClickListener(mSelClk)
        mSelClk(tv_tl_ft)
    }

    var isLoading = false
    val mSelClk: (View) -> Unit = { view ->
        run {
            if (isLoading) return@run
            isLoading = true
            tv_tl_ft.isEnabled = true
            tv_tl_knt.isEnabled = true
            tv_tl_nec.isEnabled = true
            tv_tl_rog.isEnabled = true
            view.isEnabled = false
            NetManager.loadTopList(
                    when (view) {
                        tv_tl_ft -> RoleType.FIGHTER
                        tv_tl_nec -> RoleType.NEC
                        tv_tl_knt -> RoleType.KNIGHT
                        tv_tl_rog -> RoleType.ROGUE
                        else -> RoleType.FIGHTER
                    }, {
                mData.clear()
                mData.addAll(it)
                mAdapter.notifyDataSetChanged()
                isLoading = false
            }) {
                mData.clear()
                mAdapter.notifyDataSetChanged()
                isLoading = false
                toast(it)
            }
        }
    }


    val mAdapter = object : RecyclerView.Adapter<TLVH>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TLVH(LayoutInflater.from(this@TopListActivity).inflate(R.layout.i_top_list, parent, false))

        override fun getItemCount() = mData.size

        override fun onBindViewHolder(holder: TLVH, position: Int) {
            holder.load(mData[position], position)
        }
    }


    inner class TLVH(val v: View) : RecyclerView.ViewHolder(v) {
        private val mTvName = v.findViewById<TextView>(R.id.tv_itoplist_name)
        private val mTvFloor = v.findViewById<TextView>(R.id.tv_itoplist_floor)
        private val mImgEQW = v.findViewById<ImageView>(R.id.img_itoplist_eqw)
        private val mImgEQA = v.findViewById<ImageView>(R.id.img_itoplist_eqa)
        private val mImgEQR = v.findViewById<ImageView>(R.id.img_itoplist_eqr)

        @SuppressLint("SetTextI18n")
        fun load(data: NetEntity.FightResultData, position: Int) {
            mTvName.text = "${position + 1}\t${data.p.name}"
            mTvFloor.text = "${data.floor}F"
            mImgEQW.setImageResource(StaticData.getBaseEquipInfoNullable(data.eqW?.info?.id
                    ?: -1)?.iconId ?: R.color.bg_color_show)
            mImgEQA.setImageResource(StaticData.getBaseEquipInfoNullable(data.eqA?.info?.id
                    ?: -1)?.iconId ?: R.color.bg_color_show)
            mImgEQR.setImageResource(StaticData.getBaseEquipInfoNullable(data.eqR?.info?.id
                    ?: -1)?.iconId ?: R.color.bg_color_show)
            val isSuit = EquipEngine.checkSuit(data.eqW, data.eqA, data.eqR) != -1
            mImgEQW.setOnClickListener { if (data.eqW != null && data.eqW.info.id > 0) Dialogs.ExDialogs.showEquip(this@TopListActivity, data.eqW, isSuit) {} }
            mImgEQA.setOnClickListener { if (data.eqA != null && data.eqA.info.id > 0) Dialogs.ExDialogs.showEquip(this@TopListActivity, data.eqA, isSuit) {} }
            mImgEQR.setOnClickListener { if (data.eqR != null && data.eqR.info.id > 0) Dialogs.ExDialogs.showEquip(this@TopListActivity, data.eqR, isSuit) {} }
        }
    }
}