package com.example.financialmanager

import android.os.Parcel
import android.os.Parcelable


data class Transaction(
    var id:Int,
    var image:Int,
    var type: String?,
    var place: String?,
    var amount: Double?,
    var date: String?,
    var category: String?

): Parcelable, Comparable<Transaction> {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(image)
        parcel.writeString(type)
        parcel.writeString(place)
        parcel.writeValue(amount)
        parcel.writeString(date)
        parcel.writeString(category)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Transaction> {
        override fun createFromParcel(parcel: Parcel): Transaction {
            return Transaction(parcel)
        }

        override fun newArray(size: Int): Array<Transaction?> {
            return arrayOfNulls(size)
        }
    }

    override fun compareTo(otherTransaction: Transaction): Int {


        val splitTab = this.date!!.split('-')
        val year = splitTab[0].toInt()
        val monthStr = splitTab[1]
        var dayStr = splitTab[2]
        val month = if(monthStr.startsWith('0')){
            monthStr.substring(1).toInt()
        }else{
            monthStr.toInt()
        }
        val day = if(dayStr.startsWith('0')){
            dayStr.substring(1).toInt()
        }else{
            dayStr.toInt()
        }

        val splitTabOther = this.date!!.split('-')
        val yearOther = splitTab[0].toInt()
        val monthOtherStr = splitTab[1]
        val dayOtherStr = splitTab[2]
        val dayOther = if(dayOtherStr.startsWith('0')){
            dayOtherStr.substring(1).toInt()
        }else{
            dayOtherStr.toInt()
        }
        val monthOther = if(monthOtherStr.startsWith('0')){
            monthOtherStr.substring(1).toInt()
        }else{
            monthOtherStr.toInt()
        }

        return when{
            year!=yearOther ->  year - yearOther
            month!=monthOther -> month - monthOther
            else -> day - dayOther
        }

    }
}

