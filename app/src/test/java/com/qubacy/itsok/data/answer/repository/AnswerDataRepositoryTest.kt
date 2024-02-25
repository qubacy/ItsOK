package com.qubacy.itsok.data.answer.repository

import com.qubacy.itsok._common.chat.stage.ChatStage
import com.qubacy.itsok.data._common.repository.DataRepositoryTest
import com.qubacy.itsok.data.answer.model.type.AnswerType
import com.qubacy.itsok.data.answer.repository.source.local.LocalAnswerDataSource
import com.qubacy.itsok.data.answer.repository.source.local.entity.AnswerEntity
import com.qubacy.itsok.data.answer.repository.source.local.entity.toDataAnswer
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class AnswerDataRepositoryTest : DataRepositoryTest<AnswerDataRepository>() {
    @Before
    fun setup() {
        initRepository()
    }

    private fun initRepository(
        getAnswersForLangAndStageResult: List<AnswerEntity> = listOf(),
        getAnswersForLangStageAndTypeResult: List<AnswerEntity> = listOf()
    ) {
        val localAnswerDataSourceMock = Mockito.mock(LocalAnswerDataSource::class.java)

        Mockito.`when`(localAnswerDataSourceMock.getAnswersForLangAndStage(
            Mockito.anyString(), Mockito.anyInt())).thenReturn(getAnswersForLangAndStageResult)
        Mockito.`when`(localAnswerDataSourceMock.getAnswersForLangStageAndType(
            Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())
        ).thenReturn(getAnswersForLangStageAndTypeResult)

        mDataRepository = AnswerDataRepository(localAnswerDataSourceMock)
    }

    @Test
    fun getRandomAnswerForStageAndTypeWithNullTypeTest() {
        val chatStage = ChatStage.BYE
        val typeIdValue = AnswerEntity.TYPE_DEFAULT_VALUE.toInt()
        val expectedAnswerEntity = AnswerEntity(0, "en",
            chatStage.id, typeIdValue, "test answer 1")
        val expectedAnswer = expectedAnswerEntity.toDataAnswer()

        initRepository(getAnswersForLangAndStageResult = listOf(expectedAnswerEntity))

        val gottenAnswer = mDataRepository
            .getRandomAnswerForStageAndType(chatStage, null)

        Assert.assertEquals(expectedAnswer, gottenAnswer)
    }

    @Test
    fun getRandomAnswerForStageAndTypeWithNotNullTypeTest() {
        val type = AnswerType.MEMENTO_OFFERING_TEXT_AND_IMAGE
        val expectedAnswerEntity = AnswerEntity(0, "en",
            type.stage.id, type.id, "test answer 1")
        val expectedAnswer = expectedAnswerEntity.toDataAnswer()

        initRepository(getAnswersForLangStageAndTypeResult = listOf(expectedAnswerEntity))

        val gottenAnswer = mDataRepository.getRandomAnswerForStageAndType(type.stage, type)

        Assert.assertEquals(expectedAnswer, gottenAnswer)
    }
}