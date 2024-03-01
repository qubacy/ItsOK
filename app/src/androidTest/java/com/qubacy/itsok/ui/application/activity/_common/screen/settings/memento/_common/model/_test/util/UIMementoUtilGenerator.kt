package com.qubacy.itsok.ui.application.activity._common.screen.settings.memento._common.model._test.util

import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import androidx.test.platform.app.InstrumentationRegistry
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento._common.data.UIMemento
import com.qubacy.itsok.R

object UIMementoUtilGenerator {
    const val TEST_ID = 0L
    const val TEST_TEXT = "test memento"
    val TEST_IMAGE: Drawable by lazy { loadTestDrawable() }

    fun generateUIMemento(
        withText: Boolean = false,
        withImage: Boolean = false
    ): UIMemento {
        val testText = if (withText) TEST_TEXT else null
        val testImage = if (withImage) TEST_IMAGE else null

        return UIMemento(TEST_ID, testText, testImage)
    }

    private fun loadTestDrawable(): Drawable {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        return ResourcesCompat.getDrawable(context.resources, R.drawable.itsok, context.theme)!!
    }
}
