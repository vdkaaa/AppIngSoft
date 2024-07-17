package ghoststudios.app.almuerzafacil.ui.theme

import android.os.Parcel
import android.os.Parcelable

data class Lunch (
    val id : String? = null,
    val name : String? = null,
    val description:String? = "No description added",
    val price: Int? = 0,

    val imgUri: String? = null
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeValue(price)
        parcel.writeString(imgUri)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Lunch> {
        override fun createFromParcel(parcel: Parcel): Lunch {
            return Lunch(parcel)
        }

        override fun newArray(size: Int): Array<Lunch?> {
            return arrayOfNulls(size)
        }
    }
}



