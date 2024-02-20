package com.qubacy.itsok.ui.application.activity._common.screen._common.component.message.view.previous

import android.content.Context
import android.util.AttributeSet
import com.qubacy.itsok.domain.chat.model.Message
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.qubacy.itsok.ui.application.activity._common.screen._common.component.message.view._common.MessageView

class PreviousMessageView(
    context: Context, attrs: AttributeSet
) : MessageView<MaterialTextView, ShapeableImageView, Message>(context, attrs) {

}