package com.qubacy.itsok.data.memento.model

import android.net.Uri
import com.qubacy.itsok.data.memento.repository.source.local.entity.MementoEntity
import com.qubacy.itsok.domain.settings.memento.model.Memento

data class DataMemento(
    val id: Long,
    val text: String? = null,
    val imageUriString: String? = null
)

fun DataMemento.toMemento(): Memento {
    return Memento(id, text, Uri.parse(imageUriString))
}

fun DataMemento.toMementoEntity(): MementoEntity {
    return MementoEntity(id, text, imageUriString)
}