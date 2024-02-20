package com.qubacy.itsok.domain.chat.model

import android.graphics.drawable.Drawable

data class Message(
    val text: String? = null,
    val image: Drawable? = null
) {
    fun isNull(): Boolean {
        return (text == null && image == null)
    }
}