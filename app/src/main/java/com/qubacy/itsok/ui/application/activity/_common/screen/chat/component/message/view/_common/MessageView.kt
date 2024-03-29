package com.qubacy.itsok.ui.application.activity._common.screen.chat.component.message.view._common

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.updateLayoutParams
import com.qubacy.itsok.R
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.qubacy.itsok._common.util.context.dpToPx
import com.qubacy.itsok.ui.application.activity._common.screen.chat._common.data.message.UIMessage

open class MessageView<
    TextViewType : MaterialTextView, ImageViewType : ShapeableImageView, MessageType: UIMessage
>(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {
    interface ElementType {
        val id: Int
    }

    enum class StandardElementType(override val id: Int) : ElementType {
        TEXT(0), IMAGE(1);
    }

    companion object {
        const val TAG = "MessageView"

        const val DEFAULT_ELEMENT_GAP_IN_DP = 8
        const val IMAGE_HEIGHT_COEFFICIENT = 0.5
    }

    protected var mElementGapInPx: Int

    protected var mTextView: TextViewType? = null
    protected var mImageView: ImageViewType? = null

    protected var mMessage: MessageType? = null

    init {
        mElementGapInPx = context.dpToPx(DEFAULT_ELEMENT_GAP_IN_DP)

        if (attrs == null) setDefaultAttrs()

        inflate()
    }

    private fun setDefaultAttrs() {
        setDefaultLayoutParams()
    }

    private fun setDefaultLayoutParams() {
        layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        orientation = VERTICAL
    }

    private fun inflate() {
        View.inflate(context, R.layout.component_message, this)
    }

    protected open fun setImage(image: Drawable?) {
        if (mImageView == null) {
            if (image == null) return

            initImageView()
        }

        prepareImageViewForImageAndText(image, mMessage?.text) // todo: is it ok?
        mImageView!!.setImageDrawable(image)

        mImageView!!.measure(0, 0)

    }

    protected fun initImageView() {
        mImageView = inflateImageView()

        setImageViewParams()
        addElementView(mImageView!!, StandardElementType.IMAGE)
    }

    protected open fun inflateImageView(): ImageViewType {
        return LayoutInflater.from(context).inflate(
            R.layout.component_message_image, this, false) as ImageViewType
    }

    private fun setImageViewParams() {
        setImageViewLayoutParams()
    }

    private fun setImageViewLayoutParams() {
        if (parent == null) return

        val parentView = parent as View
        val imageHeight = (parentView.measuredHeight * IMAGE_HEIGHT_COEFFICIENT).toInt()

        mImageView!!.updateLayoutParams {
            height = imageHeight
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (mImageView != null) setImageViewLayoutParams()
    }

    protected open fun prepareImageViewForImageAndText(image: Drawable?, text: String?) {
        setImageViewVisibilityWithImage(image)

        if (mImageView!!.visibility != View.GONE)
            setImageViewMarginsWithText(text)
    }

    protected open fun setImageViewVisibilityWithImage(image: Drawable?) {
        mImageView!!.visibility =
            if (image == null) View.GONE
            else View.VISIBLE
    }

    protected open fun setImageViewMarginsWithText(text: String?) {
        val newTopMargin = if (text.isNullOrEmpty()) 0 else mElementGapInPx

        mImageView!!.updateLayoutParams<LayoutParams> {
            setMargins(leftMargin, newTopMargin, rightMargin, bottomMargin)
        }
    }

    fun setMessage(message: MessageType) {
        if (message.isNull()) throw IllegalArgumentException()

        mMessage = message

        resetContent()
        setContentWithMessage(message)
    }

    /**
     * The order of calling set...() methods matters!
     */
    protected open fun setContentWithMessage(message: MessageType) {
        setText(message.text)
        setImage(message.image)
    }

    protected open fun resetContent() {
        setText(null)
        setImage(null)
    }

    protected fun setText(text: String?) {
        if (mTextView == null) {
            if (text == null) return

            initTextView()
        }

        setTextContent(text)
        adjustTextViewForText(text)
    }

    protected open fun adjustTextViewForText(text: String?) {
        mTextView?.visibility =
            if (text == null) View.GONE
            else if (text.isEmpty()) View.GONE
            else View.VISIBLE
    }

    fun getMessage(): MessageType? {
        return mMessage
    }

    protected open fun setTextContent(text: String?) {
        mTextView!!.text = text
    }

    protected fun initTextView() {
        mTextView = inflateTextView()

        addElementView(mTextView!!, StandardElementType.TEXT)
    }

    protected open fun inflateTextView(): TextViewType {
        return LayoutInflater.from(context).inflate(
            R.layout.component_prev_message_text, this, false) as TextViewType
    }

    /**
     * A return value designates a processing success status;
     */
    protected open fun addElementView(elementView: View, elementType: ElementType): Boolean {
        when (elementType) {
            StandardElementType.TEXT -> addTextView(elementView as TextViewType)
            StandardElementType.IMAGE -> addImageView(elementView as ImageViewType)
            else -> return false
        }

        return true
    }

    protected fun addTextView(textView: TextViewType) {
        if (mImageView != null) removeViewAt(0)

        addView(textView, 0)

        if (mImageView != null) addImageView(mImageView!!)
    }

    protected fun addImageView(imageView: ImageViewType) {
        val viewIndex = if (mTextView != null) 1 else 0

        addView(imageView, viewIndex)
    }
}