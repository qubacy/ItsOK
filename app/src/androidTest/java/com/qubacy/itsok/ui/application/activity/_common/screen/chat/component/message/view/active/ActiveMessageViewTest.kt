package com.qubacy.itsok.ui.application.activity._common.screen.chat.component.message.view.active

import android.widget.ImageView
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.qubacy.itsok.ui.application.activity._common.screen._common.component._common.ViewTest
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith
import com.qubacy.itsok.R
import com.qubacy.itsok.ui._common._test.view.util.matcher.image.common.CommonImageViewMatcher
import com.qubacy.itsok.ui.application.activity._common.screen._common.data.message._test.util.UIMessageUtilGenerator
import kotlinx.coroutines.GlobalScope

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ActiveMessageViewTest : ViewTest<ActiveMessageView>() {
    override fun getViewLayoutResId(): Int {
        return R.layout.component_active_message
    }

    override fun setup() {
        super.setup()

    }

    override fun setupViewConfiguration() {
        super.setupViewConfiguration()

        mView.apply {
            setCoroutineScope(GlobalScope)
        }
    }

    @Test
    fun setTextMessageWithoutAnimationTest() {
        val message = UIMessageUtilGenerator.generateUIMessage("test")

        mActivityScenarioRule.scenario.onActivity {
            mView.setMessage(message, false)
        }

        Espresso.onView(withText(message.text))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun setImageMessageWithoutAnimationTest() {
        val drawable = InstrumentationRegistry.getInstrumentation().targetContext
            .getDrawable(R.drawable.itsok)!!
        val message = UIMessageUtilGenerator.generateUIMessage(image = drawable)

        mActivityScenarioRule.scenario.onActivity {
            mView.setMessage(message, false)
        }

        Espresso.onView(ViewMatchers.isAssignableFrom(ImageView::class.java))
            .check(ViewAssertions.matches(CommonImageViewMatcher(drawable)))
    }


}