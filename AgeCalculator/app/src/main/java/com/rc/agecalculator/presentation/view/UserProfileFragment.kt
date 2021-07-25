package com.rc.agecalculator.presentation.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.rc.agecalculator.R
import com.rc.agecalculator.databinding.FragmentUserProfileBinding
import com.rc.agecalculator.exception.DateEmptyException
import com.rc.agecalculator.exception.FirstNameEmptyException
import com.rc.agecalculator.exception.LastNameEmptyException
import com.rc.agecalculator.extension.hideKeyboard
import com.rc.agecalculator.extension.showKeyboard
import com.rc.agecalculator.presentation.viewmodel.UserViewModel
import java.util.*

class UserProfileFragment : Fragment() {

    private val sharedUserViewModel: UserViewModel by activityViewModels()
    private lateinit var binding: FragmentUserProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.firstNameEditText.showKeyboard()
        setOnClickListener()
        observeValidationError()
    }

    private fun observeValidationError() {
        sharedUserViewModel.errorLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is FirstNameEmptyException -> {
                    showToast(getString(R.string.first_name_error_text))
                }
                is LastNameEmptyException -> {
                    showToast(getString(R.string.last_name_error_text))
                }
                is DateEmptyException -> {
                    showToast(getString(R.string.dob_error_text))
                }
                else -> {
                    showToast(getString(R.string.common_error))
                }
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun setOnClickListener() {
        binding.nextButton.setOnClickListener {
            it.hideKeyboard()
            sharedUserViewModel.userDataModel.firstName = binding.firstNameEditText.text.toString()
            sharedUserViewModel.userDataModel.lastName = binding.lastNameEditText.text.toString()

            if (sharedUserViewModel.isValidateInput()) {
                findNavController().navigate(R.id.action_userProfileFragment_to_userDetailsFragment)
            }
        }
        binding.dobDate.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val today = Calendar.getInstance()
        if (sharedUserViewModel.userDataModel.selectedDay != 0) {
            today.set(
                sharedUserViewModel.userDataModel.selectedYear,
                sharedUserViewModel.userDataModel.selectedMonth - 1,
                sharedUserViewModel.userDataModel.selectedDay
            )
        }
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                sharedUserViewModel.userDataModel.selectedDay = dayOfMonth
                sharedUserViewModel.userDataModel.selectedMonth = monthOfYear + 1
                sharedUserViewModel.userDataModel.selectedYear = year
                binding.dobDate.setText("""$year - ${monthOfYear + 1} - $dayOfMonth""")
            },
            today.get(Calendar.YEAR),
            today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }
}