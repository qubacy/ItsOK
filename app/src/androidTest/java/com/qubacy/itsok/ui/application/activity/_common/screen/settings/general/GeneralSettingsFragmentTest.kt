package com.qubacy.itsok.ui.application.activity._common.screen.settings.general

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.qubacy.itsok.ui._common._test.view.util.matcher.button.navigation.NavigationButtonViewMatcher
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.BaseFragmentTest
import com.qubacy.itsok.ui.application.activity._common.screen.settings.general.model.GeneralSettingsViewModel
import com.qubacy.itsok.ui.application.activity._common.screen.settings.general.model.module.GeneralSettingsViewModelModule
import com.qubacy.itsok.ui.application.activity._common.screen.settings.general.model.state.GeneralSettingsUiState
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Test
import org.junit.runner.RunWith
import com.qubacy.itsok.R
import org.junit.Assert

@HiltAndroidTest
@UninstallModules(GeneralSettingsViewModelModule::class)
@RunWith(AndroidJUnit4::class)
class GeneralSettingsFragmentTest(

) : BaseFragmentTest<
    GeneralSettingsUiState, GeneralSettingsViewModel, GeneralSettingsFragment
>() {
    override fun getFragmentClass(): Class<GeneralSettingsFragment> {
        return GeneralSettingsFragment::class.java
    }

    @Test
    fun clickingOnNavigateUpButtonLeadsToTransitionToChatFragment() {
        Espresso.onView(NavigationButtonViewMatcher(R.id.fragment_general_settings_top_bar))
            .perform(ViewActions.click())

        val gottenDestination = mNavController.currentDestination!!.id

        Assert.assertEquals(R.id.chatFragment, gottenDestination)
    }
}