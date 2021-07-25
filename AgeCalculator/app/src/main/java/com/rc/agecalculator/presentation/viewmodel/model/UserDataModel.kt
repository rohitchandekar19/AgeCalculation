package com.rc.agecalculator.presentation.viewmodel.model

data class UserDataModel(
    var firstName: String = "",
    var lastName: String = "",
    var selectedDay: Int = 0,
    var selectedMonth: Int = 0,
    var selectedYear: Int = 0
)