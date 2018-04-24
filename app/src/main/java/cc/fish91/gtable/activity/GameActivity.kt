package cc.fish91.gtable.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.ViewGroup
import cc.fish91.gtable.*
import cc.fish91.gtable.engine.FightScene
import cc.fish91.gtable.engine.FloorDataCreater
import cc.fish91.gtable.engine.FloorEngine
import cc.fish91.gtable.localdata.EquipRecord
import cc.fish91.gtable.localdata.PersonRecord
import cc.fish91.gtable.plugin.*
import cc.fish91.gtable.plugin.Math.getNear9Blocks
import cc.fish91.gtable.plugin.Math.getNearBlocks
import cc.fish91.gtable.resource.StaticData
import cc.fish91.gtable.view.Dialogs
import cc.fish91.gtable.view.FloorView
import cc.fish91.gtable.view.PersionView
import cc.fish91.gtable.view.ShowableViews
import kotlinx.android.synthetic.main.a_game.*

class GameActivity : Activity() {
    companion object {
        const val GAME_FLOOR_KEY = "KEY_FLOOR"
        fun start(a: Activity, floor: Int) {
            a.startActivityForResult(Intent(a, GameActivity::class.java).apply {
                putExtra(GAME_FLOOR_KEY, floor)
            }, 201)
        }
    }

    private val AREA_SPLIT_FLOORS = 30

    private val mGridLayoutManager by lazy { GridLayoutManager(this@GameActivity, 5) }
    private /**** FLOOR METAS************/
    val mData = mutableListOf<FloorMeta>()
    private val mMonsters = mutableListOf<MonsterData>()
    private val mBuffs = mutableListOf<Buff>()
    private val mGifts = mutableListOf<Gift>()
    private val mDropMap = mutableMapOf<Int, Equip>()
    private val mEquips = mutableMapOf<EquipPosition, Equip>()

    /****Game Scene Data*********/
    private var mMonsterK = MonsterData(1, 1, 1, 1, 1, 1)
    private var mFloor = 1
    private var mArea = 1
    private val mPerson by lazy { PersonRecord.getPersonData() }
    private val mBought by lazy { PersonRecord.getPersonBought() }
    private val mPersonView by lazy { PersionView(this@GameActivity, mFightData) }
    private val mFightData = FightSceneFinalData()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_game)
        mFloor = intent.getIntExtra(GAME_FLOOR_KEY, 1)
        makeFloor(mFloor)
        loadPerson()
        fl_game_persons.addView(mPersonView.getView())
        rv_game.layoutManager = mGridLayoutManager
        rv_game.adapter = mAdapter
    }

    private fun loadPerson() {
        StaticData.statusUpCalc(mPerson, mBought)
        loadEquips()
        mFightData.reCalc(mPerson, *mEquips.values.toTypedArray())
        mPersonView.load(mPerson)
    }


    private fun loadEquips() {
        mEquips.putNullable(EquipPosition.WEAPON, EquipRecord.getEqW())
        mEquips.putNullable(EquipPosition.ARMOR, EquipRecord.getEqA())
        mEquips.putNullable(EquipPosition.RING, EquipRecord.getEqR())
        mPersonView.flushEquip(mEquips)
    }

    private fun makeFloor(floor: Int) {
        cleanTmp()
        mArea = floor / 30 + 1
        if (floor % 10 == 0)
            mFightData.floorAppend.let {
                it.atk = 0
                it.def = 0
                it.HP = 0
            }
        FloorDataCreater.create(floor) {
            val tmpMsg = "区域$mArea 第${mFloor - AREA_SPLIT_FLOORS * (mArea - 1)}层"
            show(tmpMsg)
            mPersonView.setFloor(tmpMsg)
            mData.addAll(this)
            flushPersonUI()
            mMonsters.addAll(FloorEngine.createMonsters(mArea, floor, FloorDataCreater.getMonsterCount(floor)))
            mData.map {
                when (it.status) {
                    FloorStatus.BUFF -> mBuffs.add(FloorEngine.createBuff(mArea, floor))
                    FloorStatus.GIFT -> mGifts.add(FloorEngine.createGift(mArea, floor))
                    FloorStatus.MONSTER_K -> mMonsterK = FloorEngine.createMonsters(mArea, floor, 1)[0].changeToKing()
                    else -> {
                    }
                }
            }
            mAdapter.notifyDataSetChanged()
            for (i in mData.indices) {
                if (mData[i].status == FloorStatus.STAIR_UP) {
                    open(i, mData[i])
                    break
                }
            }
        }
    }

    private fun cleanTmp() {
        mFightData.buff.clear()
        mData.clear()
        mMonsters.clear()
        mBuffs.clear()
        mGifts.clear()
        mDropMap.clear()
    }

    val mAdapter = object : Adapter<FloorVH>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FloorVH(parent, mMonsters, mGifts, mBuffs, mMonsterK, mDropMap)

        override fun getItemCount() = 5 * 7

        override fun onBindViewHolder(holder: FloorVH, position: Int) {
            mData[position].run {
                holder.load(this)
                when (status) {
                    FloorStatus.MONSTER_K -> holder.changeMonsterK(mMonsterK)
                    FloorStatus.DROP -> holder.loadDroppedItem(position)
                }
                holder.itemView.setOnClickListener {
                    doAct(position, this)
                }
                holder.itemView.setOnLongClickListener {
                    doExAct(position, this)
                    true
                }
            }
        }
    }

    private fun doExAct(position: Int, data: FloorMeta) {
        if (!data.isNearMonster && data.isOpened) {
            open(position, data)
            when(data.status) {
                FloorStatus.MONSTER -> Dialogs.ExDialogs.showMonster(this@GameActivity, mMonsters[data.exId]) {
                    doFight(position, mMonsters[data.exId], false, true)
                }
                FloorStatus.MONSTER_K -> Dialogs.ExDialogs.showMonster(this@GameActivity, mMonsterK) {
                    doFight(position, mMonsterK, true, true)
                }
            }
        }
    }

    private fun doAct(position: Int, data: FloorMeta) {
        if (!data.isNearMonster && data.isOpened) {
            open(position, data)
            when (data.status) {
                FloorStatus.MONSTER_K -> doFight(position, mMonsterK, true)
                FloorStatus.MONSTER -> doFight(position, mMonsters[data.exId], false)
                FloorStatus.STAIR_DN -> if (mFightData.buff.keys > 0) makeFloor(++mFloor) else show("缺少钥匙")
                FloorStatus.STAIR_UP -> {
                    interrupt()
                }
                FloorStatus.GIFT -> doGift(position, data)
                FloorStatus.BUFF -> doBuff(position, data)
                FloorStatus.IDLE -> {
                }
                FloorStatus.DROP -> {
                    showDroppedEquipInfo(position, mDropMap[position])
                }
            }
        } else if (data.isNearMonster) {
            show("请先击杀附近的怪物", 300)
        }
    }

    private fun showDroppedEquipInfo(position: Int, equip: Equip?) {
        if (equip == null) {
            mData[position].status = FloorStatus.IDLE
            return
        }
        Dialogs.ExDialogs.showEquipCompare(this@GameActivity, mEquips[equip.info.position], equip) {
            if (it) {
                EquipRecord.saveEq(equip)
                mEquips[equip.info.position] = equip
                mFightData.reCalc(mPerson, *mEquips.values.toTypedArray())
                mPersonView.flushEquip(mEquips)
                mPersonView.load(mPerson)
            } else {

            }
            mData[position].status = FloorStatus.IDLE
            mAdapter.notifyItemChanged(position)
        }
    }

    private fun doBuff(position: Int, data: FloorMeta) {
        FightScene.buff(mBuffs[data.exId], mFightData.buff)
        flushPersonUI()
        cleanMeta(position)
    }

    private fun doGift(position: Int, data: FloorMeta) {
        mGifts[data.exId].let {
            FightScene.gift(it, mFightData)
            flushPersonUI()
            cleanMeta(position)
            show("${it.giftType.desc} ${it.value}")
        }
    }

    private fun doFight(position: Int, monsterData: MonsterData, isK: Boolean, forceEnd: Boolean = false) {
        if (FightScene.fight(monsterData, mFightData, forceEnd)) {
            open(position, true)
            if (mFightData.HP <= 0) {
                toast("died")
                FightScene.failed(mPerson, mFloor)
                setResult(1001)
                finish()
            } else {
                if (isK)
                    mFightData.buff.keys++
                mFightData.HP += mFightData.restore
                if (FightScene.award(mPerson, monsterData, isK)) {
                    show("等级上升！", 1500)
                    mFightData.reCalc(mPerson, *mEquips.values.toTypedArray())
                }
                FightScene.takeDrop(mFloor, isK, monsterData).let {
                    if (it != null)
                        changeToDrop(it, position)
                    else
                        mData[position].status = FloorStatus.IDLE
                }
            }
        }
        flushPersonUI()
    }

    private fun changeToDrop(e: Equip, position: Int) {
        mData[position].status = FloorStatus.DROP
        mDropMap[position] = e
        mAdapter.notifyItemChanged(position)
    }

    private fun flushPersonUI() {
        mPersonView.load(mPerson)
    }

    private fun cleanMeta(position: Int) {
        mData[position].status = FloorStatus.IDLE
        mAdapter.notifyItemChanged(position)
    }

    private fun open(position: Int, isForce: Boolean = false) = open(position, mData[position], isForce)
    private fun open(position: Int, data: FloorMeta, isForce: Boolean = false) {
        mData[position].isOpened = true
        if (data.status == FloorStatus.MONSTER_K || data.status == FloorStatus.MONSTER) {
            getNear9Blocks(position).map {
                mData[it].let {
                    it.isNearMonster = !isForce
                    if (it.status == FloorStatus.MONSTER || it.status == FloorStatus.MONSTER_K)
                        it.isNearMonster = false
                }
            }
        }
        if (isForce || !(data.status == FloorStatus.MONSTER_K || data.status == FloorStatus.MONSTER)) {
            getNearBlocks(position).map {
                mData[it].run {
                    isOpened = true
                    if (status == FloorStatus.MONSTER || status == FloorStatus.MONSTER_K) {
                        open(it, this)
                    }
                }
            }
        }
        mAdapter.notifyDataSetChanged()
    }

    private fun show(msg: String, showingTime: Int = 1000) = ShowableViews.show(tv_game_show, msg, showingTime)

    override fun onDestroy() {
        super.onDestroy()
        FloorDataCreater.cleanData()
    }

    override fun onBackPressed() {
        setResult(1002)
        FightScene.failed(mPerson, mFloor)
        super.onBackPressed()
    }

    private fun interrupt() {
        Dialogs.question(this@GameActivity, "确定返回主城？") {
            FightScene.failed(mPerson, mFloor)
            finish()
        }
    }
}


class FloorVH(val floorView: FloorView, val dropMap: Map<Int, Equip>) : RecyclerView.ViewHolder(floorView.getView()) {
    constructor(parent: ViewGroup, monsters: List<MonsterData>, gifts: List<Gift>, buffs: List<Buff>, monsterK: MonsterData, dropMap: Map<Int, Equip>) : this(FloorView(parent, monsters, gifts, buffs, monsterK), dropMap)

    fun changeMonsterK(monsterK: MonsterData) = floorView.changeMonsterK(monsterK)
    fun load(data: FloorMeta) = floorView.load(data)
    fun loadDroppedItem(position: Int) = floorView.loadEquip(dropMap[position])
}

