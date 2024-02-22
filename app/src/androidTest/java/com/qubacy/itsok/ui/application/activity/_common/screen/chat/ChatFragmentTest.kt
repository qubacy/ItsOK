package com.qubacy.itsok.ui.application.activity._common.screen.chat

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.BaseFragmentTest
import com.qubacy.itsok.ui.application.activity._common.screen.chat.model.ChatViewModel
import com.qubacy.itsok.ui.application.activity._common.screen.chat.model.module.ChatViewModelModule
import com.qubacy.itsok.ui.application.activity._common.screen.chat.model.state.ChatUiState
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
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
}