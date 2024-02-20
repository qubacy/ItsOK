package com.qubacy.itsok.ui.application.activity._common.screen.chat.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.qubacy.itsok.R
import com.qubacy.itsok.domain.chat.model.Message
import com.qubacy.itsok.ui.application.activity._common.screen._common.component.message.view.active.ActiveMessageView
import com.qubacy.itsok.ui.application.activity._common.screen._common.component.message.view.previous.PreviousMessageView
import kotlinx.coroutines.CoroutineScope

class MessageListAdapter(
    val coroutineScope: CoroutineScope
) : RecyclerView.Adapter<MessageListAdapter.MessageViewHolder>() {
    enum class ItemType(val id: Int) {
        ACTIVE(0), PREVIOUS(1);
    }

    abstract class MessageViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {
        abstract fun setData(message: Message)
    }

    class PreviousMessageViewHolder(
        val messageView: PreviousMessageView
    ) : MessageViewHolder(messageView) {
        override fun setData(message: Message) {
            messageView.setMessage(message)
        }
    }

    class ActiveMessageViewHolder(
        val messageView: ActiveMessageView
    ) : MessageViewHolder(messageView) {
        fun setData(message: Message, animate: Boolean) {
            messageView.setMessage(message, animate)
        }

        override fun setData(message: Message) {
            messageView.setMessage(message)
        }
    }

    companion object {
        const val TAG = "MessageListAdapter"
    }

    private var mLastActiveMessageHash: Int = 0
    private val mItems: MutableList<Message> = mutableListOf()
    val items: List<Message> get() = mItems

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return when (viewType) {
            ItemType.PREVIOUS.id -> {
                val prevMessageView = (LayoutInflater.from(parent.context).inflate(
                    R.layout.component_prev_message, parent, false) as PreviousMessageView)
                    .apply { setCoroutineScope(coroutineScope) }

                PreviousMessageViewHolder(prevMessageView)
            }
            ItemType.ACTIVE.id -> {
                val activeMessageView = (LayoutInflater.from(parent.context).inflate(
                    R.layout.component_active_message, parent, false) as ActiveMessageView)
                    .apply { setCoroutineScope(coroutineScope) }

                ActiveMessageViewHolder(activeMessageView)
            }
            else -> throw IllegalStateException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) ItemType.ACTIVE.id else ItemType.PREVIOUS.id
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = mItems[position]

        Log.d(TAG, "onBindViewHolder(): pos. = $position; message = ${message.toString()};")

        when (getItemViewType(position)) {
            ItemType.PREVIOUS.id -> holder.setData(message)
            ItemType.ACTIVE.id -> {
                val messageHash = message.hashCode()
                val animate = mLastActiveMessageHash != messageHash

                (holder as ActiveMessageViewHolder).setData(message, animate)

                mLastActiveMessageHash = messageHash
            }
        }
    }

    fun addItem(message: Message) {
        mItems.add(0, message)

        notifyItemInserted(0)
        notifyItemRangeChanged(0, mItems.size - 1)
    }

    fun setItems(messages: List<Message>) {
        val reversedMessages = messages.reversed()

        mLastActiveMessageHash = reversedMessages[0].hashCode()

        mItems.apply {
            clear()
            addAll(reversedMessages)
        }

        notifyDataSetChanged()
    }
}