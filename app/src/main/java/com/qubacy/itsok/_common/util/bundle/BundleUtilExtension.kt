package com.qubacy.itsok._common.util.bundle

import android.os.Build
import android.os.Bundle
import java.io.Serializable

fun <T : Serializable>Bundle.getSerializableCompat(key: String, clazz: Class<T>): T? {
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) getSerializable(key) as T?
    else getSerializable(key, clazz)
}