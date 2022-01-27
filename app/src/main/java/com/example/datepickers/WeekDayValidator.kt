package com.example.datepickers

import android.os.Parcel
import android.os.Parcelable
import com.google.android.material.datepicker.CalendarConstraints
import java.util.*


/*
* CUSTOMISED CLASS JUST CHANGE THE CONSTRUCTOR AND isValid() fn to get the
* desired output
* */

class WeekDayValidator() : CalendarConstraints.DateValidator {

    var point = 0L;

    constructor(point : Long) : this() {
        this.point = point
    }


    private val utc =
        Calendar.getInstance(TimeZone.getTimeZone("UTC"))

    constructor(parcel: Parcel) : this() {
    }

    override fun describeContents(): Int {
        return 0;
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
    }

    override fun isValid(date: Long): Boolean {

        utc.timeInMillis = date
        val dayOfWeek = utc[Calendar.DAY_OF_WEEK]
//        return dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY
        return date <= point
    }


    companion object CREATOR : Parcelable.Creator<WeekDayValidator> {
        override fun createFromParcel(parcel: Parcel): WeekDayValidator {
            return WeekDayValidator(parcel)
        }

        override fun newArray(size: Int): Array<WeekDayValidator?> {
            return arrayOfNulls(size)
        }
    }

}