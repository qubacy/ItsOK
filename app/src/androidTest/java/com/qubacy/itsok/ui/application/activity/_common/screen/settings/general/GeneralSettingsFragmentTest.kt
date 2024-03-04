package com.qubacy.itsok.ui.application.activity._common.screen.settings.general

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.qubacy.itsok.ui._common._test.view.util.matcher.button.navigation.NavigationButtonViewMatcher
import com.qubacy.itsok.ui.application.activity._common.screen.settings.general.model.GeneralSettingsViewModel
import com.qubacy.itsok.ui.application.activity._common.screen.settings.general.model.module.GeneralSettingsViewModelModule
import com.qubacy.itsok.ui.application.activity._common.screen.settings.general.model.state.GeneralSettingsUiState
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Test
import org.junit.runner.RunWith
import com.qubacy.itsok.R
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.stateful.StatefulFragmentTest
import org.hamcrest.Matchers
import org.junit.Assert

@HiltAndroidTest
@UninstallModules(GeneralSettingsViewModelModule::class)
@RunWith(AndroidJUnit4::class)
class GeneralSettingsFragmentTest(

) : StatefulFragmentTest<
    GeneralSettingsUiState, GeneralSettingsViewModel, GeneralSettingsFragment
>() {
    override fun getFragmentClass(): Class<GeneralSettingsFragment> {
        return GeneralSettingsFragment::class.java
    }

    override fun getCurrentDestination(): Int {
        return R.id.generalSettingsFragment
    }

    @Test
    fun clickingOnNavigateUpButtonLeadsToTransitionToChatFragment() {
        Espresso.onView(NavigationButtonViewMatcher(R.id.component_settings_top_bar))
            .perform(ViewActions.click())

        val gottenDestination = mNavController.currentDestination!!.id

        Assert.assertEquals(R.id.chatFragment, gottenDestination)
    }

    @Test
    fun clickingOnPositiveMementoSettingMoreButtonLeadsToTransitionToPositiveMementoesFragmentTest() {
        Espresso.onView(Matchers.allOf(
            withId(R.id.component_setting_button),
            isDescendantOfA(withId(R.id.fragment_general_settings_main_section_positive_mementoes))
        )).perform(ViewActions.click())

        val gottenDestination = mNavController.currentDestination!!.id

        Assert.assertEquals(R.id.positiveMementoesFragment, gottenDestination)
    }
}