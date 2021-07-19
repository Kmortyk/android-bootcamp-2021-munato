package com.vanilla.munato.model

import android.os.Parcel
import android.os.Parcelable

data class PaintingModel(
    val paintingID: String?, // id for pagination
    val user: String?, // username
    val name: String?, // name of the picture
    val code: String?, // javascript code for this scene
    val stars: Int, // path for cover of this painting
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(paintingID)
        parcel.writeString(user)
        parcel.writeString(name)
        parcel.writeString(code)
        parcel.writeInt(stars)
    }

    override fun describeContents() = 0 // todo

    companion object CREATOR : Parcelable.Creator<PaintingModel> {
        override fun createFromParcel(parcel: Parcel): PaintingModel {
            return PaintingModel(parcel)
        }

        override fun newArray(size: Int): Array<PaintingModel?> {
            return arrayOfNulls(size)
        }
    }
}