package com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.component.list.adapter

import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.recyclerview.widget.RecyclerView
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.component.list.adapter.BaseRecyclerViewAdapter
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento._common.data.UIMemento
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.component.list.helper.PositiveMementoItemHelperCallback
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.component.preview.PositiveMementoPreviewView

class PositiveMementoListAdapter(
    private val mCallback: PositiveMementoListAdapterCallback? = null
) : BaseRecyclerViewAdapter<UIMemento, PositiveMementoListAdapter.PositiveMementoViewHolder>(),
    PositiveMementoItemHelperCallback
{
    class PositiveMementoViewHolder(
        val view: PositiveMementoPreviewView
    ) : RecyclerView.ViewHolder(view) {
        fun setData(memento: UIMemento) {
            view.setMemento(memento)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PositiveMementoViewHolder {
        val mementoPreviewView = PositiveMementoPreviewView(parent.context)

        return PositiveMementoViewHolder(mementoPreviewView)
    }

    override fun onBindViewHolder(holder: PositiveMementoViewHolder, position: Int) {
        val memento = mItems[position]

        holder.apply {
            setData(memento)

            view.setOnClickListener { mCallback?.onMementoClicked(memento.id) }
        }
    }

    @UiThread
    fun removeMementoAt(position: Int) {
        val memento = mItems.removeAt(position)

        mCallback?.onMementoRemoved(memento.id)

        wrappedNotifyItemRemoved(position)
    }

    @UiThread
    fun addMemento(memento: UIMemento) {
        mItems.add(memento)

        wrappedNotifyItemInserted(mItems.size - 1)
    }

    @UiThread
    fun updateMemento(memento: UIMemento, index: Int) {
        mItems[index] = memento

        wrappedNotifyItemChanged(index)
    }

    @UiThread
    fun setMementoes(mementoes: List<UIMemento>) {
        replaceItems(mementoes)

        wrappedNotifyDataSetChanged()
    }

    override fun onMementoSwipedOut(position: Int) {
        removeMementoAt(position)
    }
}