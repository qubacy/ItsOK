package com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.base

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.qubacy.itsok.R
import com.qubacy.itsok._common._test.util.launcher.launchFragmentInHiltContainer
import com.qubacy.itsok.ui._common._test.view.util.matcher.toast.root.ToastRootMatcher
import com.qubacy.itsok.ui.application.activity._common.HiltTestActivity
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.BaseFragment
import dagger.hilt.android.testing.HiltAndroidRule
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Field

abstract class BaseFragmentTest<FragmentType : BaseFragment> {
    companion object {
        const val TEST_MESSAGE = "test message"
    }

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    protected lateinit var mActivityScenario: ActivityScenario<HiltTestActivity>
    protected lateinit var mFragment: FragmentType
    protected lateinit var mNavController: TestNavHostController

    @Before
    open fun setup() {
        mNavController = TestNavHostController(ApplicationProvider.getApplicationContext())

        initFragment()
    }

    abstract fun getFragmentClass(): Class<FragmentType>

    private fun retrieveModelFieldReflection(): Field {
        return getFragmentClass()
            .getDeclaredField("mModel\$delegate")
            .apply { isAccessible = true }
    }

    private fun initFragment() {
        mActivityScenario = launchFragmentInHiltContainer(fragmentClass = getFragmentClass()) {
            initFragmentOnActivity(this)
        }
    }

    protected open fun initFragmentOnActivity(fragment: Fragment) {
        mNavController.setGraph(R.navigation.nav_graph)
        Navigation.setViewNavController(fragment.requireView(), mNavController)

        mFragment = fragment as FragmentType
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
}