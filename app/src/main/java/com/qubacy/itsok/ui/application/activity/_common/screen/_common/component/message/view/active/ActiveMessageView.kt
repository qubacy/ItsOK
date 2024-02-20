package com.qubacy.itsok.ui.application.activity._common.screen._common.component.message.view.active

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.animation.AccelerateInterpolator
import com.qubacy.itsok.R
import com.qubacy.itsok.domain.chat.model.Message
import com.qubacy.itsok.ui.application.activity._common.screen._common.component.typing.view.TypingMaterialTextView
import com.qubacy.itsok.ui.application.activity._common.screen._common.component.typing.view.TypingMaterialTextViewCallback
import com.google.android.material.imageview.ShapeableImageView
import com.qubacy.itsok.ui.application.activity._common.screen._common.component.message.view._common.MessageView

class ActiveMessageView(
    context: Context,
    attrs: AttributeSet
) : MessageView<
        TypingMaterialTextView, ShapeableImageView, Message
        >(context, attrs), TypingMaterialTextViewCallback {
    companion object {
        const val TAG = "ActiveMessageView"

        const val DEFAULT_IMAGE_APPEAR_ANIMATION_DURATION = 300L
    }

    private var mAnimate: Boolean = true

    override fun inflateTextView(): TypingMaterialTextView {
        return (LayoutInflater.from(context).inflate(
            R.layout.component_active_message_text, this, false) as TypingMaterialTextView)
            .apply {
                setCoroutineScope(mCoroutineScope!!)
                setCallback(this@ActiveMessageView)
            }
    }

    fun setMessage(message: Message, animate: Boolean) {
        mAnimate = animate

        setMessage(message)
    }

    override fun setTextContent(text: String?) {
        Log.d(TAG, "setTextContent(): view = ${this.toString()}")

        if (mAnimate && text != null) mTextView!!.typeText(text)
        else mTextView!!.setText(text)
    }

    override fun resetContent() {
        if (mAnimate) {
            mTextView?.setText(String())
            setImage(null)

        } else super.resetContent()
    }

    override fun setContentWithMessage(message: Message) {
        if (message.text != null && mAnimate) {
            message.text.also { setText(it) }
        } else super.setContentWithMessage(message)
    }

    override fun setImageViewVisibilityWithImage(image: Drawable?) {
        if (!mAnimate)
            return super.setImageViewVisibilityWithImage(image)

        animateImageAppearance(image)
    }

    private fun animateImageAppearance(image: Drawable?) {
        mImageView!!.apply {
            alpha = 0f

            animate().alpha(1f).also {
                it.interpolator = AccelerateInterpolator()
                it.duration = DEFAULT_IMAGE_APPEAR_ANIMATION_DURATION
            }.withStartAction {
                super.setImageViewVisibilityWithImage(image)
            }.start()
        }
    }

    override fun onTextTypingFinished() {
        mMessage?.image?.also {
            setImage(it)
        }
    }

    override fun onDetachedFromWindow() {
        mTextView?.stopTypingText()

        super.onDetachedFromWindow()
    }

    // TODO: looks like a chunk of shitty code. mb there is a different solution?
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (mTextView?.isTyping() != true) {
            mAnimate = false

            if (mTextView?.text?.toString() != mMessage?.text)
                setText(mMessage?.text)
            if (mImageView?.drawable?.toString() != mMessage?.image?.toString())
                setImage(mMessage?.image)
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}