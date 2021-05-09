package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import com.udacity.project4.locationreminders.data.dto.ReminderDTO

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import kotlinx.coroutines.ExperimentalCoroutinesApi;
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Test

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

    //    TODO: Add testing implementation to the RemindersDao.kt
//Executes each task synchronously using Architecture Components
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: RemindersDatabase

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @Test
    fun getReminders() = runBlockingTest {
        val reminderDTO = ReminderDTO("title", "description", null, null, null)
        val reminders = listOf(reminderDTO)
        database.reminderDao().saveReminder(reminderDTO)

        val list: List<ReminderDTO> = database.reminderDao().getReminders()

        assertThat<List<ReminderDTO>>(list, `is`(reminders))
    }

    @Test
    fun getReminderById() = runBlockingTest {
        val reminderDTO = ReminderDTO("title", "description", null, null, null)
        database.reminderDao().saveReminder(reminderDTO)

        val reminder = database.reminderDao().getReminderById(reminderDTO.id)
        assertThat(reminder, `is`(reminderDTO))
    }

    @Test
    fun saveReminder() = runBlockingTest {
        val reminderDTO = ReminderDTO("title", "description", null, null, null)
        database.reminderDao().saveReminder(reminderDTO)
        val reminder = database.reminderDao().getReminderById(reminderDTO.id)
        assertThat(reminder, `is`(reminderDTO))
    }

    @Test
    fun deleteAllReminders() = runBlockingTest {
        val reminderDTO = ReminderDTO("title", "description", null, null, null)
        database.reminderDao().saveReminder(reminderDTO)
        database.reminderDao().deleteAllReminders()
        val list = database.reminderDao().getReminders()
        assertThat<List<ReminderDTO>>(list, `is`(emptyList()))
    }

    @After
    fun closeDb() {
        database.close()
    }

}