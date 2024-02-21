package com.qubacy.itsok.data.memento.repository.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.qubacy.itsok.data.memento.model.DataMemento

@Entity(
    tableName = MementoEntity.TABLE_NAME
)
data class MementoEntity(
    @PrimaryKey
    @ColumnInfo(name = ID_PROP_NAME) val id: Long,
    @ColumnInfo(
        name = "text",
        defaultValue = TEXT_DEFAULT_VALUE
    ) val text: String?,
    @ColumnInfo(
        name = "image_uri",
        defaultValue = IMAGE_URI_DEFAULT_VALUE
    ) val imageUri: String?
) {
    companion object {
        const val TABLE_NAME = "Memento"

        const val ID_PROP_NAME = "id"

        const val TEXT_DEFAULT_VALUE = "NULL"
        const val IMAGE_URI_DEFAULT_VALUE = "NULL"
    }
}

fun MementoEntity.toDataMemento(): DataMemento {
    return DataMemento(id, text, imageUri)
}