package com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelLazy
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.qubacy.itsok.R
import com.qubacy.itsok._common._test.util.launcher.launchFragmentInHiltContainer
import com.qubacy.itsok._common.error.FakeError
import com.qubacy.itsok._common.error.type.FakeErrorType
import com.qubacy.itsok.ui._common._test.view.util.action.wait.WaitViewAction
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.BaseViewModel
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.operation._common.UiOperation
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.operation.error.ErrorUiOperation
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.state.BaseUiState
import dagger.hilt.android.testing.HiltAndroidRule
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
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

    protected lateinit var mUiOperationFlow: MutableSharedFlow<UiOperation>

    @Before
    open fun setup() {
        mNavController = TestNavHostController(ApplicationProvider.getApplicationContext())

        val mModelFieldReflection = retrieveModelFieldReflection()

        initFragment(mModelFieldReflection)
        initOperationFlow()
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

    private fun initOperationFlow() {
        mUiOperationFlow = mModel.uiOperationFlow as MutableSharedFlow
    }

    private suspend fun postUiOperation(uiOperation: UiOperation) {
        mUiOperationFlow.emit(uiOperation)
    }

    @Test
    fun handleNormalErrorTest() = runTest {
        val errorOperation = ErrorUiOperation(FakeError.normal)

        postUiOperation(errorOperation)

        Espresso.onView(isRoot()).perform(WaitViewAction(2000))
        Espresso.onView(withText(FakeError.normal.message))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun handleCriticalErrorTest() {
        // todo: implement..


    }
}