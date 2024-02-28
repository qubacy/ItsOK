package com.qubacy.itsok.ui.application.activity._common.screen.settings.general.model.module

import com.qubacy.itsok.data.error.repository.ErrorDataRepository
import com.qubacy.itsok.ui.application.activity._common.screen.settings.general.model.GeneralSettingsViewModelFactory
import com.qubacy.itsok.ui.application.activity._common.screen.settings.general.model.GeneralSettingsViewModelFactoryQualifier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object GeneralSettingsViewModelModule {
    @Provides
    @GeneralSettingsViewModelFactoryQualifier
    fun provideGeneralSettingsViewModelFactory(
        errorDataRepository: ErrorDataRepository
    ): GeneralSettingsViewModelFactory {
        return GeneralSettingsViewModelFactory(errorDataRepository)
    }
}