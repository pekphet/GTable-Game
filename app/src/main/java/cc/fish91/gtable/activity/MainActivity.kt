package cc.fish91.gtable.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import cc.fish91.gtable.R
import cc.fish91.gtable.localdata.PersonRecord
import cc.fish91.gtable.resource.StaticData
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_start.setOnClickListener {
            GameActivity.start(this@MainActivity, 1)
        }
        flushPersonArea()
    }

    @SuppressLint("SetTextI18n")
    private fun flushPersonArea() {
        PersonRecord.getPersonData().let {
            tv_main_lv.text = "Lv: ${it.level}"
            tv_main_exp.text = "EXP: ${it.exp} / ${StaticData.getLimitExp(it.level)}"
            tv_main_hp.text = "HP: ${it.HP}"
            tv_main_atk.text = "ATK: ${it.atk}"
            tv_main_def.text = "DEF: ${it.def}"
            tv_main_gold.text = "GOLD: ${it.gold}"
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 201) {
            flushPersonArea()
            when (resultCode) {
                1001 -> {           //DUNGEON DEAD
                }
                1002 -> {           //PRESS BACK
                }
            }
        }
    }

}
