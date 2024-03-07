package com.qubacy.itsok.domain.chat

import app.cash.turbine.test
import com.qubacy.itsok._common._test._common.util.mock.AnyMockUtil
import com.qubacy.itsok.data._common.repository._common.DataRepository
import com.qubacy.itsok.data.answer.model.DataAnswer
import com.qubacy.itsok.data.answer.model.type.AnswerType
import com.qubacy.itsok.data.answer.repository.AnswerDataRepository
import com.qubacy.itsok.data.error.repository.ErrorDataRepository
import com.qubacy.itsok.data.memento.model.DataMemento
import com.qubacy.itsok.data.memento.repository.MementoDataRepository
import com.qubacy.itsok.domain._common.usecase.UseCaseTest
import com.qubacy.itsok.domain._common.usecase._common.result._common.DomainResult
import com.qubacy.itsok.domain.chat.model.Message
import com.qubacy.itsok.domain.chat.usecase.result.GetNextMessagesDomainResult
import com.qubacy.itsok.domain.chat.usecase.ChatUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito

class ChatUseCaseTest : UseCaseTest<ChatUseCase>() {
    protected var mGetRandomAnswerForStageAndTypeResult: DataAnswer? = null
    protected var mGetRandomMementoResult: DataMemento? = null

    override fun initRepositories(): List<DataRepository> {
        val baseRepositories = super.initRepositories()

        val answerDataRepositoryMock = Mockito.mock(AnswerDataRepository::class.java)

        Mockito.`when`(answerDataRepositoryMock.getRandomAnswerForStageAndType(
            AnyMockUtil.anyObject(), AnyMockUtil.anyObject())
        ).thenAnswer{ mGetRandomAnswerForStageAndTypeResult }

        val mementoDataRepositoryMock = Mockito.mock(MementoDataRepository::class.java)

        Mockito.`when`(mementoDataRepositoryMock.getRandomMemento())
            .thenAnswer { (mGetRandomMementoResult) }

        return baseRepositories.plus(listOf(answerDataRepositoryMock, mementoDataRepositoryMock))
    }

    override fun initUseCase(repositories: List<DataRepository>) {
        // todo: is this too bad?:
        mUseCase = ChatUseCase(
            repositories[0] as ErrorDataRepository,
            repositories[1] as AnswerDataRepository,
            repositories[2] as MementoDataRepository
        )
    }

    @Test
    fun getIntroMessagesTest() = runTest {
        val expectedDataAnswer = DataAnswer(0, null, "test message")
        val expectedMessage = Message(expectedDataAnswer.text)

        mGetRandomAnswerForStageAndTypeResult = expectedDataAnswer

        mUseCase.resultFlow.test {
            mUseCase.getIntroMessages()

            val gottenResult = awaitItem()

            assertGottenMessageFromResult(gottenResult, expectedMessage)
        }
    }

    @Test
    fun getGripeMessagesTest() = runTest {
        val expectedGripeAnswer = DataAnswer(0, null, "test answer")
        val expectedMessage = Message(expectedGripeAnswer.text)

        mGetRandomAnswerForStageAndTypeResult = expectedGripeAnswer

        mUseCase.resultFlow.test {
            mUseCase.getGripeMessages()

            val gottenResult = awaitItem()

            assertGottenMessageFromResult(gottenResult, expectedMessage)
        }
    }

    @Test
    fun getMementoMessagesTest() = runTest {
        val expectedDataMemento = DataMemento(0, "test memento")
        val expectedDataAnswer = DataAnswer(
            0, AnswerType.MEMENTO_OFFERING_NO_MEMENTO, "test answer")
        val expectedMessages = listOf(
            Message(expectedDataAnswer.text),
            Message(expectedDataMemento.text)
        )

        mGetRandomMementoResult = expectedDataMemento
        mGetRandomAnswerForStageAndTypeResult = expectedDataAnswer

        mUseCase.resultFlow.test {
            mUseCase.getMementoMessages()

            val gottenResult = awaitItem()

            Assert.assertEquals(GetNextMessagesDomainResult::class, gottenResult::class)

            val gottenMessages = (gottenResult as GetNextMessagesDomainResult).messages

            Assert.assertEquals(expectedMessages.size, gottenMessages.size)

            for (expectedMessage in expectedMessages)
                Assert.assertNotNull(gottenMessages.find { it == expectedMessage })
        }
    }

    @Test
    fun getByeMessagesTest() = runTest {
        val expectedByeAnswer = DataAnswer(0, null, "test answer")
        val expectedMessage = Message(expectedByeAnswer.text)

        mGetRandomAnswerForStageAndTypeResult = expectedByeAnswer

        mUseCase.resultFlow.test {
            mUseCase.getByeMessages()

            val gottenResult = awaitItem()

            assertGottenMessageFromResult(gottenResult, expectedMessage)
        }
    }

    private fun assertGottenMessageFromResult(gottenResult: DomainResult, expectedMessage: Message) {
        Assert.assertEquals(GetNextMessagesDomainResult::class, gottenResult::class)

        val gottenMessage = (gottenResult as GetNextMessagesDomainResult).messages.first()

        Assert.assertEquals(expectedMessage, gottenMessage)
    }
}