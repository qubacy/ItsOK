package com.qubacy.itsok.ui.application.activity._common.screen.chat.component.list.layout

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MessageListLayoutManager(
    context: Context,
    orientation: Int,
    reverseLayout: Boolean
) : LinearLayoutManager(context, orientation, reverseLayout) {
    companion object {
        const val TAG = "MessageListLayoutManger"
    }

    private var mCallback: MessageListLayoutManagerCallback? = null

    fun setCallback(callback: MessageListLayoutManagerCallback) {
        mCallback = callback
    }

    override fun onLayoutCompleted(state: RecyclerView.State?) {
        super.onLayoutCompleted(state)

        try { assertNotInLayoutOrScroll("") }
        catch (_ : IllegalStateException) { return }

        mCallback?.onLayoutCompleted()
    }
}