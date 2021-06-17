package cc.fish91.gtable.activity

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cc.fish91.gtable.PersonData
import cc.fish91.gtable.R
import cc.fish91.gtable.localdata.PersonRecord
import cc.fish91.gtable.localdata.SaveDataManager
import cc.fish91.gtable.net.NetManager
import cc.fish91.gtable.view.Dialogs
import kotlinx.android.synthetic.main.a_choose.*

class ChoosingActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_choose)
        checkVer()
        initView()
    }

    private fun initView() {
        rv_chooser.layoutManager = LinearLayoutManager(this@ChoosingActivity)
        rv_chooser.adapter = mAdapter
        mAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        mAdapter.notifyDataSetChanged()
    }

    private fun checkVer() {
        NetManager.checkVersion { wikiUrl, downloadUrl -> Dialogs.AppDialog.showUpdate(this@ChoosingActivity, wikiUrl, downloadUrl) }
    }

    val mAdapter: RecyclerView.Adapter<PersonVH> = object : RecyclerView.Adapter<PersonVH>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonVH = PersonVH()

        override fun getItemCount() = SaveDataManager.FILES_COUNT

        override fun onBindViewHolder(holder: PersonVH, position: Int) {
            holder.load(PersonRecord.getPersonDataNullable(position), position)
        }

    }

    inner class PersonVH : RecyclerView.ViewHolder(LayoutInflater.from(this@ChoosingActivity).inflate(R.layout.i_choose_data, null)) {

        fun load(p: PersonData?, position: Int) {
            this@PersonVH.itemView.findViewById<TextView>(R.id.tv_ichose).text = if (p != null) "${p.name}: \tLv${p.level}\t${p.roleType.info}" else "新建存档"
            this@PersonVH.itemView?.setOnClickListener {
                SaveDataManager.checkedPosition = position
                MainActivity.start(this@ChoosingActivity)
            }
        }
    }
}