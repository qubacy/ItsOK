package com.qubacy.itsok.ui.application.activity._common.screen.chat.model.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.base._common.model.factory.FakeBaseViewModelFactory
import com.qubacy.itsok.ui.application.activity._common.screen.chat.model.ChatViewModel
import com.qubacy.itsok.ui.application.activity._common.screen.chat.model.ChatViewModelFactoryQualifier
import com.qubacy.itsok.ui.application.activity._common.screen.chat.model.state.ChatUiState
import com.qubacy.itsok.ui.application.activity._common.screen.chat.model.state.TestChatUiState
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
            val uiState = TestChatUiState()

            Mockito.`when`(viewModel.uiState).thenReturn(uiState)
            Mockito.`when`(viewModel.getIntroMessages()).thenAnswer {
                uiState.getIntroMessagesCalled = true

                Unit
            }
            Mockito.`when`(viewModel.getGripeMessages()).thenAnswer {
                uiState.getGripeMessagesCalled = true

                Unit
            }
            Mockito.`when`(viewModel.getMementoMessages()).thenAnswer {
                uiState.getMementoMessagesCalled = true

                Unit
            }
            Mockito.`when`(viewModel.moveToBye()).thenAnswer {
                uiState.moveToByeCalled = true

                Unit
            }
            Mockito.`when`(viewModel.restart()).thenAnswer {
                uiState.restartCalled = true

                Unit
            }
            Mockito.`when`(viewModel.isGripeValid(Mockito.anyString())).thenCallRealMethod()

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