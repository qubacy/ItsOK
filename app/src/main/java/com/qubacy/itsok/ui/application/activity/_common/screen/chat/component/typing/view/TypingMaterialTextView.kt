package com.qubacy.itsok.ui.application.activity._common.screen.chat.component.typing.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import com.qubacy.itsok.R
import com.qubacy.itsok.ui.application.activity._common.screen.chat.component.typing.view.job.TypingJobLauncher
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

class TypingMaterialTextView : MaterialTextView {
    companion object {
        const val TAG = "CharMaterialTextView"

        const val DEFAULT_ATTR_INT_VALUE = -1
        const val DEFAULT_CHAR_TYPING_DURATION = 500L
    }

    private var mCoroutineScope: CoroutineScope? = null
    private var mCharTypingDuration: Long = DEFAULT_CHAR_TYPING_DURATION

    private var mTypingJob: Job? = null

    private var mFullText: String = String()
    val fullText get() = mFullText

    private var mCallback: TypingMaterialTextViewCallback? = null

    constructor(
        context: Context,
        attrs: AttributeSet
    ) : super(context, attrs) {
        initCustomAttrs(context, attrs)
    }

    private fun initCustomAttrs(
        context: Context, attrs: AttributeSet
    ) {
        val attrsTypedArray = context.obtainStyledAttributes(
            attrs,
            intArrayOf(R.attr.charTypingDuration)
        )

        attrsTypedArray.getInt(0, DEFAULT_ATTR_INT_VALUE).also {
            if (it == DEFAULT_ATTR_INT_VALUE) return@also

            mCharTypingDuration = it.toLong()
        }

        attrsTypedArray.recycle()
    }

    fun setCoroutineScope(coroutineScope: CoroutineScope) {
        mCoroutineScope = coroutineScope
    }

    fun setCallback(callback: TypingMaterialTextViewCallback) {
        mCallback = callback
    }

    fun isTyping(): Boolean {
        return mTypingJob != null
    }

    fun stopTypingText() {
        handleTypingStop()
    }

    fun setText(text: String) {
        if (mTypingJob != null) stopTypingText()

        mFullText = text
        this.text = text
    }

    fun typeText(text: String) {
        if (text.isEmpty()) return

        mFullText = text

        if (mTypingJob != null) stopTypingText()

        mTypingJob = TypingJobLauncher(
            text, this, mCharTypingDuration
        ) {
            mTypingJob = null
            mCallback?.onTextTypingFinished()

        }.run(mCoroutineScope!!)
    }

    private fun handleTypingStop() {
        text = mFullText

        Log.d(TAG, "handleTypingStop(): job to cancel = ${mTypingJob.toString()}")

        mTypingJob?.cancel()

        mTypingJob = null
    }
}