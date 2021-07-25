package com.rc.agecalculator.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rc.agecalculator.exception.DateEmptyException
import com.rc.agecalculator.exception.FirstNameEmptyException
import com.rc.agecalculator.exception.LastNameEmptyException
import com.rc.agecalculator.presentation.viewmodel.model.UserAgeModel
import com.rc.agecalculator.presentation.viewmodel.model.UserDataModel
import java.util.*

class UserViewModel : ViewModel() {
    var userDataModel: UserDataModel = UserDataModel()

    private val ageMutableLiveData = MutableLiveData<UserAgeModel>()
    val ageLiveData: LiveData<UserAgeModel> get() = ageMutableLiveData

    private val errorMutableLiveData = MutableLiveData<Throwable>()
    val errorLiveData: LiveData<Throwable> get() = errorMutableLiveData

    fun calculateAge() {
        val todayDate = Calendar.getInstance()
        val dobDate = Calendar.getInstance()
        dobDate.set(
            userDataModel.selectedYear,
            userDataModel.selectedMonth,
            userDataModel.selectedDay
        )
        val ageOfYear = todayDate.get(Calendar.YEAR) - dobDate.get(Calendar.YEAR)
        val ageOfMonth = todayDate.get(Calendar.MONTH) - dobDate.get(Calendar.MONTH) + 1
        val ageOfDay = todayDate.get(Calendar.DAY_OF_MONTH) - dobDate.get(Calendar.DAY_OF_MONTH)
        ageMutableLiveData.value = UserAgeModel(ageOfYear, ageOfMonth, ageOfDay)
    }

    fun isValidateInput(): Boolean {
        return when {
            userDataModel.firstName.isEmpty() -> {
                errorMutableLiveData.value = FirstNameEmptyException()
                false
            }
            userDataModel.lastName.isEmpty() -> {
                errorMutableLiveData.value = LastNameEmptyException()
                false
            }
            userDataModel.selectedDay == 0 -> {
                errorMutableLiveData.value = DateEmptyException()
                false
            }
            else -> {
                true
            }
        }
    }
}