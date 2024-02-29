package com.qubacy.itsok._common.util.context

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.TypedValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
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

fun Context.dpToPx(dp: Int): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        resources.displayMetrics
    ).toInt()
}

val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")