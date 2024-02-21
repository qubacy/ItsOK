package com.qubacy.itsok.domain.settings.memento.model

import android.content.Context
import android.net.Uri
import com.qubacy.itsok._common.util.context.getDrawableFromUri
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento._common.data.UIMemento

data class Memento(
    val id: Long,
    val text: String? = null,
    val imageUri: Uri? = null
) {

}

fun Memento.toUIMessage(context: Context): UIMemento {
    val imageDrawable =
        if (imageUri != null) context.getDrawableFromUri(imageUri)
        else null

    return UIMemento(id, text, imageDrawable)
}
