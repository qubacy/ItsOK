package com.qubacy.itsok._common.util.context

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import java.io.FileNotFoundException
import kotlin.Exception

fun Context.getDrawableFromUri(uri: Uri): Drawable? {
    var drawable: Drawable? = null

    try {
        val inputStream = contentResolver.openInputStream(uri)

        drawable = Drawable.createFromStream(inputStream, uri.toString())

    }
    catch (_: FileNotFoundException) { }
    catch (e: Exception) {
        e.printStackTrace()

        throw e
    }

    return drawable
}