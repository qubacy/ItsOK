package com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.component.list.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento._common.data.UIMemento
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.component.list.helper.PositiveMementoItemHelperCallback
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.component.preview.PositiveMementoPreviewView

class PositiveMementoListAdapter(
    private val mCallback: PositiveMementoListAdapterCallback? = null
) : RecyclerView.Adapter<PositiveMementoListAdapter.PositiveMementoViewHolder>(),
    PositiveMementoItemHelperCallback
{
    class PositiveMementoViewHolder(
        val view: PositiveMementoPreviewView
    ) : RecyclerView.ViewHolder(view) {
        fun setData(memento: UIMemento) {
            view.setMemento(memento)
        }
    }

    private val mMementoList: MutableList<UIMemento> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PositiveMementoViewHolder {
        val mementoPreviewView = PositiveMementoPreviewView(parent.context)

        return PositiveMementoViewHolder(mementoPreviewView)
    }

    override fun getItemCount(): Int {
        return mMementoList.size
    }

    override fun onBindViewHolder(holder: PositiveMementoViewHolder, position: Int) {
        val memento = mMementoList[position]

        holder.setData(memento)
    }

    fun removeMementoAt(position: Int) {
        val memento = mMementoList.removeAt(position)

        mCallback?.onMementoRemoved(memento.id)

        notifyItemRemoved(position)
    }

    fun addMemento(memento: UIMemento) {
        mMementoList.add(memento)

        notifyItemInserted(mMementoList.size - 1)
    }

    fun setMementoes(mementoes: List<UIMemento>) {
        mMementoList.apply {
            clear()
            addAll(mementoes)
        }

        notifyDataSetChanged()
    }

    override fun onMementoSwipedOut(position: Int) {
        removeMementoAt(position)
    }
}