package nz.co.redice.simplecalendarapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dates")
data class CalendarDate(
    @PrimaryKey val calendarDate: Long
)