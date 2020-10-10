package nz.co.redice.simplecalendarapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import nz.co.redice.simplecalendarapp.data.CalendarDate
import nz.co.redice.simplecalendarapp.data.CalendarDateAndEvents
import nz.co.redice.simplecalendarapp.data.Event

@Dao
interface CalendarDao {

    @Insert(onConflict = REPLACE)
    fun insertCalendarDate(vararg date: CalendarDate)

    @Insert(onConflict = REPLACE)
    fun insertEvent(vararg event: Event)

    @Query("DELETE FROM dates")
    fun clearDates()

    @Delete
    fun deleteDate(date: CalendarDate)

    @Query("SELECT * FROM events ORDER BY eventDate")
    fun getAllEvents(): LiveData<List<Event>>

    @Query("SELECT * FROM dates ORDER BY calendarDate")
    fun getAllDates(): LiveData<List<CalendarDate>>

    @Transaction
    @Query("SELECT * FROM dates WHERE calendarDate==:date")
    fun getAllEventsOnSelectedDate(date: Long): LiveData<CalendarDateAndEvents>
}