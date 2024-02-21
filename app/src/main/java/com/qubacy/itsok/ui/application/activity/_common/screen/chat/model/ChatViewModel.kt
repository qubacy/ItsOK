package com.qubacy.itsok.ui.application.activity._common.screen.chat.model

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.qubacy.itsok.domain._common.usecase._common.result.ErrorResult
import com.qubacy.itsok.domain._common.usecase._common.result.SuccessfulResult
import com.qubacy.itsok.domain.chat.ChatUseCase
import com.qubacy.itsok.domain.chat.model.Message
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.BaseViewModel
import com.qubacy.itsok.ui.application.activity._common.screen.chat.model.state.ChatUiState
import com.qubacy.itsok.domain._common.usecase._common.result._common.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Qualifier

@HiltViewModel
open class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val mChatUseCase: ChatUseCase
) : BaseViewModel<ChatUiState>(savedStateHandle, mChatUseCase) {
    override var mUiState: ChatUiState = ChatUiState(error = null)

    init {
        mUiState = savedStateHandle[UI_STATE_KEY] ?: mUiState
    }

    override fun onCleared() {
        mSavedStateHandle[UI_STATE_KEY] = mUiState

        super.onCleared()
    }

    open fun getNextMessages(): LiveData<Result<List<Message>>> {
        return mChatUseCase.getNextMessageWithStage(mUiState.stage).map {
            if (it is ErrorResult<List<Message>>) {
                mUiState.error = it.error
            } else {
                val resultMessages = (it as SuccessfulResult<List<Message>>).data
                val messages = mUiState.messages.plus(resultMessages)

                mUiState.messages = messages
            }

            it
        }
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