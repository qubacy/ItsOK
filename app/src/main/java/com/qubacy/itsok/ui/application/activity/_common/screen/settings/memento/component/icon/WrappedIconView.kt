package com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.component.icon

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.qubacy.itsok.R
import com.qubacy.itsok.databinding.ComponentMementoEditorInputIconBinding

class WrappedIconView(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {
    private lateinit var mBinding: ComponentMementoEditorInputIconBinding

    init {
        inflate()

        if (attrs != null) initWithAttrs(attrs)
    }

    private fun inflate() {
        val layoutInflater = LayoutInflater.from(context)

        mBinding = ComponentMementoEditorInputIconBinding
            .inflate(layoutInflater, this)
    }

    private fun initWithAttrs(attrs: AttributeSet) {
        context.theme.obtainStyledAttributes(
            attrs, R.styleable.WrappedIconView, 0, 0
        ).apply {
            try {
                val iconImage = getDrawable(R.styleable.WrappedIconView_wrappedIconViewSrc)

                mBinding.componentMementoEditorInputIconImage.setImageDrawable(iconImage)

            }
            finally { recycle() }
        }
    }
}