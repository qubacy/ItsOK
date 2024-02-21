package com.qubacy.itsok.domain.chat.model

import android.content.Context
import android.net.Uri
import com.qubacy.itsok._common.util.context.getDrawableFromUri
import com.qubacy.itsok.ui.application.activity._common.screen.chat._common.data.message.UIMessage

data class Message(
    val text: String? = null,
    val imageUri: Uri? = null
) {
    fun isNull(): Boolean {
        return (text == null && imageUri == null)
    }
}

fun Message.toUIMessage(context: Context): UIMessage {
    val imageDrawable =
        if (imageUri != null) context.getDrawableFromUri(imageUri)
        else null

    return UIMessage(text, imageDrawable)
}