package com.example.textviewplayground.ui.application.activity.screen_common.component.message.view._common.util.wrapper

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

// Note: This concept can be use to pass any custom attrs to <included> components' classes;

//class MessageViewWrapper : FrameLayout {
//    private var mMessageBackground: Drawable? = null
//
//    constructor(
//        context: Context, attrs: AttributeSet
//    ) : super(context, attrs) {
//        initCustomAttrs(context, attrs)
//    }
//
//    private fun initCustomAttrs(
//        context: Context, attrs: AttributeSet
//    ) {
//        val attrsTypedArray = context.obtainStyledAttributes(
//            attrs,
//            intArrayOf(R.attr.messageBackground)
//        )
//
//        attrsTypedArray.getDrawable(0).also {
//            if (it == null) return@also
//
//            mMessageBackground = it
//        }
//
//        attrsTypedArray.recycle()
//    }
//
//    override fun onViewAdded(child: View?) {
//        super.onViewAdded(child)
//
//        if (child == null) return
//        if (!child::class.java.isAssignableFrom(MessageView::class.java)) return
//
//        child as MessageView
//
//        child.background = mMessageBackground
//    }
//}