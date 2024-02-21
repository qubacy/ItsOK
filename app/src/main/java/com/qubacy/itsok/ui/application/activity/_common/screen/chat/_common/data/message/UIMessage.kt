package com.qubacy.itsok.ui.application.activity._common.screen.chat._common.data.message

import android.graphics.drawable.Drawable

data class UIMessage(
    val text: String? = null,
    val image: Drawable? = null
) {
    fun isNull(): Boolean {
        return (text == null && image == null)
    }
}