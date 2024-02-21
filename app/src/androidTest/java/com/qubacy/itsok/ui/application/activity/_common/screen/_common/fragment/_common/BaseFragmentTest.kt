package com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelLazy
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.qubacy.itsok.R
import com.qubacy.itsok._common._test.util.launcher.launchFragmentInHiltContainer
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.BaseViewModel
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.state.BaseUiState
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Field

abstract class BaseFragmentTest<
    UiStateType : BaseUiState,
    ViewModelType : BaseViewModel<UiStateType>,
    FragmentType : BaseFragment<UiStateType, ViewModelType>
> {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    protected lateinit var mFragment: FragmentType
    protected lateinit var mModel: ViewModelType
    protected lateinit var mNavController: TestNavHostController

    @Before
    open fun setup() {
        mNavController = TestNavHostController(ApplicationProvider.getApplicationContext())

        val mModelFieldReflection = retrieveModelFieldReflection()

        initFragment(mModelFieldReflection)
    }

    abstract fun getFragmentClass(): Class<FragmentType>

    private fun retrieveModelFieldReflection(): Field {
        return getFragmentClass()
            .getDeclaredField("mModel\$delegate")
            .apply { isAccessible = true }
    }

    private fun initFragment(modelFieldReflection: Field) {
        launchFragmentInHiltContainer(fragmentClass = getFragmentClass()) {
            mNavController.setGraph(R.navigation.nav_graph)
            Navigation.setViewNavController(requireView(), mNavController)

            mFragment = this as FragmentType
            mModel = (modelFieldReflection.get(mFragment) as ViewModelLazy<ViewModelType>).value
        }
    }

    @Test
    fun handleNormalErrorTest() {
        // todo: implement..


    }

    @Test
    fun handleCriticalErrorTest() {
        // todo: implement..


    }
}