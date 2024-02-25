package com.qubacy.itsok.ui.application.activity._common.screen._common.data.message._test.util

import android.graphics.drawable.Drawable
import com.qubacy.itsok.ui.application.activity._common.screen.chat._common.data.message.UIMessage
import kotlin.random.Random

object UIMessageUtilGenerator {
    enum class ImageMode {
        RANDOM(), SHARED();
    }

    fun generateUIMessage(
        text: String? = null,
        image: Drawable? = null
    ): UIMessage {
        return UIMessage(text, image)
    }

    fun generateUIMessages(
        count: Int,
        textPrefix: String? = null,
        image: Drawable? = null,
        imageMode: ImageMode = ImageMode.SHARED
    ): List<UIMessage> {
        return IntRange(0, count - 1).map {
            val curText = textPrefix?.plus(it.toString())
            val curImage = getImageByMode(image, imageMode)

            generateUIMessage(curText, curImage)
        }
    }

    private fun getImageByMode(image: Drawable?, mode: ImageMode): Drawable? {
        if (image == null) return null

        return when (mode) {
            ImageMode.RANDOM -> {
                if (Random(System.currentTimeMillis()).nextBoolean()) image
                else null
            }
            ImageMode.SHARED -> image
        }
    }
}