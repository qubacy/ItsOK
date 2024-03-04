package com.qubacy.itsok.ui.application.activity._common.screen.chat.model

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.qubacy.itsok._common.chat.stage.ChatStage
import com.qubacy.itsok.data.error.repository.ErrorDataRepository
import com.qubacy.itsok.domain.chat.usecase.ChatUseCase
import com.qubacy.itsok.domain.chat.model._test.util.MessageGeneratorUtil
import com.qubacy.itsok.domain.chat.usecase.result.GetNextMessagesDomainResult
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.operation.loading.SetLoadingStateUiOperation
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.business.model.BusinessViewModelTest
import com.qubacy.itsok.ui.application.activity._common.screen.chat.model.operation.ChangeStageUiOperation
import com.qubacy.itsok.ui.application.activity._common.screen.chat.model.operation.NextMessagesUiOperation
import com.qubacy.itsok.ui.application.activity._common.screen.chat.model.state.ChatUiState
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito
import java.util.concurrent.atomic.AtomicBoolean

class ChatViewModelTest : BusinessViewModelTest<ChatUiState, ChatUseCase, ChatViewModel>(
    ChatUseCase::class.java
) {
    private var mGetIntroMessagesCallFlag: AtomicBoolean = AtomicBoolean(false)
    private var mGetGripeMessagesCallFlag: AtomicBoolean = AtomicBoolean(false)
    private var mGetMementoMessagesCallFlag: AtomicBoolean = AtomicBoolean(false)
    private var mGetByeMessagesCallFlag: AtomicBoolean = AtomicBoolean(false)

    override fun initUseCase(): ChatUseCase {
        val useCase = super.initUseCase()

        Mockito.`when`(useCase.getIntroMessages()).thenAnswer {
            mGetIntroMessagesCallFlag.set(true)
        }
        Mockito.`when`(useCase.getGripeMessages()).thenAnswer {
            mGetGripeMessagesCallFlag.set(true)
        }
        Mockito.`when`(useCase.getMementoMessages()).thenAnswer {
            mGetMementoMessagesCallFlag.set(true)
        }
        Mockito.`when`(useCase.getByeMessages()).thenAnswer {
            mGetByeMessagesCallFlag.set(true)
        }

        return useCase
    }

    override fun resetResults() {
        super.resetResults()

        mGetIntroMessagesCallFlag.set(false)
        mGetGripeMessagesCallFlag.set(false)
        mGetMementoMessagesCallFlag.set(false)
        mGetByeMessagesCallFlag.set(false)
    }

    override fun createViewModel(
        savedStateHandle: SavedStateHandle,
        errorDataRepository: ErrorDataRepository
    ): ChatViewModel {
        return ChatViewModel(savedStateHandle, errorDataRepository, mUseCase)
    }

    @Test
    fun getIntroMessagesTest() {
        mModel.getIntroMessages()

        Assert.assertTrue(mGetIntroMessagesCallFlag.get())
    }

    @Test
    fun getGripeMessagesTest() {
        mModel.getGripeMessages()

        Assert.assertTrue(mGetGripeMessagesCallFlag.get())
    }

    @Test
    fun getMementoMessagesTest() {
        mModel.getMementoMessages()

        Assert.assertTrue(mGetMementoMessagesCallFlag.get())
    }

    @Test
    fun isGripeValidTest() {
        data class TestCase(
            val gripeText: String,
            val isCorrect: Boolean
        )

        val testCases = listOf(
            TestCase("", false),
            TestCase("  ", false),
            TestCase("test", true)
        )

        for (testCase in testCases) {
            val gottenIsCorrect = mModel.isGripeValid(testCase.gripeText)

            Assert.assertEquals(testCase.isCorrect, gottenIsCorrect)
        }
    }

    @Test
    fun processGetNextMessagesDomainResultTest() = runTest {
        val initUiState = ChatUiState(
            stage = ChatStage.IDLE,
            error = null,
            isLoading = false
        )

        setUiState(initUiState)

        val expectedMessages = MessageGeneratorUtil.generateMessages(2)
        val expectedStage = ChatStage.GRIPE
        val expectedLoadingState = false

        mModel.uiOperationFlow.test {
            mResultFlow.emit(GetNextMessagesDomainResult(expectedMessages))

            val gottenMessagesOperation = awaitItem()
            val gottenLoadingOperation = awaitItem()
            val gottenSetStageOperation = awaitItem()

            Assert.assertEquals(NextMessagesUiOperation::class, gottenMessagesOperation::class)
            Assert.assertEquals(SetLoadingStateUiOperation::class, gottenLoadingOperation::class)
            Assert.assertEquals(ChangeStageUiOperation::class, gottenSetStageOperation::class)

            val gottenMessages = (gottenMessagesOperation as NextMessagesUiOperation).messages
            val gottenLoadingState = (gottenLoadingOperation as SetLoadingStateUiOperation).isLoading
            val gottenStage = (gottenSetStageOperation as ChangeStageUiOperation).stage

            Assert.assertEquals(expectedMessages.size, gottenMessages.size)

            for (expectedMessage in expectedMessages)
                Assert.assertTrue(gottenMessages.contains(expectedMessage))

            Assert.assertEquals(expectedLoadingState, gottenLoadingState)
            Assert.assertEquals(expectedStage, gottenStage)

            Assert.assertEquals(expectedMessages.size, mModel.uiState.messages.size)

            for (expectedMessage in expectedMessages)
                Assert.assertTrue(mModel.uiState.messages.contains(expectedMessage))
        }
    }

    @Test
    fun updateStageAfterMessagesTest() {
        data class TestCase(
            val initStage: ChatStage,
            val expectedEndStage: ChatStage
        )

        val testCases = listOf(
            TestCase(ChatStage.IDLE, ChatStage.GRIPE),
            TestCase(ChatStage.GRIPE, ChatStage.MEMENTO_OFFERING)
        )
        val updateStageAfterMessagesMethodReflection = ChatViewModel::class.java
            .getDeclaredMethod("updateStageAfterMessages")
            .apply { isAccessible = true }

        for (testCase in testCases) {
            val initUiState = ChatUiState(
                stage = testCase.initStage,
                error = null,
                isLoading = false
            )

            setUiState(initUiState)
            updateStageAfterMessagesMethodReflection.invoke(mModel)

            Assert.assertEquals(testCase.expectedEndStage, mModel.uiState.stage)
        }
    }

    @Test
    fun moveToByeTest() = runTest {
        val expectedStage = ChatStage.BYE

        mModel.uiOperationFlow.test {
            mModel.moveToBye()

            Assert.assertTrue(mGetByeMessagesCallFlag.get())

            val gottenStageOperation = awaitItem()

            Assert.assertEquals(ChangeStageUiOperation::class, gottenStageOperation::class)

            val gottenStage = (gottenStageOperation as ChangeStageUiOperation).stage

            Assert.assertEquals(expectedStage, gottenStage)
            Assert.assertEquals(expectedStage, mModel.uiState.stage)
        }
    }

    @Test
    fun restartTest() = runTest {
        val initUiState = ChatUiState(stage = ChatStage.GRIPE, error = null, isLoading = false)
        val expectedStage = ChatStage.IDLE
        val expectedLoadingState = true

        setUiState(initUiState)

        mModel.uiOperationFlow.test {
            mModel.restart()

            val gottenStageOperation = awaitItem()
            val gottenLoadingOperation = awaitItem()

            Assert.assertEquals(ChangeStageUiOperation::class, gottenStageOperation::class)
            Assert.assertEquals(SetLoadingStateUiOperation::class, gottenLoadingOperation::class)
            Assert.assertTrue(mGetIntroMessagesCallFlag.get())

            val gottenStage = (gottenStageOperation as ChangeStageUiOperation).stage
            val gottenLoadingState = (gottenLoadingOperation as SetLoadingStateUiOperation).isLoading

            Assert.assertEquals(expectedStage, gottenStage)
            Assert.assertEquals(expectedLoadingState, gottenLoadingState)

            Assert.assertEquals(expectedStage, mModel.uiState.stage)
            Assert.assertEquals(expectedLoadingState, mModel.uiState.isLoading)
        }
    }
}