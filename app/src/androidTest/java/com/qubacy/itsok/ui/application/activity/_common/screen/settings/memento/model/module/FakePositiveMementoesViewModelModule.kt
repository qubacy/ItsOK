package com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.qubacy.itsok._common._test.util.mock.AnyMockUtil
import com.qubacy.itsok.domain.settings.memento.model.Memento
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.stateful.model.factory.FakeStatefulViewModelFactory
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.PositiveMementoesViewModel
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.PositiveMementoesViewModelFactoryQualifier
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.state.PositiveMementoesUiState
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.state.TestPositiveMementoesUiState
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

    ) : FakeStatefulViewModelFactory<PositiveMementoesUiState>() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val viewModel = super.create(modelClass) as PositiveMementoesViewModel
            val uiState = TestPositiveMementoesUiState()

            Mockito.`when`(viewModel.uiState).thenReturn(uiState)
            Mockito.`when`(viewModel.createMemento(AnyMockUtil.anyObject())).thenAnswer {
                val mementoToCreate = it.arguments[0] as Memento

                uiState.mementoToCreate = mementoToCreate

                Unit
            }
            Mockito.`when`(viewModel.updateMemento(AnyMockUtil.anyObject())).thenAnswer {
                val mementoToUpdate = it.arguments[0] as Memento

                uiState.mementoToUpdate = mementoToUpdate

                Unit
            }
            Mockito.`when`(viewModel.removeMemento(Mockito.anyLong())).thenAnswer {
                val mementoToRemoveId = it.arguments[0] as Long

                uiState.mementoToRemoveId = mementoToRemoveId

                Unit
            }

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