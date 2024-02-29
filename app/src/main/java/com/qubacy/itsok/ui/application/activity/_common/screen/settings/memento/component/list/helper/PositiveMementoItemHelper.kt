package com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.component.list.helper

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class PositiveMementoItemHelper(
    private val mCallback: PositiveMementoItemHelperCallback
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        mCallback.onMementoSwipedOut(viewHolder.adapterPosition)
    }
}