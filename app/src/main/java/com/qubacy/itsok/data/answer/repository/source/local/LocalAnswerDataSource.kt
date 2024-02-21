package com.qubacy.itsok.data.answer.repository.source.local

import androidx.room.Dao
import androidx.room.Query
import com.qubacy.itsok.data._common.repository.source._common.DataSource
import com.qubacy.itsok.data.answer.repository.source.local.entity.AnswerEntity

@Dao
interface LocalAnswerDataSource : DataSource {
    @Query(
        "SELECT * " +
        "FROM ${AnswerEntity.TABLE_NAME} " +
        "WHERE ${AnswerEntity.LANG_PROP_NAME} = :lang " +
        "AND ${AnswerEntity.STAGE_ID_PROP_NAME} = :stageId " +
        "AND ${AnswerEntity.TYPE_ID_PROP_NAME} = :typeId"
    )
    fun getAnswersForLangStageAndType(lang: String, stageId: Int, typeId: Int): List<AnswerEntity>

    @Query(
        "SELECT * " +
        "FROM ${AnswerEntity.TABLE_NAME} " +
        "WHERE ${AnswerEntity.LANG_PROP_NAME} = :lang " +
        "AND ${AnswerEntity.STAGE_ID_PROP_NAME} = :stageId"
    )
    fun getAnswersForLangAndStage(lang: String, stageId: Int): List<AnswerEntity>
}