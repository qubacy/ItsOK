package com.qubacy.itsok.data.answer.repository.source.local

import android.content.ContentValues
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.qubacy.itsok._common.chat.stage.ChatStage
import com.qubacy.itsok.data._common.repository.source_common.local.database.LocalDatabaseDataSourceTest
import com.qubacy.itsok.data._common.repository.source_common.local.database.util.insert
import com.qubacy.itsok.data.answer.model.type.AnswerType
import com.qubacy.itsok.data.answer.repository.source.local.entity.AnswerEntity
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocalAnswerDataSourceTest : LocalDatabaseDataSourceTest() {
    private lateinit var mLocalAnswerDataSource: LocalAnswerDataSource

    @Before
    override fun setup() {
        super.setup()

        mLocalAnswerDataSource = mDatabase.answerDao()
    }

    private fun insertAnswers(answerEntities: List<AnswerEntity>) {
        for (answerEntity in answerEntities) {
            val argValues = ContentValues()

            argValues.put(AnswerEntity.ID_PROP_NAME, answerEntity.id)
            argValues.put(AnswerEntity.LANG_PROP_NAME, answerEntity.lang)
            argValues.put(AnswerEntity.STAGE_ID_PROP_NAME, answerEntity.stageId)
            argValues.put(AnswerEntity.TYPE_ID_PROP_NAME, answerEntity.typeId)
            argValues.put(AnswerEntity.TEXT_PROP_NAME, answerEntity.text)

            mDatabase.insert(AnswerEntity.TABLE_NAME, argValues)
        }
    }

    @Test
    fun getAnswersForLangAndStageTest() {
        val langCode = "en"
        val stageId = ChatStage.IDLE.id

        val originalAnswerEntities = listOf(
            AnswerEntity(1, langCode, stageId, text = "test answer 1"),
            AnswerEntity(2, langCode, ChatStage.BYE.id, text = "test answer 2"),
        )
        val expectedAnswerEntities = originalAnswerEntities.filter {
            it.stageId == stageId && it.lang == langCode }

        insertAnswers(expectedAnswerEntities)

        val gottenAnswers = mLocalAnswerDataSource.getAnswersForLangAndStage(langCode, stageId)

        Assert.assertEquals(expectedAnswerEntities.size, gottenAnswers.size)

        for (expectedAnswerEntity in expectedAnswerEntities)
            Assert.assertNotNull(gottenAnswers.find { it == expectedAnswerEntity })
    }

    @Test
    fun getAnswersForLangStageAndType() {
        val langCode = "en"
        val stageId = ChatStage.IDLE.id
        val typeId = AnswerType.MEMENTO_OFFERING_TEXT_ONLY.id

        val originalAnswerEntities = listOf(
            AnswerEntity(1, "ru", stageId, typeId, text = "test answer 1"),
            AnswerEntity(2, langCode, stageId, AnswerType.MEMENTO_OFFERING_NO_MEMENTO.id, text = "test answer 2"),
            AnswerEntity(3, langCode, stageId, typeId, text = "test answer 3")
        )
        val expectedAnswerEntities = originalAnswerEntities.filter {
            it.stageId == stageId && it.lang == langCode && it.typeId == typeId }

        insertAnswers(expectedAnswerEntities)

        val gottenAnswers = mLocalAnswerDataSource.getAnswersForLangAndStage(langCode, stageId)

        Assert.assertEquals(expectedAnswerEntities.size, gottenAnswers.size)

        for (expectedAnswerEntity in expectedAnswerEntities)
            Assert.assertNotNull(gottenAnswers.find { it == expectedAnswerEntity })
    }
}