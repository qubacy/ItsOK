package com.qubacy.itsok.ui.application.activity._common.screen.chat.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import com.qubacy.itsok._common.chat.stage.ChatStage
import com.qubacy.itsok.domain.chat.ChatUseCase
import com.qubacy.itsok.domain.chat.model.Message
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.BaseViewModel
import com.qubacy.itsok.ui.application.activity._common.screen.chat.model.state.ChatUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Qualifier

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val mChatUseCase: ChatUseCase
) : BaseViewModel<ChatUiState>() {
    fun getNextMessages(): LiveData<List<Message>> {
        val stage = mUiState.value?.stage ?: ChatStage.IDLE

        return mChatUseCase.getNextMessageWithStage(stage).map {
            val messages = mUiState.value?.messages?.plus(it) ?: it

            mUiState.postValue(ChatUiState(messages = messages, error = null))

            it
        }
    }
}

@Qualifier
annotation class ChatViewModelFactoryQualifier

class ChatViewModelFactory(
    private val mChatUseCase: ChatUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(ChatViewModel::class.java))
            throw IllegalArgumentException()

        return ChatViewModel(mChatUseCase) as T
    }
}