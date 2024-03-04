package com.qubacy.itsok.ui.application.activity._common.screen.chat.component.list.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.recyclerview.widget.RecyclerView
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.component.list.adapter.BaseRecyclerViewAdapter
import com.qubacy.itsok.ui.application.activity._common.screen.chat.component.message.view.active.ActiveMessageView
import com.qubacy.itsok.ui.application.activity._common.screen.chat.component.message.view.previous.PreviousMessageView
import com.qubacy.itsok.ui.application.activity._common.screen.chat._common.data.message.UIMessage
import com.qubacy.itsok.ui.application.activity._common.screen.chat.component.list.layout.MessageListLayoutManager
import com.qubacy.itsok.ui.application.activity._common.screen.chat.component.list.layout.MessageListLayoutManagerCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope

open class MessageListAdapter(
    private val mCoroutineScope: CoroutineScope = GlobalScope
) : BaseRecyclerViewAdapter<UIMessage, MessageListAdapter.MessageViewHolder>(),
    MessageListLayoutManagerCallback
{
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

    private lateinit var mRecyclerView: RecyclerView

    private val mPendingMessagesToAdd: ArrayDeque<UIMessage> = ArrayDeque()

    private var mCurrentActiveMessageViewHolder: ActiveMessageViewHolder? = null
    private var mLastRecycledActiveMessageViewHolder: ActiveMessageViewHolder? = null
    private var mIsActiveMessageViewHolderAnimationInterrupted: Boolean = true
    private var mLastActiveMessageHash: Int = 0

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        mRecyclerView = recyclerView

        (mRecyclerView.layoutManager as MessageListLayoutManager).setCallback(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return when (viewType) {
            ItemType.PREVIOUS.id -> createPreviousMessageViewHolder(parent.context)
            ItemType.ACTIVE.id -> createActiveMessageViewHolder(parent.context)
            else -> throw IllegalStateException()
        }
    }

    open fun createPreviousMessageViewHolder(context: Context): PreviousMessageViewHolder {
        val prevMessageView = PreviousMessageView(context)

        return PreviousMessageViewHolder(prevMessageView)
    }

    open fun createActiveMessageViewHolder(context: Context): ActiveMessageViewHolder {
        val activeMessageView = ActiveMessageView(context).apply {
            setCoroutineScope(mCoroutineScope)
        }

        return ActiveMessageViewHolder(activeMessageView)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) ItemType.ACTIVE.id else ItemType.PREVIOUS.id
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = mItems[position]

        when (getItemViewType(position)) {
            ItemType.PREVIOUS.id -> holder.setData(message)
            ItemType.ACTIVE.id -> {
                val messageHash = message.hashCode()
                val animate = mLastActiveMessageHash != messageHash

                holder as ActiveMessageViewHolder

                mCurrentActiveMessageViewHolder = holder
                mIsActiveMessageViewHolderAnimationInterrupted = false

                holder.setData(message, animate, {
                    runPendingItemAddingAnimation()
                }) {
                    mIsActiveMessageViewHolderAnimationInterrupted =
                        holder.messageView.isTyping()
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

        mItems.addAll(0, mPendingMessagesToAdd)

        wrappedNotifyItemRangeInserted(0, mPendingMessagesToAdd.size)

        if (originalMessageCount > 0)
            wrappedNotifyItemRangeChanged(mPendingMessagesToAdd.size, mItems.size)

        mPendingMessagesToAdd.clear()
    }

    @UiThread
    fun addItem(message: UIMessage) {
        mItems.add(0, message)

        wrappedNotifyItemInserted(0)
        wrappedNotifyItemRangeChanged(0, mItems.size - 1)

        scrollToActiveMessage()
    }

    open fun scrollToActiveMessage() {
        mRecyclerView.scrollToPosition(0)
    }

    @UiThread
    fun addItems(messages: List<UIMessage>) {
        if (messages.isEmpty()) return

        onActiveElementDetached()

        val pendingMessages = if (mPendingMessagesToAdd.isEmpty()) {
            val lastMessage = messages.first()

            addItem(lastMessage)

            messages.drop(1)
        } else
            messages

        mPendingMessagesToAdd.addAll(pendingMessages)
    }

    @UiThread
    fun setItems(messages: List<UIMessage>) {
        if (messages.isEmpty()) return resetItems()

        val reversedMessages = messages.reversed()

        mLastActiveMessageHash = reversedMessages[0].hashCode()

        replaceItems(reversedMessages)

        wrappedNotifyDataSetChanged()
    }

    override fun onViewRecycled(holder: MessageViewHolder) {
        super.onViewRecycled(holder)

        if (holder is ActiveMessageViewHolder)
            mLastRecycledActiveMessageViewHolder = holder
    }

    override fun onLayoutCompleted() {
        if (mLastRecycledActiveMessageViewHolder == null
        || !mIsActiveMessageViewHolderAnimationInterrupted
        ) {
            return
        }

        onActiveElementDetached()

        mLastRecycledActiveMessageViewHolder = null
    }
}