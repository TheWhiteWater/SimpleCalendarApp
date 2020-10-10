package nz.co.redice.simplecalendarapp

import android.app.Application
import androidx.room.Room
import nz.co.redice.simplecalendarapp.db.CalendarDatabase

class CalendarApplication : Application() {

    companion object {
        lateinit var database: CalendarDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            this,
            CalendarDatabase::class.java,
            "calendar_database"
        )
            .build()
    }

}