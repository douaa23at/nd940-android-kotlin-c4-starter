package com.udacity.project4.locationreminders.reminderslist

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class RemindersListViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var reminderListViewModel: RemindersListViewModel
    private lateinit var fakeDataSource: FakeDataSource

    @Before
    fun init() {
        fakeDataSource = FakeDataSource()
        reminderListViewModel =
            RemindersListViewModel(ApplicationProvider.getApplicationContext(), fakeDataSource)
    }

    @Test
    fun testloadReminders() = mainCoroutineRule.runBlockingTest {
        val reminderDTO = ReminderDTO("title", "description", null, null, null)
        fakeDataSource.saveReminder(reminderDTO)
        reminderListViewModel.loadReminders()
        val reminders = reminderListViewModel.remindersList.getOrAwaitValue()
        val list = listOf(
            ReminderDataItem(
                reminderDTO.title,
                reminderDTO.description,
                null,
                null,
                null,
                reminderDTO.id
            )
        )
        assertThat(reminders, `is`(list))
    }

    @Test
    fun shouldReturnError() = mainCoroutineRule.runBlockingTest {
        fakeDataSource.reminders = null
        reminderListViewModel.loadReminders()
        val result = reminderListViewModel.showSnackBar.getOrAwaitValue()
        assertThat(result, `is`("Error"))
    }

    @Test
    fun check_loading() = mainCoroutineRule.runBlockingTest {
        mainCoroutineRule.pauseDispatcher()
        reminderListViewModel.loadReminders()
        var result = reminderListViewModel.showLoading.getOrAwaitValue()
        assertThat(result, `is`(true))
        mainCoroutineRule.resumeDispatcher()
        result = reminderListViewModel.showLoading.getOrAwaitValue()
        assertThat(result, `is`(false))
    }

}