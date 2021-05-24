package com.udacity.project4.locationreminders.savereminder

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.data.local.RemindersDatabase
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import com.udacity.project4.locationreminders.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class SaveReminderViewModelTest {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var saveReminderViewModel: SaveReminderViewModel
    private lateinit var fakeDataSource: FakeDataSource
    private lateinit var database: RemindersDatabase

    @Before
    fun init() {
        saveReminderViewModel =
            SaveReminderViewModel(ApplicationProvider.getApplicationContext(), fakeDataSource)
    }

    @Test
    fun testOnClear() {
        saveReminderViewModel.onClear()
        val reminderTile = saveReminderViewModel.reminderTitle.getOrAwaitValue()
        val reminderDescription = saveReminderViewModel.reminderDescription.getOrAwaitValue()
        val reminderSelectedLocationStr =
            saveReminderViewModel.reminderSelectedLocationStr.getOrAwaitValue()
        val selectedPOI = saveReminderViewModel.selectedPOI.getOrAwaitValue()
        val latitude = saveReminderViewModel.latitude.getOrAwaitValue()
        val longitude = saveReminderViewModel.longitude.getOrAwaitValue()
        assert(reminderTile == null)
        assert(reminderDescription == null)
        assert(reminderSelectedLocationStr == null)
        assert(selectedPOI == null)
        assert(latitude == null)
        assert(longitude == null)
    }

    @Test
    fun testValidateAndSaveReminder() = mainCoroutineRule.runBlockingTest {
        val reminderData = ReminderDataItem("title", "description", "location", null, null)
        saveReminderViewModel.validateAndSaveReminder(reminderData)
        val reminder = fakeDataSource.getReminder(reminderData.id)
        val reminderDTO = ReminderDTO(
                reminderData.title,
                reminderData.description,
                reminderData.location,
                null,
                null,
                reminderData.id
            )
        assertThat(reminder as Result.Success, not(nullValue()))
        assertThat(reminder.data, `is`(reminderDTO))
    }


}