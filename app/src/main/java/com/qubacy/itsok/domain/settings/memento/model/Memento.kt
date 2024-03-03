package com.qubacy.itsok.domain.settings.memento.model

import android.content.Context
import android.net.Uri
import com.qubacy.itsok._common.util.context.getDrawableFromUri
import com.qubacy.itsok.data.memento.model.DataMemento
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento._common.data.UIMemento
import java.io.Serializable

data class Memento(
    val id: Long? = null,
    val text: String? = null,
    val imageUri: Uri? = null
) : Serializable {

}

fun Memento.toUIMemento(context: Context): UIMemento {
    val imageDrawable =
        if (imageUri != null) context.getDrawableFromUri(imageUri)
        else null

    return UIMemento(id!!, text, imageDrawable)
}

fun Memento.toDataMemento(): DataMemento {
    return DataMemento(id, text, imageUri?.toString())
}