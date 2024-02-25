package com.qubacy.itsok.ui.application.activity._common.screen._common.component._common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.view.get
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.qubacy.itsok.ui.application.activity._common.HiltTestActivity
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.Before
import org.junit.Rule
import org.junit.rules.RuleChain

abstract class ViewTest<ViewType : View> {
    protected val mActivityScenarioRule = ActivityScenarioRule(HiltTestActivity::class.java)

    @JvmField
    @Rule
    val ruleChain = RuleChain
        .outerRule(HiltAndroidRule(this))
        .around(mActivityScenarioRule)

    protected lateinit var mView: ViewType

    @LayoutRes
    protected abstract fun getViewLayoutResId(): Int

    @Before
    open fun setup() {
        initView()
    }

    private fun initView() {
        mActivityScenarioRule.scenario.onActivity {
            val viewLayoutResId = getViewLayoutResId()
            val container = it.findViewById<ViewGroup>(android.R.id.content)

            mView = (LayoutInflater.from(it)
                .inflate(viewLayoutResId, container, true) as ViewGroup)[0] as ViewType
        }

        setupViewConfiguration()

        mActivityScenarioRule.scenario.moveToState(Lifecycle.State.RESUMED)
    }

    protected open fun setupViewConfiguration() { }
}