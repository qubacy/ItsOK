package com.qubacy.itsok.data.error.repository.source.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.qubacy.itsok._common.error.Error

@Entity(
    tableName = ErrorEntity.TABLE_NAME,
    primaryKeys = [ErrorEntity.ID_PROP_NAME, ErrorEntity.LANG_PROP_NAME]
)
data class ErrorEntity(
    @ColumnInfo(name = ID_PROP_NAME) val id: Long,
    @ColumnInfo(name = LANG_PROP_NAME) val lang: String,
    @ColumnInfo(name = MESSAGE_PROP_NAME) val message: String,
    @ColumnInfo(
        name = IS_CRITICAL_PROP_NAME,
        defaultValue = IS_CRITICAL_DEFAULT_VALUE
    ) val isCritical: Boolean
) {
    companion object {
        const val TABLE_NAME = "Error"

        const val ID_PROP_NAME = "id"
        const val LANG_PROP_NAME = "lang"
        const val MESSAGE_PROP_NAME = "message"
        const val IS_CRITICAL_PROP_NAME = "is_critical"

        const val IS_CRITICAL_DEFAULT_VALUE = "0"
    }
}

fun ErrorEntity.toError(): Error {
    return Error(id, message, isCritical)
}