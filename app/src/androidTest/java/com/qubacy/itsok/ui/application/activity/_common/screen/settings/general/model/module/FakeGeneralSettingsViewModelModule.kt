package com.qubacy.itsok.ui.application.activity._common.screen.settings.general.model.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.stateful.model.factory.FakeStatefulViewModelFactory
import com.qubacy.itsok.ui.application.activity._common.screen.settings.general.model.GeneralSettingsViewModel
import com.qubacy.itsok.ui.application.activity._common.screen.settings.general.model.GeneralSettingsViewModelFactoryQualifier
import com.qubacy.itsok.ui.application.activity._common.screen.settings.general.model.state.GeneralSettingsUiState
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.testing.TestInstallIn
import org.mockito.Mockito

@Module
@TestInstallIn(
    components = [ActivityRetainedComponent::class],
    replaces = [GeneralSettingsViewModelModule::class]
)
object FakeGeneralSettingsViewModelModule {
    class FakeGeneralSettingsViewModelFactory(

    ) : FakeStatefulViewModelFactory<GeneralSettingsUiState>() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val viewModel = super.create(modelClass) as GeneralSettingsViewModel

            // todo: rethink this one:
            Mockito.`when`(viewModel.uiState).thenReturn(GeneralSettingsUiState())

            return viewModel as T
        }
    }

    @Provides
    @GeneralSettingsViewModelFactoryQualifier
    fun provideGeneralSettingsViewModelFactory(
    ): ViewModelProvider.Factory {
        return FakeGeneralSettingsViewModelFactory()
    }
}