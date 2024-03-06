package com.qubacy.itsok.ui.application.activity._common.screen.chat.component.message.view.previous

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.qubacy.itsok.R
import com.qubacy.itsok.ui.application.activity._common.screen.chat.component.message.view._common.MessageView
import com.qubacy.itsok.ui.application.activity._common.screen.chat._common.data.message.UIMessage

class PreviousMessageView(
    context: Context, attrs: AttributeSet? = null
) : MessageView<MaterialTextView, ShapeableImageView, UIMessage>(context, attrs) {
    init {
        background = ResourcesCompat.getDrawable(
            context.resources, R.drawable.message_back_adaptive, context.theme)
    }
}