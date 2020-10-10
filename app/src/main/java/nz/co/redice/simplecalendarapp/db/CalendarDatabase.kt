package nz.co.redice.simplecalendarapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import nz.co.redice.simplecalendarapp.data.CalendarDate
import nz.co.redice.simplecalendarapp.data.Event

@Database(
    entities = [(CalendarDate::class), (Event::class)],
    version = 1
)
abstract class CalendarDatabase : RoomDatabase() {

    abstract fun calendarDao(): CalendarDao
}