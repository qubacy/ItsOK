package com.qubacy.itsok.ui.application.activity._common.screen._common.component.message.view._common

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.updateLayoutParams
import com.qubacy.itsok.R
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.qubacy.itsok.ui.application.activity._common.screen.chat._common.data.message.UIMessage
import kotlinx.coroutines.CoroutineScope

open class MessageView<
    TextViewType : MaterialTextView, ImageViewType : ShapeableImageView, MessageType: UIMessage
>(
    context: Context,
    attrs: AttributeSet
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
    }

    protected var mCoroutineScope: CoroutineScope? = null

    protected var mElementGapInPx: Int

    protected var mTextView: TextViewType? = null
    protected var mImageView: ImageViewType? = null

    protected var mMessage: MessageType? = null

    init {
        mElementGapInPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            DEFAULT_ELEMENT_GAP_IN_DP.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
    }

    fun setCoroutineScope(coroutineScope: CoroutineScope) {
        mCoroutineScope = coroutineScope
    }

    protected open fun setImage(image: Drawable?) {
        if (mImageView == null) {
            if (image == null) return

            initImageView()
        }

        prepareImageViewForImageAndText(image, mMessage?.text) // todo: is it ok?
        mImageView!!.setImageDrawable(image)
    }

    protected fun initImageView() {
        mImageView = inflateImageView()

        addElementView(mImageView!!, StandardElementType.IMAGE)
    }

    protected open fun inflateImageView(): ImageViewType {
        return LayoutInflater.from(context).inflate(
            R.layout.component_message_image, this, false) as ImageViewType
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