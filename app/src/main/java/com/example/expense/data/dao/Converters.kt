package com.example.expense.data.dao

import androidx.room.TypeConverter
import java.util.Date

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long): Date?{
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun  dateTimeStamps(date: Date?): Long?{
        return date?.time
    }
}