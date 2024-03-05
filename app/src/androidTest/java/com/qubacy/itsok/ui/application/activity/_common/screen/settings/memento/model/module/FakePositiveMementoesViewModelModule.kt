package com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.base._common.model.factory.FakeBaseViewModelFactory
import com.qubacy.itsok.ui.application.activity._common.screen.chat.model.ChatViewModel
import com.qubacy.itsok.ui.application.activity._common.screen.chat.model.state.ChatUiState
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.PositiveMementoesViewModel
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.PositiveMementoesViewModelFactoryQualifier
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.state.PositiveMementoesUiState
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.testing.TestInstallIn
import org.mockito.Mockito

@Module
@TestInstallIn(
    components = [ActivityRetainedComponent::class],
    replaces = [PositiveMementoesViewModelModule::class]
)
object FakePositiveMementoesViewModelModule {
    class FakePositiveMementoesViewModelFactory(

    ) : FakeBaseViewModelFactory<PositiveMementoesUiState>() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val viewModel = super.create(modelClass) as PositiveMementoesViewModel

            // todo: rethink this one:
            Mockito.`when`(viewModel.uiState).thenReturn(PositiveMementoesUiState())

            return viewModel as T
        }
    }

    @PositiveMementoesViewModelFactoryQualifier
    @Provides
    fun providePositiveMementoesViewModelFactory(

    ): ViewModelProvider.Factory {
        return FakePositiveMementoesViewModelFactory()
    }
}