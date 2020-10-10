package nz.co.redice.simplecalendarapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import junit.framework.Assert.assertTrue
import nz.co.redice.simplecalendarapp.data.Event
import nz.co.redice.simplecalendarapp.data.EventTagJoin
import nz.co.redice.simplecalendarapp.data.Tag
import nz.co.redice.simplecalendarapp.db.CalendarDao
import nz.co.redice.simplecalendarapp.db.CalendarDatabase
import org.junit.After
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
    fun insertTagsInsertsTags() {
        val tag1 = Tag("tag1")
        val tag2 = Tag("tag2")
        dao.insertTag(tag1, tag2)

        val testObserver: Observer<List<Tag>> = mock()
        dao.getAllTags().observeForever(testObserver)

        val listClass = ArrayList::class.java as Class<ArrayList<Tag>>
        val argumentCaptor = ArgumentCaptor.forClass(listClass)

        verify(testObserver).onChanged(argumentCaptor.capture())
        val captureArgument = argumentCaptor.value
        assertTrue(captureArgument.containsAll(listOf(tag1, tag2)))
    }


    @Test
    fun getEventsByTagsRetrievesEvents() {
        val tag1 = Tag("tag1")
        dao.insertTag(tag1)

        val event1 = Event(1,1,1,"event1")
        val event2 = Event(2,1,1,"event2")
        dao.insertEvent(event1, event2)

        val eventTagConstraint1 = EventTagJoin(event1.eventId, tag1.name)
        val eventTagConstraint2 = EventTagJoin(event2.eventId, tag1.name)
        dao.insertEventTagConstraint(eventTagConstraint1, eventTagConstraint2)

        assertTrue(dao.getEventsByTag(tag1.name).containsAll(listOf(event1, event2)))
    }

    @Test
    fun getTagsByEventIdRetrievesTags() {
        val tag1 = Tag("tag1")
        val tag2 = Tag("tag2")
        dao.insertTag(tag1, tag2)

        val event1 = Event(1,1,1,"event1")
        dao.insertEvent(event1)

        val eventTagConstraint1 = EventTagJoin(event1.eventId, tag1.name)
        val eventTagConstraint2 = EventTagJoin(event1.eventId, tag2.name)
        dao.insertEventTagConstraint(eventTagConstraint1, eventTagConstraint2)

        assertTrue(dao.getTagsByEventId(event1.eventId).containsAll(listOf(tag1, tag2)))
    }




}