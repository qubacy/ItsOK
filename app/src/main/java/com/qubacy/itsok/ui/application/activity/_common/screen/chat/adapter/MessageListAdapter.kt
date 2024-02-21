package com.qubacy.itsok.ui.application.activity._common.screen.chat.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.qubacy.itsok.R
import com.qubacy.itsok.ui.application.activity._common.screen._common.component.message.view.active.ActiveMessageView
import com.qubacy.itsok.ui.application.activity._common.screen._common.component.message.view.previous.PreviousMessageView
import com.qubacy.itsok.ui.application.activity._common.screen.chat._common.data.message.UIMessage
import kotlinx.coroutines.CoroutineScope

class MessageListAdapter(
    private val mCoroutineScope: CoroutineScope
) : RecyclerView.Adapter<MessageListAdapter.MessageViewHolder>() {
    enum class ItemType(val id: Int) {
        ACTIVE(0), PREVIOUS(1);
    }

    abstract class MessageViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {
        abstract fun setData(message: UIMessage)
    }

    class PreviousMessageViewHolder(
        val messageView: PreviousMessageView
    ) : MessageViewHolder(messageView) {
        override fun setData(message: UIMessage) {
            messageView.setMessage(message)
        }
    }

    class ActiveMessageViewHolder(
        val messageView: ActiveMessageView
    ) : MessageViewHolder(messageView) {
        fun setData(
            message: UIMessage,
            animate: Boolean,
            endAction: (() -> Unit)? = null,
            detachmentAction: (() -> Unit)? = null
        ) {
            messageView.setMessage(message, animate, endAction, detachmentAction)
        }

        override fun setData(message: UIMessage) {
            messageView.setMessage(message)
        }
    }

    companion object {
        const val TAG = "MessageListAdapter"
    }

    private val mPendingMessagesToAdd: ArrayDeque<UIMessage> = ArrayDeque()

    private var mLastActiveMessageHash: Int = 0
    private val mItems: MutableList<UIMessage> = mutableListOf()
    val items: List<UIMessage> get() = mItems

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return when (viewType) {
            ItemType.PREVIOUS.id -> {
                val prevMessageView = (LayoutInflater.from(parent.context).inflate(
                    R.layout.component_prev_message, parent, false) as PreviousMessageView)
                    .apply { setCoroutineScope(mCoroutineScope) }

                PreviousMessageViewHolder(prevMessageView)
            }
            ItemType.ACTIVE.id -> {
                val activeMessageView = (LayoutInflater.from(parent.context).inflate(
                    R.layout.component_active_message, parent, false) as ActiveMessageView)
                    .apply { setCoroutineScope(mCoroutineScope) }

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

                holder as ActiveMessageViewHolder

                holder.setData(message, animate, {
                    runPendingItemAddingAnimation()
                }) {
                    onActiveElementDetached()
                }

                mLastActiveMessageHash = messageHash
            }
        }
    }

    private fun runPendingItemAddingAnimation() {
        if (mPendingMessagesToAdd.isEmpty()) return

        val nextMessage = mPendingMessagesToAdd.removeFirst()

        addItem(nextMessage)
    }

    private fun onActiveElementDetached() {
        val originalMessageCount = mItems.size

        mItems.addAll(mPendingMessagesToAdd)

        notifyItemRangeInserted(0, mPendingMessagesToAdd.size - 1)

        if (originalMessageCount > 0)
            notifyItemRangeChanged(mPendingMessagesToAdd.size, mItems.size - 1)

        mPendingMessagesToAdd.clear()
    }

    fun addItem(message: UIMessage) {
        mItems.add(message)

        notifyItemInserted(0)
        notifyItemRangeChanged(0, mItems.size - 1)
    }

    fun addItems(messages: List<UIMessage>) {
        if (messages.isEmpty()) return

        val reversedMessages = messages.reversed()

        val pendingMessages = if (mPendingMessagesToAdd.isEmpty()) {
            val lastMessage = reversedMessages.first()

            addItem(lastMessage)

            reversedMessages.drop(1)
        } else
            reversedMessages

        mPendingMessagesToAdd.addAll(pendingMessages)
    }

    fun setItems(messages: List<UIMessage>) {
        val reversedMessages = messages.reversed()

        mLastActiveMessageHash = reversedMessages[0].hashCode()

        mItems.apply {
            clear()
            addAll(reversedMessages)
        }

        notifyDataSetChanged()
    }
}