package com.rc.agecalculator.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.rc.agecalculator.exception.DateEmptyException
import com.rc.agecalculator.exception.FirstNameEmptyException
import com.rc.agecalculator.exception.LastNameEmptyException
import com.rc.agecalculator.presentation.viewmodel.model.UserDataModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.isA
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class UserViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var userDataModel: UserDataModel
    private lateinit var userViewModel: UserViewModel

    @Mock
    private lateinit var errorLivedataObserver: Observer<Throwable>

    @Before
    fun setUp() {
        userViewModel = UserViewModel()
    }

    @Test
    fun testCalculateAge_checkForOneYear() {
        val date = Calendar.getInstance()
        date.add(Calendar.YEAR, -1)
        userDataModel = UserDataModel(
            "",
            "",
            date.get(Calendar.DAY_OF_MONTH),
            date.get(Calendar.MONTH) + 1,
            date.get(Calendar.YEAR)
        )
        userViewModel.userDataModel = userDataModel
        userViewModel.calculateAge()
        userViewModel.ageLiveData.observeForever {
            assertEquals(it.months, 0)
            assertEquals(it.years, 1)
            assertEquals(it.days, 0)
        }
    }

    @Test
    fun testCalculateAge_checkForTwoYear() {
        val date = Calendar.getInstance()
        date.add(Calendar.YEAR, -2)
        userDataModel = UserDataModel(
            "",
            "",
            date.get(Calendar.DAY_OF_MONTH),
            date.get(Calendar.MONTH) + 1,
            date.get(Calendar.YEAR)
        )
        userViewModel.userDataModel = userDataModel
        userViewModel.calculateAge()
        userViewModel.ageLiveData.observeForever {
            assertEquals(it.months, 0)
            assertEquals(it.years, 2)
            assertEquals(it.days, 0)
        }
    }

    @Test
    fun testCalculateAge_checkForTwoMonth() {
        val date = Calendar.getInstance()
        date.add(Calendar.MONTH, -2)
        userDataModel = UserDataModel(
            "",
            "",
            date.get(Calendar.DAY_OF_MONTH),
            date.get(Calendar.MONTH) + 1,
            date.get(Calendar.YEAR)
        )
        userViewModel.userDataModel = userDataModel
        userViewModel.calculateAge()
        userViewModel.ageLiveData.observeForever {
            assertEquals(it.months, 2)
            assertEquals(it.years, 0)
            assertEquals(it.days, 0)
        }
    }

    @Test
    fun testCalculateAge_checkForTwoDays() {
        val date = Calendar.getInstance()
        date.add(Calendar.DAY_OF_MONTH, -2)
        userDataModel = UserDataModel(
            "",
            "",
            date.get(Calendar.DAY_OF_MONTH),
            date.get(Calendar.MONTH) + 1,
            date.get(Calendar.YEAR)
        )
        userViewModel.userDataModel = userDataModel
        userViewModel.calculateAge()
        userViewModel.ageLiveData.observeForever {
            assertEquals(it.months, 0)
            assertEquals(it.years, 0)
            assertEquals(it.days, 2)
        }
    }

    @Test
    fun testIsValidateInput_firstNameCheck() {
        val date = Calendar.getInstance()
        userDataModel = UserDataModel(
            "",
            "",
            date.get(Calendar.DAY_OF_MONTH),
            date.get(Calendar.MONTH) + 1,
            date.get(Calendar.YEAR)
        )
        userViewModel.userDataModel = userDataModel
        assertEquals(userViewModel.isValidateInput(), false)
        userViewModel.errorLiveData.observeForever(errorLivedataObserver)
        errorLivedataObserver.onChanged(isA(FirstNameEmptyException::class.java))
    }

    @Test
    fun testIsValidateInput_LastNameCheck() {
        val date = Calendar.getInstance()
        userDataModel = UserDataModel(
            "RC",
            "",
            date.get(Calendar.DAY_OF_MONTH),
            date.get(Calendar.MONTH) + 1,
            date.get(Calendar.YEAR)
        )
        userViewModel.userDataModel = userDataModel
        assertEquals(userViewModel.isValidateInput(), false)
        userViewModel.errorLiveData.observeForever(errorLivedataObserver)
        errorLivedataObserver.onChanged(isA(LastNameEmptyException::class.java))
    }

    @Test
    fun testIsValidateInput_DOBCheck() {
        userDataModel = UserDataModel(
            "RC",
            "RC",
            0, 0, 0
        )
        userViewModel.userDataModel = userDataModel
        assertEquals(userViewModel.isValidateInput(), false)
        userViewModel.errorLiveData.observeForever(errorLivedataObserver)
        errorLivedataObserver.onChanged(isA(DateEmptyException::class.java))
    }

    @Test
    fun testIsValidateInput_validInput() {
        val date = Calendar.getInstance()
        userDataModel = UserDataModel(
            "RC",
            "RC",
            date.get(Calendar.DAY_OF_MONTH),
            date.get(Calendar.MONTH) + 1,
            date.get(Calendar.YEAR)
        )
        userViewModel.userDataModel = userDataModel
        assertEquals(userViewModel.isValidateInput(), true)
    }
}