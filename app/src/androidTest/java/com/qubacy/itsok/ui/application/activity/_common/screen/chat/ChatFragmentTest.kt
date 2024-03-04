package com.qubacy.itsok.ui.application.activity._common.screen.chat

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.qubacy.itsok.domain.chat.model._test.util.MessageUtilGenerator
import com.qubacy.itsok.ui._common._test.view.util.action.wait.WaitViewAction
import com.qubacy.itsok.ui.application.activity._common.screen.chat.model.ChatViewModel
import com.qubacy.itsok.ui.application.activity._common.screen.chat.model.module.ChatViewModelModule
import com.qubacy.itsok.ui.application.activity._common.screen.chat.model.operation.NextMessagesUiOperation
import com.qubacy.itsok.ui.application.activity._common.screen.chat.model.state.ChatUiState
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.test.runTest
import com.qubacy.itsok.R
import com.qubacy.itsok._common.chat.stage.ChatStage
import com.qubacy.itsok.ui._common._test.view.util.matcher.image.animated.AnimatedImageViewMatcher
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.business.BusinessFragmentTest
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.operation.loading.SetLoadingStateUiOperation
import com.qubacy.itsok.ui.application.activity._common.screen.chat.component.typing.view.TypingMaterialTextView
import com.qubacy.itsok.ui.application.activity._common.screen.chat.model.operation.ChangeStageUiOperation
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@UninstallModules(
    ChatViewModelModule::class
)
class ChatFragmentTest(

) : BusinessFragmentTest<ChatUiState, ChatViewModel, ChatFragment>() {
    override fun getFragmentClass(): Class<ChatFragment> {
        return ChatFragment::class.java
    }

    override fun getCurrentDestination(): Int {
        return R.id.chatFragment
    }

    @Before
    override fun setup() {
        super.setup()
    }

    @Test
    fun loadingStateChangeLeadsToAvatarChangeTest() = runTest {
        val thinkingAnimDuration = InstrumentationRegistry.getInstrumentation().targetContext
            .resources.getInteger(R.integer.itsok_animation_thinking_duration).toLong()

        mUiOperationFlow.emit(SetLoadingStateUiOperation(true))

        Espresso.onView(withId(R.id.fragment_chat_image_avatar))
            .perform(WaitViewAction(thinkingAnimDuration))
            .check(ViewAssertions.matches(
                AnimatedImageViewMatcher(R.drawable.itsok_animated_thinking_backwards)
            ))

        mUiOperationFlow.emit(SetLoadingStateUiOperation(false))

        Espresso.onView(withId(R.id.fragment_chat_image_avatar))
            .perform(WaitViewAction(thinkingAnimDuration))
            .check(ViewAssertions.matches(
                AnimatedImageViewMatcher(R.drawable.itsok_animated_thinking)
            ))
    }

    @Test
    fun loadingStateChangeLeadsToControlsEnabledChangeTest() = runTest {
        mUiOperationFlow.emit(SetLoadingStateUiOperation(true))

        Espresso.onView(withId(R.id.fragment_chat_gripe_input))
            .check(ViewAssertions.matches(Matchers.not(ViewMatchers.isEnabled())))
        Espresso.onView(withId(R.id.component_chat_memento_buttons_button_positive))
            .check(ViewAssertions.matches(Matchers.not(ViewMatchers.isEnabled())))
        Espresso.onView(withId(R.id.component_chat_memento_buttons_button_negative))
            .check(ViewAssertions.matches(Matchers.not(ViewMatchers.isEnabled())))

        mUiOperationFlow.emit(SetLoadingStateUiOperation(false))

        Espresso.onView(withId(R.id.fragment_chat_gripe_input))
            .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
        Espresso.onView(withId(R.id.component_chat_memento_buttons_button_positive))
            .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
        Espresso.onView(withId(R.id.component_chat_memento_buttons_button_negative))
            .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
    }

    @Test
    fun gripeTextInputGoesUpOnSoftKeyboardAppearanceTest() = runTest {
        mUiOperationFlow.emit(ChangeStageUiOperation(ChatStage.GRIPE))

        Espresso.onView(withId(R.id.fragment_chat_gripe_input))
            .perform(ViewActions.click())
        Espresso.onView(withId(R.id.component_chat_gripe_input_text))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    private fun getActiveMessageCharTypingAnimationDuration(): Long {
        var charTypingDuration = TypingMaterialTextView.DEFAULT_CHAR_TYPING_DURATION

        mActivityScenario.onActivity {
            charTypingDuration = mFragment.requireView()
                .findViewById<TypingMaterialTextView>(R.id.component_active_message_text)!!
                .getChatTypingDuration()
        }

        return charTypingDuration
    }

    @Test
    fun newMessageAppearsTest() = runTest {
        val message = MessageUtilGenerator.generateMessage()
        val newMessageOperation = NextMessagesUiOperation(
            listOf(message)
        )

        mUiOperationFlow.emit(newMessageOperation)

        val charTypingDuration = getActiveMessageCharTypingAnimationDuration()
        val typingDuration = charTypingDuration * message.text!!.length

        Espresso.onView(isRoot()).perform(WaitViewAction(typingDuration))
        Espresso.onView(withText(message.text))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Deprecated("Should be synchronized properly.")
    @Test
    fun newMessagesAppearOneByOneTest() = runTest {
        val messages = MessageUtilGenerator.generateMessages(3)
        val newMessagesOperation = NextMessagesUiOperation(messages)

        mUiOperationFlow.emit(newMessagesOperation)

        var charTypingDuration = getActiveMessageCharTypingAnimationDuration()

        for (i in messages.indices) {
            val message = messages[i]
            val textTypingDuration = charTypingDuration * message.text!!.length
            var activeTextView: TypingMaterialTextView? = null

            mActivityScenario.onActivity {
                activeTextView = mFragment.requireView().findViewById(R.id.component_active_message_text)
            }

            Espresso.onView(isRoot()).perform(WaitViewAction(textTypingDuration))
            Assert.assertEquals(message.text, activeTextView!!.fullText)
        }
    }

    @Test
    fun newMessageChunksChangingTest() = runTest {
        val prevMessageChunk = MessageUtilGenerator.generateMessages(2)
        val nextMessageChunk = MessageUtilGenerator.generateMessages(1, prevMessageChunk.size)

        mUiOperationFlow.emit(NextMessagesUiOperation(prevMessageChunk))

        Espresso.onView(withText(prevMessageChunk.last().text))
            .check(ViewAssertions.doesNotExist())

        mUiOperationFlow.emit(NextMessagesUiOperation(nextMessageChunk))

        for (prevMessage in prevMessageChunk)
            Espresso.onView(withText(prevMessage.text))
                .check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun settingStageFromGripeToMementoOfferingLeadsToControlsSetChangeTest() = runTest {
        mUiOperationFlow.emit(ChangeStageUiOperation(ChatStage.GRIPE))

        Espresso.onView(withId(R.id.fragment_chat_gripe_input))
            .check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(withId(R.id.fragment_chat_memento_buttons))
            .check(ViewAssertions.matches(
                ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)))

        mUiOperationFlow.emit(ChangeStageUiOperation(ChatStage.MEMENTO_OFFERING))

        Espresso.onView(withId(R.id.fragment_chat_gripe_input))
            .check(ViewAssertions.matches(
                ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        Espresso.onView(withId(R.id.fragment_chat_memento_buttons))
            .check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun settingStageFromGripeToMementoOfferingLeadsToAvatarChangeTest() = runTest {
        val wonderAnimationDuration = InstrumentationRegistry.getInstrumentation().targetContext
            .resources.getInteger(R.integer.itsok_animation_wonder_duration).toLong()
        val happyMementoAnimationDuration = InstrumentationRegistry.getInstrumentation().targetContext
            .resources.getInteger(R.integer.itsok_animation_happy_memento_duration).toLong()

        mUiOperationFlow.emit(ChangeStageUiOperation(ChatStage.GRIPE))

        Espresso.onView(withId(R.id.fragment_chat_image_avatar))
            .perform(WaitViewAction(wonderAnimationDuration))
            .check(ViewAssertions.matches(
                AnimatedImageViewMatcher(R.drawable.itsok_animated_wonder_backwards)
            ))

        mUiOperationFlow.emit(ChangeStageUiOperation(ChatStage.MEMENTO_OFFERING))

        Espresso.onView(withId(R.id.fragment_chat_image_avatar))
            .perform(WaitViewAction(happyMementoAnimationDuration))
            .check(ViewAssertions.matches(
                AnimatedImageViewMatcher(R.drawable.itsok_animated_happy_memento_backwards)
            ))
    }

    @Test
    fun settingStageFromMementoOfferingToByeLeadsToControlsSetChangeTest() = runTest {
        mUiOperationFlow.emit(ChangeStageUiOperation(ChatStage.MEMENTO_OFFERING))

        Espresso.onView(withId(R.id.fragment_chat_memento_buttons))
            .check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(withId(R.id.fragment_chat_bye_buttons))
            .check(ViewAssertions.matches(
                ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)))

        mUiOperationFlow.emit(ChangeStageUiOperation(ChatStage.BYE))

        Espresso.onView(withId(R.id.fragment_chat_memento_buttons))
            .check(ViewAssertions.matches(
                ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        Espresso.onView(withId(R.id.fragment_chat_bye_buttons))
            .check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun settingStageFromMementoOfferingToByeLeadsToAvatarChangeTest() = runTest {
        val happyMementoAnimationDuration = InstrumentationRegistry.getInstrumentation().targetContext
            .resources.getInteger(R.integer.itsok_animation_wonder_duration).toLong()
        val byeAnimationDuration = InstrumentationRegistry.getInstrumentation().targetContext
            .resources.getInteger(R.integer.itsok_animation_happy_memento_duration).toLong()

        mUiOperationFlow.emit(ChangeStageUiOperation(ChatStage.MEMENTO_OFFERING))

        Espresso.onView(withId(R.id.fragment_chat_image_avatar))
            .perform(WaitViewAction(happyMementoAnimationDuration))
            .check(ViewAssertions.matches(
                AnimatedImageViewMatcher(R.drawable.itsok_animated_happy_memento_backwards)
            ))

        mUiOperationFlow.emit(ChangeStageUiOperation(ChatStage.BYE))

        Espresso.onView(withId(R.id.fragment_chat_image_avatar))
            .perform(WaitViewAction(byeAnimationDuration))
            .check(ViewAssertions.matches(
                AnimatedImageViewMatcher(R.drawable.itsok_animated_happy_bye_backwards)
            ))
    }

    @Test
    fun clickingOnSettingsMenuItemLeadsToTransitionToGeneralSettingsFragmentTest() {
        Espresso.onView(withId(R.id.chat_top_bar_action_settings))
            .perform(ViewActions.click())

        val gottenDestination = mNavController.currentDestination!!.id

        Assert.assertEquals(R.id.generalSettingsFragment, gottenDestination)
    }
}