package cc.fish91.gtable.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import cc.fish91.gtable.*
import cc.fish91.gtable.resource.StaticData

class FloorView(parent: ViewGroup, monsters: List<MonsterData>, gifts: List<Gift>, buffs: List<Buff>, monsterK: MonsterData) : LoadableView<FloorMeta> {
    private val view = LayoutInflater.from(parent.context).inflate(R.layout.v_floor_meta, parent, false)
    private val mTv = view.findViewById<TextView>(R.id.tv_gamefloor_content)
    private val mAtkTv = view.findViewById<TextView>(R.id.tv_gamefloor_atk)
    private val mDefTv = view.findViewById<TextView>(R.id.tv_gamefloor_def)
    private val mHpTv = view.findViewById<TextView>(R.id.tv_gamefloor_hp)
    private val mUnusedFL = view.findViewById<FrameLayout>(R.id.fl_gamefloor_unused)
    private val mNMonsterFL = view.findViewById<FrameLayout>(R.id.fl_gamefloor_nmst)
    private val mMonsters = monsters
    private val mGifts = gifts
    private val mBuff = buffs
    private var mMonsterK = monsterK

    override fun load(data: FloorMeta) {
        mUnusedFL.visibility = if (data.isOpened) View.GONE else View.VISIBLE
        mNMonsterFL.visibility = if (!data.isNearMonster) View.GONE else View.VISIBLE
        when (data.status) {
            FloorStatus.MONSTER_K -> {
                dispAll()
                mTv.text = "K-${StaticData.getBaseMonster(mMonsterK.mId).name}"
                mAtkTv.text = "${mMonsterK.atk}"
                mDefTv.text = "${mMonsterK.def}"
                mHpTv.text = "${mMonsterK.HP}"
            }
            FloorStatus.MONSTER -> {
                dispAll()
                mMonsters[data.exId].run {
                    mAtkTv.text = "$atk"
                    mDefTv.text = "$def"
                    mHpTv.text = "$HP"
                    mTv.text = StaticData.getBaseMonster(mId).name
                }
            }
            FloorStatus.BUFF -> {
                hideAll()
                mBuff[data.exId].run {
                    mTv.text = "${StaticData.getBuffInfo(ability).first}\n+ $value"
                }
            }
            FloorStatus.GIFT -> {
                hideAll()
                mGifts[data.exId].run {
                    mTv.text = "${StaticData.getGiftInfo(giftType).first}\n+ $value"
                }
            }
            FloorStatus.IDLE -> {
                hideAll()
                mTv.text = ""
            }
            FloorStatus.STAIR_DN -> {
                hideAll()
                mTv.text = "next"
            }
            FloorStatus.STAIR_UP -> {
                hideAll()
                mTv.text = "back"
            }
        }
    }

    fun changeMonsterK(monsterK: MonsterData) {
        mMonsterK = monsterK
    }

    override fun getView() = this.view

    private fun hideAll() {
        mAtkTv.visibility = View.GONE
        mDefTv.visibility = View.GONE
        mHpTv.visibility = View.GONE
    }
    private fun dispAll() {
        mAtkTv.visibility = View.VISIBLE
        mDefTv.visibility = View.VISIBLE
        mHpTv.visibility = View.VISIBLE
    }
}