package com.qubacy.itsok.ui.application.activity._common.screen.chat

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
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
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.operation.loading.SetLoadingStateUiOperation
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.util.extensional.closeSoftKeyboard
import com.qubacy.itsok.ui.application.activity._common.screen.chat._common.data.message.UIMessage
import com.qubacy.itsok.ui.application.activity._common.screen.chat.component.list.adapter.MessageListAdapter
import com.qubacy.itsok.ui.application.activity._common.screen.chat.component.list.layout.MessageListLayoutManager
import com.qubacy.itsok.ui.application.activity._common.screen.chat.model.ChatViewModel
import com.qubacy.itsok.ui.application.activity._common.screen.chat.model.ChatViewModelFactoryQualifier
import com.qubacy.itsok.ui.application.activity._common.screen.chat.model.operation.ChangeStageUiOperation
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
        mBinding.fragmentChatGripeInput.componentChatGripeInputText
            .setOnKeyListener { _, keyCode, keyEvent ->
                onGripeInputKeyPressed(keyCode, keyEvent)
            }
        mBinding.fragmentChatMementoButtons.apply {
            componentChatMementoButtonsButtonPositive.setOnClickListener {
                onPositiveMementoButtonClicked()
            }
            componentChatMementoButtonsButtonNegative.setOnClickListener {
                onNegativeMementoButtonClicked()
            }
        }
        mBinding.fragmentChatByeButtons.apply {
            componentChatByeButtonsButtonBye.setOnClickListener {
                onByeButtonClicked()
            }
            componentChatByeButtonsButtonRestart.setOnClickListener {
                onRestartButtonClicked()
            }
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
        mBinding.fragmentChatMementoButtons.apply {
            root.updatePadding(bottom = insets.bottom)
        }
        mBinding.fragmentChatByeButtons.apply {
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
            ChangeStageUiOperation::class ->
                processChangeStageUiOperation(uiOperation as ChangeStageUiOperation)
            else -> return false
        }

        return true
    }

    override fun processSetLoadingOperation(loadingOperation: SetLoadingStateUiOperation) {
        setLoadingState(loadingOperation.isLoading)
    }

    private fun processNextMessagesOperation(messagesOperation: NextMessagesUiOperation) {
        val resolvedMessages = resolveMessages(messagesOperation.messages)

        mAdapter.addItems(resolvedMessages)
    }

    private fun processChangeStageUiOperation(stageOperation: ChangeStageUiOperation) {
        setStage(stageOperation.stage)
    }

    private fun initChat() {
        mModel.getIntroMessages()
    }

    private fun onGripeInputKeyPressed(keyCode: Int, keyEvent: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_DOWN) {
            onGripeEnterPressed()

            return true
        }

        return false
    }

    private fun onGripeEnterPressed() {
        val gripeText = mBinding.fragmentChatGripeInput.componentChatGripeInputText.text.toString()

        if (!mModel.isGripeValid(gripeText))
            return onPopupMessageOccurred(R.string.fragment_chat_error_invalid_gripe)

        mBinding.fragmentChatGripeInput.componentChatGripeInputText.text?.clear()
        closeSoftKeyboard()

        applyGripe(gripeText)
    }

    private fun applyGripe(gripe: String) {
        mModel.getGripeMessages()
    }

    private fun onPositiveMementoButtonClicked() {
        mModel.moveToBye()
    }

    private fun onNegativeMementoButtonClicked() {
        mModel.getMementoMessages()
    }

    private fun onByeButtonClicked() {
        requireActivity().finishAndRemoveTask()
    }

    private fun onRestartButtonClicked() {
        mAdapter.resetItems()
        mModel.restart()
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
        mBinding.fragmentChatGripeInput.root.visibility =
            if (stage == ChatStage.GRIPE) View.VISIBLE else View.GONE
        mBinding.fragmentChatMementoButtons.root.visibility =
            if (stage == ChatStage.MEMENTO_OFFERING) View.VISIBLE else View.GONE
        mBinding.fragmentChatByeButtons.root.visibility =
            if (stage == ChatStage.BYE) View.VISIBLE else View.GONE
    }

    override fun setLoadingState(isLoading: Boolean) {
        Log.d(TAG, "setLoadingState(): isLoading = $isLoading;")

        setAvatarAppearanceWithLoadingState(isLoading)
        setControlsEnabled(!isLoading)
    }

    private fun setControlsEnabled(isEnabled: Boolean) {
        mBinding.fragmentChatGripeInput.root.isEnabled = isEnabled
        mBinding.fragmentChatMementoButtons
            .componentChatMementoButtonsButtonPositive.isEnabled = isEnabled
        mBinding.fragmentChatMementoButtons
            .componentChatMementoButtonsButtonNegative.isEnabled = isEnabled
        mBinding.fragmentChatByeButtons
            .componentChatByeButtonsButtonBye.isEnabled = isEnabled
        mBinding.fragmentChatByeButtons
            .componentChatByeButtonsButtonRestart.isEnabled = isEnabled
    }

    private fun setAvatarAppearanceWithStage(stage: ChatStage) {
        val nextDrawableIdRes = getNextAvatarDrawableWithStage(stage)

        if (nextDrawableIdRes == mPrevAvatarDrawableResId) return
        else runBackwardsAvatarAnimation { runAvatarAnimationDrawable(nextDrawableIdRes) }
    }

    private fun setAvatarAppearanceWithLoadingState(isLoading: Boolean) {
        runBackwardsAvatarAnimation {
            if (isLoading && mPrevAvatarDrawableResId != getLoadingAvatarDrawableResId())
                runLoadingAvatarAnimation()
            else setAvatarAppearanceWithStage(mModel.uiState.stage)
        }
    }

    private fun getLoadingAvatarDrawableResId(): Int {
        return R.drawable.itsok_animated_thinking
    }

    private fun runLoadingAvatarAnimation() {
        val loadingDrawableResId = getLoadingAvatarDrawableResId()

        runAvatarAnimationDrawable(loadingDrawableResId)
    }

    private fun runAvatarAnimationDrawable(@DrawableRes drawableResId: Int) {
        val nextDrawable = getAnimatedVectorDrawableByDrawableResId(drawableResId)

        runAvatarAnimatedVectorDrawable(nextDrawable)

        mPrevAvatarDrawableResId = drawableResId
    }

    private fun getNextAvatarDrawableWithStage(stage: ChatStage): Int {
        return when (stage) {
            ChatStage.IDLE -> R.drawable.itsok_animated_thinking
            ChatStage.GRIPE -> R.drawable.itsok_animated_wonder
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