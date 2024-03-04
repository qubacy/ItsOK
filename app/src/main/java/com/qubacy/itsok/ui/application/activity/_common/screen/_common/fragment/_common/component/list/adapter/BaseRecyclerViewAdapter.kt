package com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.component.list.adapter

import androidx.annotation.UiThread
import androidx.recyclerview.widget.RecyclerView

/**
 * Note: In order to successfully compose Unit tests, it's recommended to use WRAPPED version
 * of notify..() methods.
 */
abstract class BaseRecyclerViewAdapter<ItemType, ViewHolderType: RecyclerView.ViewHolder>(

) : RecyclerView.Adapter<ViewHolderType>() {
    protected val mItems: MutableList<ItemType> = mutableListOf()
    val items: List<ItemType> get() = mItems

    override fun getItemCount(): Int {
        return mItems.size
    }

    protected fun replaceItems(items: List<ItemType>) {
        mItems.apply {
            clear()
            addAll(items)
        }
    }

    @UiThread
    fun resetItems() {
        wrappedNotifyItemRangeRemoved(0, mItems.size)
        mItems.clear()
    }

    open fun wrappedNotifyDataSetChanged() {
        notifyDataSetChanged()
    }

    open fun wrappedNotifyItemInserted(position: Int) {
        notifyItemInserted(position)
    }

    open fun wrappedNotifyItemRemoved(position: Int) {
        notifyItemRemoved(position)
    }

    open fun wrappedNotifyItemChanged(position: Int) {
        notifyItemChanged(position)
    }

    open fun wrappedNotifyItemMoved(fromPosition: Int, toPosition: Int) {
        notifyItemMoved(fromPosition, toPosition)
    }

    open fun wrappedNotifyItemRangeChanged(positionStart: Int, itemCount: Int) {
        notifyItemRangeChanged(positionStart, itemCount)
    }

    open fun wrappedNotifyItemRangeRemoved(positionStart: Int, itemCount: Int) {
        notifyItemRangeRemoved(positionStart, itemCount)
    }

    open fun wrappedNotifyItemRangeInserted(positionStart: Int, itemCount: Int) {
        notifyItemRangeInserted(positionStart, itemCount)
    }
}