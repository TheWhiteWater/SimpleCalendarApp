package nz.co.redice.simplecalendarapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey


@Entity(
    tableName = "events",
    foreignKeys = [ForeignKey(
        entity = CalendarDate::class,
        parentColumns = ["calendarDate"],
        childColumns = ["eventDate"],
        onDelete = CASCADE,
        onUpdate = CASCADE
    )]
)
data class Event(
    @PrimaryKey(autoGenerate = true) val eventId: Int,
    @ColumnInfo(index = true) val eventDate: Long,
    val eventTime: Long,
    val eventTitle: String,
    val eventNote: String
)