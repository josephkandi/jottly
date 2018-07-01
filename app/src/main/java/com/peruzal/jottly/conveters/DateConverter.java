package com.peruzal.jottly.conveters;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by joseph on 6/30/18.
 */

public class DateConverter {
    @TypeConverter
    public static Long dateToTimestamp(Date date){
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Date timestampToDate(Long timestamp){
        return timestamp == null ? null : new Date(timestamp);
    }
}