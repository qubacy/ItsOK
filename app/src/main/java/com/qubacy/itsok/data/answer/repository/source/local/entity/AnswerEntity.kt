package com.qubacy.itsok.data.answer.repository.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.qubacy.itsok.data.answer.model.DataAnswer
import com.qubacy.itsok.data.answer.model.type.AnswerType

@Entity(
    tableName = AnswerEntity.TABLE_NAME,
    primaryKeys = [
        AnswerEntity.ID_PROP_NAME,
        AnswerEntity.LANG_PROP_NAME,
        AnswerEntity.STAGE_ID_PROP_NAME
    ]
)
data class AnswerEntity(
    @ColumnInfo(name = ID_PROP_NAME) val id: Long,
    @ColumnInfo(name = LANG_PROP_NAME) val lang: String,
    @ColumnInfo(name = STAGE_ID_PROP_NAME) val stageId: Int,
    @ColumnInfo(
        name = TYPE_ID_PROP_NAME,
        defaultValue = TYPE_DEFAULT_VALUE
    ) val typeId: Int?,
    val text: String
) {
    companion object {
        const val TABLE_NAME = "Answer"

        const val ID_PROP_NAME = "id"
        const val LANG_PROP_NAME = "lang"
        const val STAGE_ID_PROP_NAME = "stage_id"
        const val TYPE_ID_PROP_NAME = "type_id"

        const val TYPE_DEFAULT_VALUE = "NULL"
    }
}

fun AnswerEntity.toDataAnswer(): DataAnswer {
    val type =
        if (typeId != null) AnswerType.getTypeByStageIdAndId(stageId, typeId)
        else null

    return DataAnswer(id, type, text)
}
