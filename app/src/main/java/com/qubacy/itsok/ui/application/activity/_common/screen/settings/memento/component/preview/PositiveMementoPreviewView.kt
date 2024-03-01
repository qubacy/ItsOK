package com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.component.preview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.StyleRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.widget.TextViewCompat
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import com.qubacy.itsok.R
import com.qubacy.itsok._common.util.context.dpToPx
import com.qubacy.itsok.databinding.ComponentPositiveMementoPreviewBinding
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento._common.data.UIMemento

class PositiveMementoPreviewView(
    context: Context,
    attrs: AttributeSet? = null
) : MaterialCardView(context, attrs) {
    companion object {
        const val DEFAULT_HEIGHT_IN_DP = 100

        const val DEFAULT_TEXT_LAYOUT_WEIGHT = 2f
        const val DEFAULT_IMAGE_LAYOUT_WEIGHT = 1f

        const val TEXT_VIEW_INDEX = 0
        const val IMAGE_VIEW_INDEX = 1
    }

    private lateinit var mBinding: ComponentPositiveMementoPreviewBinding

    private var mTextView: MaterialTextView? = null
    private var mImageView: AppCompatImageView? = null

    private var mComponentGap: Int = 0
    private var mCardGap: Int = 0

    @StyleRes
    private var mTextAppearanceStyle: Int = 0
    @ColorInt
    private var mCardBackgroundColor: Int = 0

    init {
        inflate()
        initStyles()
        initDimens()

        if (attrs == null) setDefaultAttrs()
    }

    private fun inflate() {
        val layoutInflater = LayoutInflater.from(context)

        mBinding = ComponentPositiveMementoPreviewBinding
            .inflate(layoutInflater, this)
    }

    @SuppressLint("ResourceType")
    private fun initStyles() {
        context.theme.obtainStyledAttributes(
            R.style.AppTheme,
            intArrayOf(
                com.google.android.material.R.attr.textAppearanceBodyLarge,
                com.google.android.material.R.attr.colorSurfaceContainerLow
            )
        ).apply {
            try {
                val textAppearanceBodyLargeStyleResId = getResourceId(0, 0)

                if (textAppearanceBodyLargeStyleResId == 0) throw IllegalStateException()

                val cardBackgroundColorInt = getColor(1, 0)

                mTextAppearanceStyle = textAppearanceBodyLargeStyleResId
                mCardBackgroundColor = cardBackgroundColorInt
            }
            finally { recycle() }
        }
    }

    private fun initDimens() {
        mComponentGap = context.resources
            .getDimension(R.dimen.medium_gap_between_components).toInt()
        mCardGap = context.resources
            .getDimension(R.dimen.default_gap_between_components).toInt()
    }

    private fun setDefaultAttrs() {
        setDefaultLayoutParams()
        setStyle()
    }

    private fun setDefaultLayoutParams() {
        val heightInPx = context.dpToPx(DEFAULT_HEIGHT_IN_DP)

        layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            heightInPx
        ).apply {
            bottomMargin = mCardGap
        }
    }

    private fun setStyle() {
        setCardBackgroundColor(mCardBackgroundColor)

        isClickable = true
    }

    fun setMemento(memento: UIMemento) {
        if (memento.text == null && memento.image == null)
            throw IllegalArgumentException()

        setText(memento.text)
        setImage(memento.image)

        addViewsToLayout()
        setComponentsLayoutParams()
    }

    private fun setText(text: String?) {
        if (text.isNullOrEmpty()) return resetTextView()
        if (mTextView == null) createTextView()
        if (mTextView!!.visibility != View.VISIBLE)
            mTextView!!.visibility = View.VISIBLE

        mTextView!!.text = text
    }

    private fun resetTextView() {
        mTextView?.apply {
            this.text = String()
            visibility = ViewGroup.GONE
        }
    }

    private fun createTextView() {
        mTextView = MaterialTextView(context).apply {
            TextViewCompat.setTextAppearance(this, mTextAppearanceStyle)
            setPadding(mComponentGap, 0, mComponentGap, 0)

            gravity = Gravity.CENTER_VERTICAL
        }
    }

    private fun setImage(image: Drawable?) {
        if (image == null) return resetImageView()
        if (mImageView == null) createImageView()
        if (mImageView!!.visibility != View.VISIBLE)
            mImageView!!.visibility = View.VISIBLE

        mImageView!!.setImageDrawable(image)
    }

    private fun resetImageView() {
        mImageView?.apply {
            setImageDrawable(null)

            visibility = View.GONE
        }
    }

    private fun createImageView() {
        mImageView = AppCompatImageView(context).apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }

    private fun addViewsToLayout() {
        if (mImageView != null && mTextView != null &&
            mImageView!!.parent != null && mTextView!!.parent == null
        ) {
            mBinding.componentPositiveMementoPreviewWrapper.apply {
                removeViewAt(TEXT_VIEW_INDEX)

                addView(mTextView)
                addView(mImageView)
            }

        } else {
            if (mTextView != null && mTextView!!.parent == null) {
                val viewIndex = getViewIndex(mTextView!!)

                mBinding.componentPositiveMementoPreviewWrapper.addView(
                    mTextView,
                    viewIndex
                )
            }
            if (mImageView != null && mImageView!!.parent == null) {
                val viewIndex = getViewIndex(mImageView!!)

                mBinding.componentPositiveMementoPreviewWrapper.addView(
                    mImageView,
                    viewIndex
                )
            }
        }
    }

    private fun getViewIndex(view: View): Int {
        return when (view) {
            mTextView -> TEXT_VIEW_INDEX
            mImageView -> if (mTextView == null) TEXT_VIEW_INDEX else IMAGE_VIEW_INDEX
            else -> throw IllegalArgumentException()
        }
    }

    private fun setComponentsLayoutParams() {
        val isTextViewPresented = isViewPresented(mTextView)
        val isImageViewPresented = isViewPresented(mImageView)

        if (isTextViewPresented && isImageViewPresented) {
            mTextView!!.apply {
                layoutParams = LinearLayoutCompat
                    .LayoutParams(0, LayoutParams.MATCH_PARENT)
                    .also {
                        it.weight = DEFAULT_TEXT_LAYOUT_WEIGHT
                    }
            }
            mImageView!!.apply {
                layoutParams = LinearLayoutCompat
                    .LayoutParams(0, LayoutParams.MATCH_PARENT)
                    .also {
                        it.weight = DEFAULT_IMAGE_LAYOUT_WEIGHT
                    }
            }

        } else if (isTextViewPresented) {
            mTextView!!.apply {
                layoutParams = LinearLayoutCompat
                    .LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            }

        } else {
            mImageView!!.apply {
                layoutParams = LinearLayoutCompat
                    .LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            }
        }
    }

    private fun isViewPresented(view: View?): Boolean {
        return (view != null && view.visibility != View.GONE)
    }
}