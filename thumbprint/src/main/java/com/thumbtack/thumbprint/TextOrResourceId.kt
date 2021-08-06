package com.thumbtack.thumbprint

import android.content.Context
import android.content.res.Resources
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.StringRes

/**
 * A sealed class which allows either a simple string or a resource to be given to a
 * Thumbprint component.
 */
sealed class TextOrResourceId : Parcelable {
    fun toString(context: Context): String {
        return toString(context.resources)
    }

    /** Resolves to a string, possibly via a resource lookup */
    fun toString(resources: Resources): String {
        return when (this) {
            is Text -> this.text
            is ResourceId -> resources.getString(this.resourceId)
        }
    }

    override fun describeContents() = 0

    companion object {
        fun create(text: String) = Text(text)
        fun create(@StringRes resourceId: Int) = ResourceId(resourceId)
    }
}

data class Text(val text: String) : TextOrResourceId() {
    constructor(parcel: Parcel) : this(parcel.readString()!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(text)
    }

    companion object CREATOR : Parcelable.Creator<Text> {
        override fun createFromParcel(parcel: Parcel): Text {
            return Text(parcel)
        }

        override fun newArray(size: Int): Array<Text?> {
            return arrayOfNulls(size)
        }
    }
}

data class ResourceId(@StringRes val resourceId: Int) : TextOrResourceId() {
    constructor(parcel: Parcel) : this(parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(resourceId)
    }

    companion object CREATOR : Parcelable.Creator<ResourceId> {
        override fun createFromParcel(parcel: Parcel): ResourceId {
            return ResourceId(parcel)
        }

        override fun newArray(size: Int): Array<ResourceId?> {
            return arrayOfNulls(size)
        }
    }
}
