package com.qubacy.itsok.data.answer.repository.source.local

import android.content.ContentValues
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.qubacy.itsok._common.chat.stage.ChatStage
import com.qubacy.itsok.data._common.repository.source_common.local.database.LocalDatabaseDataSourceTest
import com.qubacy.itsok.data._common.repository.source_common.local.database._common._test.insertable.LocalInsertableDatabaseDataSourceTest
import com.qubacy.itsok.data.answer.model.type.AnswerType
import com.qubacy.itsok.data.answer.repository.source.local.entity.AnswerEntity
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocalAnswerDataSourceTest(

) : LocalDatabaseDataSourceTest(), LocalInsertableDatabaseDataSourceTest<AnswerEntity> {
    private lateinit var mLocalAnswerDataSource: LocalAnswerDataSource

    @Before
    override fun setup() {
        super.setup()

        mLocalAnswerDataSource = mDatabase.answerDao()
    }

    override fun packEntityContent(itemEntity: AnswerEntity): ContentValues {
        val contentValues = ContentValues().apply {
            put(AnswerEntity.ID_PROP_NAME, itemEntity.id)
            put(AnswerEntity.LANG_PROP_NAME, itemEntity.lang)
            put(AnswerEntity.STAGE_ID_PROP_NAME, itemEntity.stageId)
            put(AnswerEntity.TYPE_ID_PROP_NAME, itemEntity.typeId)
            put(AnswerEntity.TEXT_PROP_NAME, itemEntity.text)
        }

        return contentValues
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

        insertItems(mDatabase, AnswerEntity.TABLE_NAME, expectedAnswerEntities)

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

        insertItems(mDatabase, AnswerEntity.TABLE_NAME, expectedAnswerEntities)

        val gottenAnswers = mLocalAnswerDataSource.getAnswersForLangAndStage(langCode, stageId)

        Assert.assertEquals(expectedAnswerEntities.size, gottenAnswers.size)

        for (expectedAnswerEntity in expectedAnswerEntities)
            Assert.assertNotNull(gottenAnswers.find { it == expectedAnswerEntity })
    }
}