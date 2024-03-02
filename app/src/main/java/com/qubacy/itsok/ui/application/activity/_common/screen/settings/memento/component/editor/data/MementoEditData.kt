package com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.component.editor.data

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import androidx.core.os.ParcelCompat

data class MementoEditData(
    var text: String? = null,
    var imageUri: Uri? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        ParcelCompat.readParcelable(parcel, Uri::class.java.classLoader, Uri::class.java)
    ) {

    }

    fun isEmpty(): Boolean {
        return (text.isNullOrEmpty() && imageUri == null)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(text)
        parcel.writeParcelable(imageUri, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MementoEditData> {
        override fun createFromParcel(parcel: Parcel): MementoEditData {
            return MementoEditData(parcel)
        }

        override fun newArray(size: Int): Array<MementoEditData?> {
            return arrayOfNulls(size)
        }
    }
}