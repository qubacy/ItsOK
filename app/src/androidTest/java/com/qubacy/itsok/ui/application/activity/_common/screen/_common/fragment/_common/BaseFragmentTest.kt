package com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common

import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelLazy
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.NoActivityResumedException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.qubacy.itsok.R
import com.qubacy.itsok._common._test.util.launcher.launchFragmentInHiltContainer
import com.qubacy.itsok._common.error.FakeError
import com.qubacy.itsok.ui._common._test.view.util.matcher.toast.root.ToastRootMatcher
import com.qubacy.itsok.ui.application.activity._common.HiltTestActivity
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.BaseViewModel
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.operation._common.UiOperation
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.operation.error.ErrorUiOperation
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.state.BaseUiState
import dagger.hilt.android.testing.HiltAndroidRule
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Field

abstract class BaseFragmentTest<
    UiStateType : BaseUiState,
    ViewModelType : BaseViewModel<UiStateType>,
    FragmentType : BaseFragment<UiStateType, ViewModelType>
> {
    companion object {
        const val TEST_MESSAGE = "test message"
    }

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    protected lateinit var mActivityScenario: ActivityScenario<HiltTestActivity>
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
        mActivityScenario = launchFragmentInHiltContainer(fragmentClass = getFragmentClass()) {
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

        Espresso.onView(withText(FakeError.normal.message))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(withText(R.string.component_error_dialog_button_neutral_caption))
            .perform(click())
        Espresso.onView(withText(FakeError.normal.message))
            .check(ViewAssertions.doesNotExist())
    }

    @Test
    fun handleCriticalErrorTest() = runTest {
        val errorOperation = ErrorUiOperation(FakeError.critical)

        postUiOperation(errorOperation)

        Espresso.onView(withText(FakeError.critical.message))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(withText(R.string.component_error_dialog_button_neutral_caption))
            .perform(click())

        try { Espresso.onView(isRoot()).check(ViewAssertions.doesNotExist()) }
        catch (_: NoActivityResumedException) { }
    }

    @Test
    fun showMessageTest() = runTest {
        val onPopupMessageOccurredMethodReflection = BaseFragment::class.java
            .getDeclaredMethod(
                "onPopupMessageOccurred", String::class.java, Int::class.java)
            .apply { isAccessible = true }

        mActivityScenario.onActivity {
            onPopupMessageOccurredMethodReflection.invoke(
                mFragment, TEST_MESSAGE, Toast.LENGTH_SHORT)
        }

        Espresso.onView(withText(TEST_MESSAGE))
            .inRoot(RootMatchers.withDecorView(ToastRootMatcher(mFragment.requireActivity())))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun operationFlowCollectedOnceAfterFragmentRestartTest() {
        val expectedCollectorCount = 1

        mActivityScenario.moveToState(Lifecycle.State.CREATED)
        mActivityScenario.moveToState(Lifecycle.State.STARTED)

        val gottenCollectorCount = Class
            .forName("kotlinx.coroutines.flow.internal.AbstractSharedFlow")
            .getDeclaredField("nCollectors")
            .apply { isAccessible = true }.get(mUiOperationFlow)

        Assert.assertEquals(expectedCollectorCount, gottenCollectorCount)
    }
}