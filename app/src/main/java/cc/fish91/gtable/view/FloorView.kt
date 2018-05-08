package cc.fish91.gtable.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
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
    private val mContentImg = view.findViewById<ImageView>(R.id.img_gamefloor_content)
    private val mContentLL = view.findViewById<LinearLayout>(R.id.ll_gamefloor_content)
    private val mMonsterImg = view.findViewById<ImageView>(R.id.img_gamefloor_pic)
    private val mKMarkImg = view.findViewById<ImageView>(R.id.img_gamefloor_kmark)
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
                dispContent(false)
                mKMarkImg.visibility = View.VISIBLE
                mKMarkImg.setImageResource(R.drawable.t_icon_mark_king)
                mMonsterImg.setImageResource(StaticData.getBaseMonster(mMonsterK.mId).iconId)
                mAtkTv.text = "${mMonsterK.atk}"
                mDefTv.text = "${mMonsterK.def}"
                mHpTv.text = "${mMonsterK.HP}"
            }
            FloorStatus.MONSTER_SP -> {
                dispAll()
                dispContent(false)
                mKMarkImg.visibility = View.VISIBLE
                mKMarkImg.setImageResource(R.drawable.ticon_award)
                mMonsterImg.setImageResource(StaticData.getBaseMonster(mMonsterK.mId).iconId)
                mAtkTv.text = "${mMonsterK.atk}"
                mDefTv.text = "${mMonsterK.def}"
                mHpTv.text = "${mMonsterK.HP}"
            }
            FloorStatus.MONSTER -> {
                dispAll()
                dispContent(false)
                mMonsters[data.exId].run {
                    mMonsterImg.setImageResource(StaticData.getBaseMonster(mId).iconId)
                    mAtkTv.text = "$atk"
                    mDefTv.text = "$def"
                    mHpTv.text = "$HP"
                    mTv.text = StaticData.getBaseMonster(mId).name
                }
            }
            FloorStatus.BUFF -> {
                hideAll()
                dispContent(true)
                mBuff[data.exId].run {
                    mContentImg.setImageResource(StaticData.getBuffInfo(ability).first)
                    mTv.text = "$value"
                }
            }
            FloorStatus.GIFT -> {
                hideAll()
                dispContent(true)
                mGifts[data.exId].run {
                    mContentImg.setImageResource(StaticData.getGiftInfo(giftType).first)
                    mTv.text = "$value"
                }
            }
            FloorStatus.IDLE -> {
                hideAll()
                dispContent(false)
                mMonsterImg.visibility = View.GONE
            }
            FloorStatus.STAIR_DN -> {
                hideAll()
                dispContent(false)
                mMonsterImg.setImageResource(R.drawable.t_icon_down)
            }
            FloorStatus.STAIR_UP -> {
                hideAll()
                dispContent(false)
                mMonsterImg.setImageResource(R.drawable.t_icon_up)
            }
            FloorStatus.DROP -> {

            }
        }
    }

    fun changeMonsterK(monsterK: MonsterData) {
        mMonsterK = monsterK
    }

    fun loadEquip(q: Equip?) {
        if (q == null)  {
            hideAll()
            dispContent(false)
            mMonsterImg.visibility = View.GONE
            return
        }
        hideAll()
        dispContent(true)
        mContentImg.setImageResource(q.info.iconId)
        mTv.text = "+${q.level}"
    }

    override fun getView() = this.view

    private fun hideAll() {
        mKMarkImg.visibility = View.GONE
        mAtkTv.visibility = View.GONE
        mDefTv.visibility = View.GONE
        mHpTv.visibility = View.GONE
    }

    private fun dispAll() {
        mKMarkImg.visibility = View.GONE
        mAtkTv.visibility = View.VISIBLE
        mDefTv.visibility = View.VISIBLE
        mHpTv.visibility = View.VISIBLE
    }

    private fun dispContent(isDisp: Boolean) {
        mMonsterImg.visibility = if (isDisp) View.GONE else View.VISIBLE
        mContentLL.visibility = if (!isDisp) View.GONE else View.VISIBLE
    }
}