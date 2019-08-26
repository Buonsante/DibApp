package it.uniba.maw.collapsiblecalendar.data;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;


import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by shrikanthravi on 06/03/18.
 */

public class Day implements Parcelable{
    public static final String DEBUG_TAG = "dibAppDebug";
    private int mYear;
    private int mMonth;
    private int mDay;

    public Day(int year, int month, int day){
        this.mYear = year;
        this.mMonth = month;
        this.mDay = day;
    }

    public int getMonth(){
        return mMonth;
    }

    public int getYear(){
        return mYear;
    }

    public int getDay(){
        return mDay;
    }



    public Day(Parcel in){
        int[] data = new int[3];
        in.readIntArray(data);
        this.mYear = data[0];
        this.mMonth = data[1];
        this.mYear = data[2];
    }

    public boolean isSunday(){
        GregorianCalendar c = new GregorianCalendar();
        c.set(mYear,mMonth,mDay);
        Log.w(DEBUG_TAG, "DAY: "+c.get(Calendar.DAY_OF_WEEK)+"    -----    "+Calendar.SUNDAY);
        if(c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
            return true;
        else
            return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeIntArray(new int[] {this.mYear,
                this.mMonth,
                this.mDay});
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Day createFromParcel(Parcel in) {
            return new Day(in);
        }

        public Day[] newArray(int size) {
            return new Day[size];
        }
    };


}
