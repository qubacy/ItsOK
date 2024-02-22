package com.qubacy.itsok.ui.application.activity._common.screen.chat

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.qubacy.itsok.domain.chat.model._test.util.MessageUtilGenerator
import com.qubacy.itsok.ui._common._test.view.util.action.wait.WaitViewAction
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.BaseFragmentTest
import com.qubacy.itsok.ui.application.activity._common.screen.chat.model.ChatViewModel
import com.qubacy.itsok.ui.application.activity._common.screen.chat.model.module.ChatViewModelModule
import com.qubacy.itsok.ui.application.activity._common.screen.chat.model.operation.NextMessagesUiOperation
import com.qubacy.itsok.ui.application.activity._common.screen.chat.model.state.ChatUiState
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.test.runTest
import com.qubacy.itsok.R
import com.qubacy.itsok.ui.application.activity._common.screen.chat.component.typing.view.TypingMaterialTextView
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

) : BaseFragmentTest<ChatUiState, ChatViewModel, ChatFragment>() {
    override fun getFragmentClass(): Class<ChatFragment> {
        return ChatFragment::class.java
    }

    @Before
    override fun setup() {
        super.setup()
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
}