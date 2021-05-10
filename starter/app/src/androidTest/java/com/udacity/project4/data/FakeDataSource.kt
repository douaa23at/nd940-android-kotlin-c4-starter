package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeRemindersLocalRepository(var reminders: MutableList<ReminderDTO>? = mutableListOf()) : ReminderDataSource {

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        return Result.Success(ArrayList(reminders))
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        reminders?.add(reminder)
    }

    override suspend fun getReminder(id: String) : Result<ReminderDTO> {
        val reminder = reminders?.find { reminder -> reminder.id == id }
        return if (reminder == null)  Result.Error("", null)
        else Result.Success(reminder)
    }

    override suspend fun deleteAllReminders() {
        reminders?.clear()
    }


}