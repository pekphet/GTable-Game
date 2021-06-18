package cc.fish91.gtable.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import cc.fish91.gtable.PersonBought
import cc.fish91.gtable.PersonData
import cc.fish91.gtable.R
import cc.fish91.gtable.RoleType
import cc.fish91.gtable.localdata.PersonRecord
import cc.fish91.gtable.net.NetManager
import cc.fish91.gtable.plugin.dp2px
import cc.fish91.gtable.plugin.makeStringArray
import cc.fish91.gtable.plugin.toast
import cc.fish91.gtable.resource.StaticData
import cc.fish91.gtable.resource.StaticInfo
import cc.fish91.gtable.view.Dialogs
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    companion object {
        fun start(ctx: Context) {
            ctx.startActivity(Intent(ctx, MainActivity::class.java))
        }
    }

    lateinit var mAdapter: ArrayAdapter<String>
    lateinit var mPersonData: PersonData
    lateinit var mBought: PersonBought
    var mFloor = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_start.setOnClickListener {
            GameActivity.start(this@MainActivity, mFloor)
        }
        img_main_info.setOnClickListener {
            Dialogs.show(this@MainActivity, "游戏说明", "${StaticInfo.APP_INFO_D}\n版本号：${packageManager.getPackageInfo(packageName, 0).versionName}") {}
        }
        tv_main_toplist.setOnClickListener {
            startActivity(Intent(this@MainActivity, TopListActivity::class.java))
        }
        flushPersonArea()
        flushUps()
        initSpinner()
//        Dialogs.ExDialogs.showSelectors(this@MainActivity, "123", "ceshi", RoleType.values().makeStringArray(RoleType::info)){
//            msg, position -> toast("At $position, Msg: $msg")
//        }
    }

    private fun checkName() {
        if (mPersonData.name.isEmpty()) {
            Dialogs.edit(this@MainActivity, "请输入角色名字", { name ->
                if (name.length > 8) {
                    toast("名字需要在8个字以内")
                    checkName()
                } else {
                    NetManager.checkName(name) {
                        if (it) {
                            mPersonData.name = name
                            PersonRecord.storePersonData(mPersonData)
                            toast("新建用户成功")
                            flushPersonArea()
                        } else {
                            toast("用户已经存在")
//                            checkName()
                        }
                    }
                }
            }){
                finish()
            }
        }
    }

    @Synchronized
    private fun flushUps() {
        tv_home_atk_up.let {
            it.text = "ATK Lv${mBought.atkLv + 1} 使用${StaticData.getUPFee(mBought.atkLv)}G升级"
            it.isEnabled = mPersonData.gold >= StaticData.getUPFee(mBought.atkLv)
            it.setOnClickListener {
                it.setOnClickListener(null)
                PersonRecord.abilityBought(mBought, mPersonData, 1)
                flushPersonArea()
                flushUps()
            }
        }
        tv_home_def_up.let {
            it.text = "DEF Lv${mBought.defLv + 1} 使用${StaticData.getUPFee(mBought.defLv)}G升级"
            it.isEnabled = mPersonData.gold >= StaticData.getUPFee(mBought.defLv)
            it.setOnClickListener {
                it.setOnClickListener(null)
                PersonRecord.abilityBought(mBought, mPersonData, 2)
                flushPersonArea()
                flushUps()
            }
        }
        tv_home_hp_up.let {
            it.text = "HP  Lv${mBought.hpLv + 1} 使用${StaticData.getUPFee(mBought.hpLv)}G升级"
            it.isEnabled = mPersonData.gold >= StaticData.getUPFee(mBought.hpLv)
            it.setOnClickListener {
                it.setOnClickListener(null)
                PersonRecord.abilityBought(mBought, mPersonData, 3)
                flushPersonArea()
                flushUps()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun flushPersonArea() {
        mBought = PersonRecord.getPersonBought()
        PersonRecord.getPersonData().let {
            mPersonData = it
            checkName()
            tv_main_lv.text = "[${it.roleType.info}]  ${it.name}  等级: ${it.level}"
            tv_main_exp.text = "经验值: ${it.exp} / ${StaticData.getLimitExp(it.level)}"
            tv_main_hp.text = "体力值: ${StaticData.getUppedHP(it.HP, mBought.hpLv)}"
            tv_main_atk.text = "攻击力: ${StaticData.getUppedAtk(it.atk, mBought.atkLv)}"
            tv_main_def.text = "防御力: ${StaticData.getUppedDef(it.def, mBought.defLv)}"
            tv_main_gold.text = "金币: ${it.gold}"
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 201) {
            flushPersonArea()
            flushUps()
            mAdapter.notifyDataSetChanged()
            when (resultCode) {
                1001 -> {           //DUNGEON DEAD
                    NetManager.storeInfoEquip(0)
                }
                1002 -> {           //PRESS BACK
                }
                1003 -> {           //Interrupt
                    NetManager.storeInfoEquip(0)
                }
            }
        }
    }

    private fun initSpinner() {
        mAdapter = object : ArrayAdapter<String>(this@MainActivity, R.layout.i_sp_main_floor) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup) = LayoutInflater.from(parent.context).inflate(R.layout.i_sp_main_floor, parent, false).also {
                it.findViewById<TextView>(R.id.text1).text = "${if (position > 0) position * 5 else 1}F"
                it.setBackgroundResource(android.R.color.transparent)
            }

            override fun getItem(position: Int) = "${if (position > 0) position * 5 else 1}F"

            override fun getItemId(position: Int) = position.toLong()

            override fun getCount() = mPersonData.maxFloor / 5 + 1
        }
        mAdapter.setDropDownViewResource(R.layout.i_sp_main_floor)
        sp_main_skip.adapter = mAdapter
        sp_main_skip.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mFloor = if (position == 0) 1 else position * 5
            }
        }
    }
}
