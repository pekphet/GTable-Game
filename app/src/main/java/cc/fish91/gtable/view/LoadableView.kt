package cc.fish91.gtable.view

import android.view.View

interface LoadableView<Data> {
    fun load(data: Data)
    fun getView() : View
}