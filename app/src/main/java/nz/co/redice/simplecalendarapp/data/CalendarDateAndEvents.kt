package nz.co.redice.simplecalendarapp.data

import androidx.room.Embedded
import androidx.room.Relation

data class CalendarDateAndEvents (
    @Embedded val calendarDate: CalendarDate,

    @Relation (
        parentColumn = "calendarDate",
        entityColumn = "eventDate"
    ) val events: List<Event>
)