package cc.fish91.gtable.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cc.fish91.gtable.*
import cc.fish91.gtable.engine.*
import cc.fish91.gtable.localdata.EquipRecord
import cc.fish91.gtable.localdata.PersonRecord
import cc.fish91.gtable.net.NetEntity
import cc.fish91.gtable.net.NetManager
import cc.fish91.gtable.plugin.*
import cc.fish91.gtable.plugin.Math.getNear9Blocks
import cc.fish91.gtable.plugin.Math.getNearBlocks
import cc.fish91.gtable.resource.StaticData
import cc.fish91.gtable.resource.StaticData.AREA_SPLIT_FLOORS
import cc.fish91.gtable.resource.StaticData.CHANGE_ROLE_TYPE_1
import cc.fish91.gtable.resource.StaticData.KEEP_EQUIP_FLOORS
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

    private val mGridLayoutManager by lazy { GridLayoutManager(this@GameActivity, 5) }
    /**** FLOOR METAS************/
    private val mData = mutableListOf<FloorMeta>()
    private val mMonsters = mutableListOf<MonsterData>()
    private val mBuffs = mutableListOf<Buff>()
    private val mGifts = mutableListOf<Gift>()
    private val mDropMap = mutableMapOf<Int, Equip>()
    private val mEquips = mutableMapOf<EquipPosition, Equip>()
    private val mTasks = mutableListOf<TaskEntity>()
    private var mStartFloor = 0

    /****Game Scene Data*********/
    private var mMonsterK = MonsterData(1, 1, 1, 1, 1, 1)
    private var mMonsterSP = MonsterData(1, 1, 1, 1, 1, 1)
    private var mFloor = 1
    private var mArea = 1
    private val mPerson by lazy { PersonRecord.getPersonData() }
    private val mBought by lazy { PersonRecord.getPersonBought() }
    private val mPersonView by lazy { PersionView(this@GameActivity, mFightData) }
    private val mFightData = FightSceneFinalData()
    private lateinit var mRoleTypePSkill: IFightEffect


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_game)
        mFloor = intent.getIntExtra(GAME_FLOOR_KEY, 1)
        mStartFloor = mFloor
        makeFloor(mFloor)
        loadPerson()
        initTask()
        fl_game_persons.addView(mPersonView.getView())
        rv_game.layoutManager = mGridLayoutManager
        rv_game.adapter = mAdapter
    }

    private fun initTask() {
        PersonRecord.getTasks().run {
            map {
                if (it.type == TaskType.UP_FLOORS && it.currentValue < mFloor) {
                    it.needValue += mFloor - it.currentValue
                    it.currentValue = mFloor
                }
            }
            if (size < 3) {
                for (i in 1..(3 - size)) {
                    PersonRecord.storeTask(TaskEngine.create(mPerson.level, mFloor))
                }
            }
        }
        mTasks.clear()
        mTasks.addAll(PersonRecord.getTasks())
    }

    private fun loadPerson() {
        StaticData.statusUpCalc(mPerson, mBought)
        mRoleTypePSkill = mPerson.roleType.pSkill.objectInstance!!
        loadEquips()
        mFightData.reCalc(mPerson, *mEquips.values.toTypedArray())
        mPersonView.load(mPerson)
        mPersonView.setOnPersonClick({
            Dialogs.ExDialogs.showPerson(this@GameActivity, mFightData, mPerson.roleType)
        }) {
            Dialogs.questionSmall(this@GameActivity, resources.getString(R.string.del_role_tip)) {
                PersonRecord.clearData()
                finish()
            }
        }
        mPersonView.setOnQuestClick {
            Dialogs.ExDialogs.showTasks(this@GameActivity, mTasks, mTaskCK)
        }
        mPersonView.setOnCodeClick {
            Dialogs.edit(this@GameActivity, resources.getString(R.string.award_title), {
                NetManager.loadExchangeCode(it, {
                    takeCodeAward(it)
                    show(it.awardType.desc)
                }) { show(it) }
            })
        }
    }


    private fun loadEquips() {
        mEquips.putNullable(EquipPosition.WEAPON, EquipRecord.getEqW())
        mEquips.putNullable(EquipPosition.ARMOR, EquipRecord.getEqA())
        mEquips.putNullable(EquipPosition.RING, EquipRecord.getEqR())
        mFightData.suit = EquipEngine.getSuitById(EquipEngine.checkSuit(*(mEquips.values.toTypedArray())))
        mPersonView.flushEquip(mEquips, mFightData.suit != null)
    }

    private fun makeFloor(floor: Int) {
        TaskEngine.checkFloor(mTasks, floor)
        cleanTmp()
        mArea = floor / StaticData.AREA_SPLIT_FLOORS + 1
        if (floor % 10 == 0)
            mFightData.floorAppend.let {
                it.atk = 0
                it.def = 0
                it.HP = 0
            }
        FloorDataCreater.create(floor) {
            val tmpMsg = resources.getString(R.string.floor_info, "$mArea ", "${mFloor - AREA_SPLIT_FLOORS * (mArea - 1)} ")
//            val tmpMsg = "区域$mArea 第${mFloor - AREA_SPLIT_FLOORS * (mArea - 1)}层"
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
                    FloorStatus.MONSTER_SP -> mMonsterSP = FloorEngine.createMonster(StaticData.getRandSPMonsterId(), floor)
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

    val mAdapter = object : RecyclerView.Adapter<FloorVH>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FloorVH(parent, mMonsters, mGifts, mBuffs, mMonsterK, mMonsterSP, mDropMap)

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

    private val mTaskCK: (TaskEntity, Boolean) -> Unit = { task, isK ->
        when (task.award!!.type) {
            TaskAwardType.Exp -> {
                show(resources.getString(R.string.gain_exp, task.award?.aValue?:0))
                mPerson.exp += task.award!!.aValue
            }
            TaskAwardType.Gold -> {
                show(resources.getString(R.string.gain_gold, task.award?.aValue?:0))
                mPerson.gold += task.award!!.aValue
            }
            TaskAwardType.Equip -> {
                EquipEngine.createByRare(task.award!!.aValue, task.award!!.level, if (isK && Math.percent(5)) {
                    show(resources.getString(R.string.ur_equip_tip))
                    4
                } else task.award!!.rare).run {
                    showTaskEquip(this)
                }
            }
        }
        mTasks.remove(task)
        PersonRecord.storeTasks(mTasks)
    }

    private fun doExAct(position: Int, data: FloorMeta) {
        if (!data.isNearMonster && data.isOpened) {
            open(position, data)
            when (data.status) {
                FloorStatus.MONSTER -> Dialogs.ExDialogs.showMonster(this@GameActivity, mMonsters[data.exId], mFloor) {
                    doFight(position, mMonsters[data.exId], false, true)
                }
                FloorStatus.MONSTER_K -> Dialogs.ExDialogs.showMonster(this@GameActivity, mMonsterK, mFloor) {
                    doFight(position, mMonsterK, true, true)
                }
                FloorStatus.MONSTER_SP -> Dialogs.ExDialogs.showMonster(this@GameActivity, mMonsterSP, mFloor) {
                    doFight(position, mMonsterSP, false, true)
                }
            }
        }
    }

    private fun doAct(position: Int, data: FloorMeta) {
        if (!data.isNearMonster && data.isOpened) {
            open(position, data)
            when (data.status) {
                FloorStatus.MONSTER_K -> doFight(position, mMonsterK, true)
                FloorStatus.MONSTER_SP -> doFight(position, mMonsterSP, false)
                FloorStatus.MONSTER -> doFight(position, mMonsters[data.exId], false)
                FloorStatus.STAIR_DN -> if (mFightData.buff.keys > 0) makeFloor(++mFloor) else show(resources.getString(R.string.tip_need_key))
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
            show(resources.getString(R.string.tip_has_monster), 300)
        }
    }

    private fun showDroppedEquipInfo(position: Int, equip: Equip?) {
        if (equip == null) {
            mData[position].status = FloorStatus.IDLE
            return
        }
        Dialogs.ExDialogs.showEquipCompare(this@GameActivity, mEquips[equip.info.position], equip) {
            if (it) {
                if (mFloor <= KEEP_EQUIP_FLOORS || equip.rare >= 3)
                    EquipRecord.saveEq(equip)
                mEquips[equip.info.position] = equip
                mFightData.suit = EquipEngine.getSuitById(EquipEngine.checkSuit(*(mEquips.values.toTypedArray())))
                mFightData.reCalc(mPerson, *mEquips.values.toTypedArray())
                mPersonView.flushEquip(mEquips, mFightData.suit != null)
                mPersonView.load(mPerson)
            } else {
                mPerson.gold += equip.info.sell * equip.rare
                show(resources.getString(R.string.gain_gold, equip.info.sell * equip.rare))
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
        if (FightScene.fight(monsterData, mFightData, mFloor, forceEnd, mRoleTypePSkill)) {
            open(position, true)
            if (mFightData.HP <= 0) {
                toast("Died")
                FightScene.failed(mPerson, mTasks, mFloor)
                setResult(1001)
                finish()
            } else {
                if (isK)
                    mFightData.buff.keys++
                mFightData.HP = if (mFightData.HP + mFightData.restore > 0) Math.limitAdd(mFightData.HP, mFightData.restore, mFightData.HPLine) else 5
                TaskEngine.checkMonster(mTasks, monsterData.mId, isK)
                if (FightScene.award(mPerson, monsterData, isK)) {
                    show(resources.getString(R.string.tip_lvup), 1500)
                    mFightData.reCalc(mPerson, *mEquips.values.toTypedArray())
                    if (mPerson.level >= CHANGE_ROLE_TYPE_1 && mPerson.roleType == RoleType.BEGINNER) {
                        doRoleType()
                    }
                }
                FightScene.takeDrop(mFloor, isK, mData[position].status == FloorStatus.MONSTER_SP, monsterData).let {
                    if (it != null)
                        changeToDrop(it, position)
                    else
                        mData[position].status = FloorStatus.IDLE
                }
            }
        }
        flushPersonUI()
    }

    private fun doRoleType() {
        PersonRecord.getEnabledRoleType1().run {
            Dialogs.ExDialogs.showSelectors(this@GameActivity, resources.getString(R.string.transfer_tip), resources.getString(R.string.transfer_content), this.map { "${it.info}\n${it.pSkill.objectInstance!!.getInfo(0)}" }) { info, pos ->
                run {
                    PersonRecord.changeRoleType1(mPerson.name, this[pos], mPerson.gold)
                    toast(resources.getString(R.string.transfer_suc))
                    finish()
                }
            }
        }
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
        if (mFloor - mStartFloor >= 5) {
            NetManager.commitFightResult(mFloor)
        }
        FloorDataCreater.cleanData()
    }

    override fun onBackPressed() {
        setResult(1002)
        FightScene.failed(mPerson, mTasks, mFloor)
        super.onBackPressed()
    }

    private fun interrupt() {
        Dialogs.questionSmall(this@GameActivity, resources.getString(R.string.giveup_dungeon)) {
            FightScene.failed(mPerson, mTasks, mFloor)
            setResult(1003)
            finish()
        }
    }

    private fun showTaskEquip(equip: Equip) {
        Dialogs.ExDialogs.showEquipCompare(this@GameActivity, mEquips[equip.info.position], equip) {
            if (it) {
                if (mFloor <= KEEP_EQUIP_FLOORS || equip.rare >= 3)
                    EquipRecord.saveEq(equip)
                mEquips[equip.info.position] = equip
                mFightData.suit = EquipEngine.getSuitById(EquipEngine.checkSuit(*(mEquips.values.toTypedArray())))
                mFightData.reCalc(mPerson, *mEquips.values.toTypedArray())
                mPersonView.flushEquip(mEquips, mFightData.suit != null)
                mPersonView.load(mPerson)
            } else {
                (equip.rare * equip.info.sell).let {
                    mPerson.gold += it
                    show(resources.getString(R.string.gain_gold, it))
                }
            }
        }
    }

    private fun takeCodeAward(it: NetEntity.ExchangeCodeResp) = when (it.awardType) {
        ExchangeCodeType.EQUIP_A, ExchangeCodeType.EQUIP_W, ExchangeCodeType.EQUIP_R -> {
            showTaskEquip(EquipEngine.createByRare(it.eid, it.value, it.rare))
        }
        ExchangeCodeType.ERROR -> {
            show(resources.getString(R.string.tip_awardcode_err))
        }
        ExchangeCodeType.GOLD -> {
            mPerson.gold += it.value
        }
        ExchangeCodeType.LV -> {
            mPerson.exp += 99999
        }
        ExchangeCodeType.CHANGETYPE -> {
            mPerson.level = CHANGE_ROLE_TYPE_1
            mPerson.roleType = RoleType.BEGINNER
            mPerson.exp = 100000
        }
    }
}


class FloorVH(val floorView: FloorView, val dropMap: Map<Int, Equip>) : RecyclerView.ViewHolder(floorView.getView()) {
    constructor(parent: ViewGroup, monsters: List<MonsterData>, gifts: List<Gift>, buffs: List<Buff>, monsterK: MonsterData, monsterSP: MonsterData, dropMap: Map<Int, Equip>) : this(FloorView(parent, monsters, gifts, buffs, monsterK, monsterSP), dropMap)

    fun changeMonsterK(monsterK: MonsterData) = floorView.changeMonsterK(monsterK)
    fun load(data: FloorMeta) = floorView.load(data)
    fun loadDroppedItem(position: Int) = floorView.loadEquip(dropMap[position])
}

