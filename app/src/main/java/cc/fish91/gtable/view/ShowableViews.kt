package cc.fish91.gtable.view

import android.view.View
import android.widget.TextView
import cc.fish91.gtable.Framework
import java.util.concurrent.ArrayBlockingQueue

object ShowableViews {
    private val mQueue = ArrayBlockingQueue<Triple<View, Any?, Int>>(16)
    private val FADE_TIME_MS = 200L
    private val SHOW_IDLE_TIME = 200L

    fun show(v: View, msg: Any? = null, showTime: Int = 1000) {
        mQueue.put(Triple(v, msg, showTime))
        if (mQueue.size <= 1)
            showByQueue()
    }

    private fun showByQueue() {
        mQueue.peek().run {
            if (this == null)
                return
            first.alpha = 0f
            when(first) {
                is TextView -> if (second is CharSequence){(first as TextView).text = second as CharSequence}
            }
            first.visibility = View.VISIBLE
            first.animate().alpha(1f).setDuration(FADE_TIME_MS).setListener(null)
            Framework._H.postDelayed({
                first.animate().alpha(0f).setDuration(FADE_TIME_MS).setListener(null)
                mQueue.poll()
                Framework._H.postDelayed({
                    first.visibility = View.GONE
                    when(first) {
                        is TextView -> (first as TextView).text = ""
                    }
                    showByQueue()
                }, SHOW_IDLE_TIME + FADE_TIME_MS)
            }, third.toLong() + FADE_TIME_MS)
        }
    }
}