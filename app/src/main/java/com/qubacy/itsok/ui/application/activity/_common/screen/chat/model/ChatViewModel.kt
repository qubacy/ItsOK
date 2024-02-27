package com.qubacy.itsok.ui.application.activity._common.screen.chat.model

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qubacy.itsok._common.chat.stage.ChatStage
import com.qubacy.itsok.domain._common.usecase._common.result._common.DomainResult
import com.qubacy.itsok.domain.chat.ChatUseCase
import com.qubacy.itsok.domain.chat.result.GetNextMessagesDomainResult
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.BaseViewModel
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.operation._common.UiOperation
import com.qubacy.itsok.ui.application.activity._common.screen.chat.model.operation.ChangeStageUiOperation
import com.qubacy.itsok.ui.application.activity._common.screen.chat.model.operation.NextMessagesUiOperation
import com.qubacy.itsok.ui.application.activity._common.screen.chat.model.state.ChatUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Qualifier

@HiltViewModel
open class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val mChatUseCase: ChatUseCase
) : BaseViewModel<ChatUiState>(savedStateHandle, mChatUseCase) {
    override var mUiState: ChatUiState = ChatUiState(error = null, isLoading = false)
    companion object {
        const val TAG = "ChatViewModel"

        val GRIPE_REGEX = Regex("^\\S.*\$")
    }

    init {
        mUiState = savedStateHandle[UI_STATE_KEY] ?: mUiState
    }

    override fun onCleared() {
        mSavedStateHandle[UI_STATE_KEY] = mUiState

        super.onCleared()
    }

    override fun processDomainResultFlow(domainResult: DomainResult): UiOperation? {
        val uiOperation = super.processDomainResultFlow(domainResult)

        if (uiOperation != null) return uiOperation

        return when (domainResult::class) {
            GetNextMessagesDomainResult::class ->
                processGetNextMessagesDomainResult(domainResult as GetNextMessagesDomainResult)
            else -> null
        }
    }

    private fun processGetNextMessagesDomainResult(
        messagesResult: GetNextMessagesDomainResult
    ): UiOperation {
        changeLoadingState(false)
        updateStageAfterMessages()

        mUiState.messages = mUiState.messages.plus(messagesResult.messages)

        return NextMessagesUiOperation(messagesResult.messages)
    }

    private fun updateStageAfterMessages() {
        when (uiState.stage) {
            ChatStage.IDLE -> setStage(ChatStage.GRIPE)
            ChatStage.GRIPE -> {
                setStage(ChatStage.MEMENTO_OFFERING)
                getMementoMessages()
            }
            else -> { }
        }
    }

    private fun setStage(newStage: ChatStage) {
        mUiState.stage = newStage

        viewModelScope.launch {
            mUiOperationFlow.emit(ChangeStageUiOperation(newStage))
        }
    }

    open fun getIntroMessages() {
        changeLoadingState(true)
        mChatUseCase.getIntroMessages()
    }

    open fun isGripeValid(gripe: String): Boolean {
        return GRIPE_REGEX.matches(gripe)
    }

    open fun getGripeMessages() {
        changeLoadingState(true)
        mChatUseCase.getGripeMessages()
    }

    open fun getMementoMessages() {
        changeLoadingState(true)
        mChatUseCase.getMementoMessages()
    }

    open fun moveToBye() {
        setStage(ChatStage.BYE)
        mChatUseCase.getByeMessages()
    }

    open fun restart() {
        mUiState = ChatUiState(error = null, isLoading = false)

        setStage(ChatStage.IDLE)
        getIntroMessages()
    }
}

@Qualifier
annotation class ChatViewModelFactoryQualifier

class ChatViewModelFactory(
    private val mChatUseCase: ChatUseCase
) : AbstractSavedStateViewModelFactory() {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (!modelClass.isAssignableFrom(ChatViewModel::class.java))
            throw IllegalArgumentException()

        return ChatViewModel(handle, mChatUseCase) as T
    }
}