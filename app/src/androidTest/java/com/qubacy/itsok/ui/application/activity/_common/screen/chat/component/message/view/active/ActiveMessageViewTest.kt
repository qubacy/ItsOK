package com.qubacy.itsok.ui.application.activity._common.screen.chat.component.message.view.active

import android.widget.ImageView
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.qubacy.itsok.ui.application.activity._common.screen._common.component._common.ViewTest
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith
import com.qubacy.itsok.R
import com.qubacy.itsok.ui._common._test.view.util.action.wait.WaitViewAction
import com.qubacy.itsok.ui._common._test.view.util.matcher.image.common.CommonImageViewMatcher
import com.qubacy.itsok.ui.application.activity._common.screen._common.data.message._test.util.UIMessageUtilGenerator
import com.qubacy.itsok.ui.application.activity._common.screen.chat.component.typing.view.TypingMaterialTextView
import kotlinx.coroutines.GlobalScope
import org.hamcrest.Matchers
import org.junit.Assert

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

    @Test
    fun setTextImageMessageWithoutAnimationTest() {
        val drawable = InstrumentationRegistry.getInstrumentation().targetContext
            .getDrawable(R.drawable.itsok)!!
        val message = UIMessageUtilGenerator.generateUIMessage("test", drawable)

        mActivityScenarioRule.scenario.onActivity {
            mView.setMessage(message, false)
        }

        Espresso.onView(withText(message.text))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.isAssignableFrom(ImageView::class.java))
            .check(ViewAssertions.matches(CommonImageViewMatcher(drawable)))
    }

    @Deprecated("Could be synchronized much better using TypingMaterialTextViewCallback.")
    @Test
    fun setTextMessageWithAnimationTest() {
        val message = UIMessageUtilGenerator.generateUIMessage("test")

        mActivityScenarioRule.scenario.onActivity {
            mView.setMessage(message, true)
        }

        Espresso.onView(withText(message.text)).check(ViewAssertions.doesNotExist())

        val charTypingAnimationDuration = getCharTypingAnimationDuration()
        val textTypingAnimationDuration = charTypingAnimationDuration * message.text!!.length

        Espresso.onView(isRoot()).perform(WaitViewAction(textTypingAnimationDuration))
        Espresso.onView(withText(message.text))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Deprecated("Could be synchronized much better using TypingMaterialTextViewCallback.")
    @Test
    fun setImageMessageWithAnimationTest() {
        val drawable = InstrumentationRegistry.getInstrumentation().targetContext
            .getDrawable(R.drawable.itsok)!!
        val message = UIMessageUtilGenerator.generateUIMessage(image = drawable)

        mActivityScenarioRule.scenario.onActivity {
            mView.setMessage(message, true)
        }

        Espresso.onView(ViewMatchers.isAssignableFrom(ImageView::class.java))
            .check(ViewAssertions.matches(Matchers.allOf(
                CommonImageViewMatcher(drawable),
                Matchers.not(ViewMatchers.withAlpha(1f))
            )))
        Espresso.onView(isRoot()).perform(WaitViewAction(
            ActiveMessageView.DEFAULT_IMAGE_APPEAR_ANIMATION_DURATION))
        Espresso.onView(ViewMatchers.isAssignableFrom(ImageView::class.java))
            .check(ViewAssertions.matches(Matchers.allOf(
                CommonImageViewMatcher(drawable),
                ViewMatchers.withAlpha(1f)
            )))
    }

    @Deprecated("Could be synchronized much better using TypingMaterialTextViewCallback.")
    @Test
    fun setTextImageMessageWithAnimationTest() {
        val drawable = InstrumentationRegistry.getInstrumentation().targetContext
            .getDrawable(R.drawable.itsok)!!
        val message = UIMessageUtilGenerator.generateUIMessage("test", drawable)

        mActivityScenarioRule.scenario.onActivity {
            mView.setMessage(message, true)
        }

        Espresso.onView(withText(message.text)).check(ViewAssertions.doesNotExist())
        Espresso.onView(ViewMatchers.isAssignableFrom(ImageView::class.java))
            .check(ViewAssertions.doesNotExist())

        val charTypingAnimationDuration = getCharTypingAnimationDuration()
        val textTypingAnimationDuration = charTypingAnimationDuration * message.text!!.length
        val totalAnimationDuration = textTypingAnimationDuration +
            ActiveMessageView.DEFAULT_IMAGE_APPEAR_ANIMATION_DURATION

        Espresso.onView(isRoot()).perform(WaitViewAction(totalAnimationDuration))
        Espresso.onView(withText(message.text))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.isAssignableFrom(ImageView::class.java))
            .check(ViewAssertions.matches(Matchers.allOf(
                CommonImageViewMatcher(drawable),
                ViewMatchers.withAlpha(1f)
            )))
    }

    @Test
    fun isTypingTest() {
        Assert.assertFalse(mView.isTyping())

        val message = UIMessageUtilGenerator.generateUIMessage("test")

        mActivityScenarioRule.scenario.onActivity {
            mView.setMessage(message, true)
        }

        val charTypingAnimationDuration = getCharTypingAnimationDuration()
        val textTypingAnimationDuration = charTypingAnimationDuration * message.text!!.length

        Assert.assertTrue(mView.isTyping())

        Espresso.onView(isRoot()).perform(WaitViewAction(textTypingAnimationDuration))

        Assert.assertFalse(mView.isTyping())
    }

    @Test
    fun stopTypingTest() {
        val message = UIMessageUtilGenerator.generateUIMessage(
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor " +
                "incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis " +
                "nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
        )

        mActivityScenarioRule.scenario.onActivity {
            mView.setMessage(message, true)
            mView.stopTyping()
        }

        Espresso.onView(withText(message.text))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    private fun getCharTypingAnimationDuration(): Long {
        var charTypingAnimationDuration = TypingMaterialTextView.DEFAULT_CHAR_TYPING_DURATION

        mActivityScenarioRule.scenario.onActivity {
            charTypingAnimationDuration =
                it.findViewById<TypingMaterialTextView>(
                    R.id.component_active_message_text)!!.getChatTypingDuration()
        }

        return charTypingAnimationDuration
    }
}