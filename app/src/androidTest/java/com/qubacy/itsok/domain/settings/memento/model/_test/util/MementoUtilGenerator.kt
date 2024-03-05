package com.qubacy.itsok.domain.settings.memento.model._test.util

import android.net.Uri
import androidx.test.platform.app.InstrumentationRegistry
import com.qubacy.itsok.R
import com.qubacy.itsok._common.context.util.getUriFromResId
import com.qubacy.itsok.domain.settings.memento.model.Memento

object MementoUtilGenerator {
    const val DEFAULT_PREFIX = "test memento "
    val DEFAULT_IMAGE_RESOURCE_ID = R.drawable.itsok

    val TEST_IMAGE: Uri by lazy {
        InstrumentationRegistry.getInstrumentation()
            .targetContext.getUriFromResId(DEFAULT_IMAGE_RESOURCE_ID)
    }

    fun generateMemento(id: Long? = null, withImage: Boolean = false): Memento {
        val image = if (withImage) TEST_IMAGE else null

        return Memento(id, DEFAULT_PREFIX + id.toString(), image)
    }

    fun generateMementoes(
        count: Int,
        idShift: Int = 0,
        withImage: Boolean = false
    ): List<Memento> {
        return IntRange(idShift, idShift + count - 1).map {
            val id = (it + idShift).toLong()

            generateMemento(id, withImage)
        }
    }
}