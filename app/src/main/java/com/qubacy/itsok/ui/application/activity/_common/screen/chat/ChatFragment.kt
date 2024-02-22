package com.qubacy.itsok.ui.application.activity._common.screen.chat

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.core.graphics.Insets
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.vectordrawable.graphics.drawable.Animatable2Compat.AnimationCallback
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.qubacy.itsok.R
import com.qubacy.itsok.databinding.FragmentChatBinding
import com.qubacy.itsok.domain.chat.model.Message
import com.qubacy.itsok._common.chat.stage.ChatStage
import com.qubacy.itsok.domain.chat.model.toUIMessage
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.BaseFragment
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.operation._common.UiOperation
import com.qubacy.itsok.ui.application.activity._common.screen.chat._common.data.message.UIMessage
import com.qubacy.itsok.ui.application.activity._common.screen.chat.component.list.adapter.MessageListAdapter
import com.qubacy.itsok.ui.application.activity._common.screen.chat.component.list.layout.MessageListLayoutManager
import com.qubacy.itsok.ui.application.activity._common.screen.chat.model.ChatViewModel
import com.qubacy.itsok.ui.application.activity._common.screen.chat.model.ChatViewModelFactoryQualifier
import com.qubacy.itsok.ui.application.activity._common.screen.chat.model.operation.NextMessagesUiOperation
import com.qubacy.itsok.ui.application.activity._common.screen.chat.model.state.ChatUiState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment(

) : BaseFragment<ChatUiState, ChatViewModel>() {
    companion object {
        const val TAG = "ChatFragment"
    }

    @Inject
    @ChatViewModelFactoryQualifier
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val mModel: ChatViewModel by viewModels(
        factoryProducer = { viewModelFactory }
    )

    private lateinit var mBinding: FragmentChatBinding
    private lateinit var mAdapter: MessageListAdapter

    @DrawableRes
    private var mPrevAvatarDrawableResId: Int = R.drawable.itsok

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentChatBinding.inflate(inflater, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = MessageListAdapter(lifecycleScope)

        mBinding.fragmentChatMessageList.apply {
            layoutManager = MessageListLayoutManager(
                requireContext(), LinearLayout.VERTICAL, true)
            adapter = mAdapter
        }
    }

    override fun onResume() {
        super.onResume()

        if (mModel.uiState.messages.isEmpty()) initChat()
    }

    override fun viewInsetsToCatch(): Int {
        return (super.viewInsetsToCatch() or WindowInsetsCompat.Type.ime())
    }

    override fun adjustViewToInsets(insets: Insets) {
        super.adjustViewToInsets(insets)

        mBinding.fragmentChatTopBarWrapper.apply {
            updatePadding(top = insets.top)
        }
        mBinding.fragmentChatGripeInput.apply {
            root.updatePadding(bottom = insets.bottom)
        }
    }

    override fun runInitWithUiState(uiState: ChatUiState) {
        super.runInitWithUiState(uiState)

        setChatMessages(uiState.messages)
        setStage(uiState.stage)
    }

    override fun processUiOperation(uiOperation: UiOperation): Boolean {
        if (super.processUiOperation(uiOperation)) return true

        when (uiOperation::class) {
            NextMessagesUiOperation::class ->
                processNextMessagesOperation(uiOperation as NextMessagesUiOperation)
            else -> return false
        }

        return true
    }

    private fun processNextMessagesOperation(messagesOperation: NextMessagesUiOperation) {
        val resolvedMessages = resolveMessages(messagesOperation.messages)

        mAdapter.addItems(resolvedMessages)

        return
    }

    private fun initChat() {
        mModel.getNextMessages()
    }

    private fun resolveMessages(messages: List<Message>): List<UIMessage> {
        val context = requireContext()

        return messages.map { it.toUIMessage(context) }
    }

    private fun setChatMessages(messages: List<Message>) {
        val resolvedMessages = resolveMessages(messages)

        mAdapter.setItems(resolvedMessages)
    }

    private fun setStage(stage: ChatStage) {
        setControlsWithStage(stage)
        setAvatarAppearanceWithStage(stage)
    }

    private fun setControlsWithStage(stage: ChatStage) {
        when (stage) {
            ChatStage.IDLE -> {
                mBinding.fragmentChatGripeInput.root.visibility = View.VISIBLE
            }
            ChatStage.THINKING -> {
                // todo: mb it has to block all the controls?
            }
            ChatStage.MEMENTO_OFFERING -> {
                mBinding.fragmentChatGripeInput.root.visibility = View.GONE
            }
            ChatStage.BYE -> {
                mBinding.fragmentChatGripeInput.root.visibility = View.GONE
            }
        }
    }

    private fun setAvatarAppearanceWithStage(stage: ChatStage) {
        runBackwardsAvatarAnimation {
            runNextAvatarAnimationWithStage(stage)
        }
    }

    private fun runNextAvatarAnimationWithStage(stage: ChatStage) {
        val nextDrawableIdRes = getNextAvatarDrawableWithStage(stage)
        val nextDrawable = getAnimatedVectorDrawableByDrawableResId(nextDrawableIdRes)

        runAvatarAnimatedVectorDrawable(nextDrawable)

        mPrevAvatarDrawableResId = nextDrawableIdRes
    }

    private fun getNextAvatarDrawableWithStage(stage: ChatStage): Int {
        return when (stage) {
            ChatStage.IDLE -> R.drawable.itsok_animated_wonder
            ChatStage.THINKING -> R.drawable.itsok_animated_thinking
            ChatStage.MEMENTO_OFFERING -> R.drawable.itsok_animated_happy_memento
            ChatStage.BYE -> R.drawable.itsok_animated_happy_bye
        }
    }

    private fun runBackwardsAvatarAnimation(endAction: () -> Unit) {
        val backwardsDrawableResId =
            getBackwardsAvatarDrawableWithDrawable(mPrevAvatarDrawableResId)

        if (backwardsDrawableResId == null) {
            endAction.invoke()

            return
        }

        val backwardsDrawable = getAnimatedVectorDrawableByDrawableResId(backwardsDrawableResId)

        runAvatarAnimatedVectorDrawable(backwardsDrawable, endAction)
    }

    private fun getBackwardsAvatarDrawableWithDrawable(@DrawableRes drawable: Int): Int? {
        return when (drawable) {
            R.drawable.itsok -> null
            R.drawable.itsok_animated_wonder -> R.drawable.itsok_animated_wonder_backwards
            R.drawable.itsok_animated_thinking -> R.drawable.itsok_animated_thinking_backwards
            R.drawable.itsok_animated_happy_memento -> R.drawable.itsok_animated_happy_memento_backwards
            R.drawable.itsok_animated_happy_bye -> R.drawable.itsok_animated_happy_bye_backwards
            else -> throw IllegalStateException()
        }
    }

    private fun runAvatarAnimatedVectorDrawable(
        drawable: AnimatedVectorDrawableCompat, endAction: (() -> Unit)? = null
    ) {
        mBinding.fragmentChatImageAvatar.setImageDrawable(drawable)

        drawable.registerAnimationCallback(object : AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) {
                endAction?.invoke()
            }
        })

        drawable.start()
    }

    private fun getAnimatedVectorDrawableByDrawableResId(
        @DrawableRes drawableResId: Int
    ): AnimatedVectorDrawableCompat {
        return AnimatedVectorDrawableCompat.create(requireContext(), drawableResId)!!
    }
}