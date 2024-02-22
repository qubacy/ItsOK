package com.qubacy.itsok.ui.application.activity._common.screen.chat.model.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.factory.FakeBaseViewModelFactory
import com.qubacy.itsok.ui.application.activity._common.screen.chat.model.ChatViewModel
import com.qubacy.itsok.ui.application.activity._common.screen.chat.model.ChatViewModelFactoryQualifier
import com.qubacy.itsok.ui.application.activity._common.screen.chat.model.state.ChatUiState
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.testing.TestInstallIn
import org.mockito.Mockito

@Module
@TestInstallIn(
    components = [ActivityRetainedComponent::class],
    replaces = [ChatViewModelModule::class]
)
object FakeChatViewModelModule {
    class FakeChatViewModelFactory(

    ) : FakeBaseViewModelFactory<ChatUiState>() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val viewModel = super.create(modelClass) as ChatViewModel

            // todo: rethink this one:
            Mockito.`when`(viewModel.uiState).thenReturn(ChatUiState(error = null))

            return viewModel as T
        }
    }

    @Provides
    @ChatViewModelFactoryQualifier
    fun provideChatViewModelFactory(
    ): ViewModelProvider.Factory {
        return FakeChatViewModelFactory()
    }
}