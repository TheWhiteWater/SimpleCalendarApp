package nz.co.redice.simplecalendarapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import nz.co.redice.simplecalendarapp.data.CalendarDate
import nz.co.redice.simplecalendarapp.data.CalendarDateAndEvents
import nz.co.redice.simplecalendarapp.data.Event
import nz.co.redice.simplecalendarapp.db.CalendarDao
import nz.co.redice.simplecalendarapp.db.CalendarDatabase
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor

@RunWith(AndroidJUnit4::class)
class CalendarDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var database: CalendarDatabase
    lateinit var dao: CalendarDao


    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            CalendarDatabase::class.java
        ).build()
        dao = database.calendarDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun getAllDatesReturnsEmptyList() {
        val testObserver: Observer<List<CalendarDate>> = mock()
        dao.getAllDates().observeForever(testObserver)
        verify(testObserver).onChanged(emptyList())
    }

    @Test
    fun getAllEventsReturnsEmptyList() {
        val testObserver: Observer<List<Event>> = mock()
        dao.getAllEvents().observeForever(testObserver)
        verify(testObserver).onChanged(emptyList())
    }

    @Test
    fun insertDateInsertsCalendarDates() {
        val date1 = CalendarDate(1)
        val date2 = CalendarDate(2)
        dao.insertCalendarDate(date1, date2)

        val testObserver: Observer<List<CalendarDate>> = mock()
        dao.getAllDates().observeForever(testObserver)

        val listClass = ArrayList::class.java as Class<ArrayList<CalendarDate>>
        val argumentCaptor = ArgumentCaptor.forClass(listClass)

        verify(testObserver).onChanged(argumentCaptor.capture())
        val captureArgument = argumentCaptor.value
        assertTrue(captureArgument.containsAll(listOf(date1, date2)))
    }

    @Test
    fun insertEventInsertsEvents() {
        val date1 = CalendarDate(1)
        dao.insertCalendarDate(date1)

        val event1 = Event(1,1,1, "title", "body")
        val event2 = Event(2,1,1, "title", "body")
        dao.insertEvent(event1, event2)

        val testObserver: Observer<List<Event>> = mock()
        dao.getAllEvents().observeForever(testObserver)

        val listClass = ArrayList::class.java as Class<ArrayList<Event>>
        val argumentCaptor = ArgumentCaptor.forClass(listClass)

        verify(testObserver).onChanged(argumentCaptor.capture())
        val captureArgument = argumentCaptor.value
        assertTrue(captureArgument.containsAll(listOf(event1, event2)))
    }


    @Test
    fun getAllEventsOnSelectedDateReturnsProperEvents() {
        val date1 = CalendarDate(1)
        dao.insertCalendarDate(date1)

        val event1 = Event(1,1,1, "title", "body")
        val event2 = Event(2,1,1, "title", "body")
        dao.insertEvent(event1, event2)

        val testObserver: Observer<CalendarDateAndEvents> = mock()
        dao.getAllEventsOnSelectedDate(1).observeForever(testObserver)
        val argumentCaptor = ArgumentCaptor.forClass(CalendarDateAndEvents::class.java)

        verify(testObserver).onChanged(argumentCaptor.capture())
        val captureArgument = argumentCaptor.value
        assertTrue(captureArgument.events.containsAll(listOf(event1, event2)))
    }


    @Test
    fun clearDatesDeletesAllDates() {
        val date1 = CalendarDate(1)
        val date2 = CalendarDate(2)
        dao.insertCalendarDate(date1, date2)
        dao.clearDates()

        val testObserver: Observer<List<CalendarDate>> = mock()
        dao.getAllDates().observeForever(testObserver)
        verify(testObserver).onChanged(emptyList())
    }



    @Test
    fun deleteDateDeletesSpecificDate() {
        val date1 = CalendarDate(1)
        val date2 = CalendarDate(2)
        val date3 = CalendarDate(3)
        dao.insertCalendarDate(date1, date2, date3)

        dao.deleteDate(date2)

        val testObserver: Observer<List<CalendarDate>> = mock()
        dao.getAllDates().observeForever(testObserver)

        val listClass = ArrayList::class.java as Class<ArrayList<CalendarDate>>
        val argumentCaptor = ArgumentCaptor.forClass(listClass)

        verify(testObserver).onChanged(argumentCaptor.capture())
        val captureArgument = argumentCaptor.value
        assertFalse(captureArgument.contains(date2))
    }
}