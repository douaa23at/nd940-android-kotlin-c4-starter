package com.udacity.project4.locationreminders.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

    //    TODO: Add testing implementation to the RemindersLocalRepository.kt

    private lateinit var remindersLocalRepository: RemindersLocalRepository
    lateinit var database: RemindersDatabase

    @Before
    fun createRepository() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).allowMainThreadQueries().build()
        remindersLocalRepository = RemindersLocalRepository(
            database.reminderDao(),
            Dispatchers.Main
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getReminders() = runBlocking {
        val reminderDTO = ReminderDTO("title", "description", null, null, null)
        database.reminderDao().saveReminder(reminderDTO)
        val reminders = remindersLocalRepository.getReminders() as Result.Success
        assertThat(reminders, IsEqual(Result.Success(listOf(reminderDTO))))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun saveReminder() = runBlocking {
        val reminderDTO = ReminderDTO("title", "description", null, null, null)
        database.reminderDao().saveReminder(reminderDTO)
        val reminders = remindersLocalRepository.getReminders() as Result.Success
        assertThat(reminders, IsEqual(Result.Success(listOf(reminderDTO))))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getReminder() = runBlocking {
        val reminderDTO = ReminderDTO("title", "description", null, null, null)
        database.reminderDao().saveReminder(reminderDTO)
        val reminder = remindersLocalRepository.getReminder(reminderDTO.id) as Result.Success
        assertThat(reminder, `is`(Result.Success(reminderDTO)))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun deleteAllReminders() = runBlocking {
        val reminderDTO = ReminderDTO("title", "description", null, null, null)
        database.reminderDao().saveReminder(reminderDTO)
        remindersLocalRepository.deleteAllReminders()
        val reminders = remindersLocalRepository.getReminders() as Result.Success
        assertThat(reminders, `is`(Result.Success(emptyList())))
    }

}