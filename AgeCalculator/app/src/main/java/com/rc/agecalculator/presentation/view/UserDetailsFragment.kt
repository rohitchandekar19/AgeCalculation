package com.rc.agecalculator.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.rc.agecalculator.R
import com.rc.agecalculator.databinding.FragmentUserDetailsBinding
import com.rc.agecalculator.presentation.viewmodel.UserViewModel
import com.rc.agecalculator.presentation.viewmodel.model.UserAgeModel

class UserDetailsFragment : Fragment() {

    private val sharedUserViewModel: UserViewModel by activityViewModels()
    private lateinit var binding: FragmentUserDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUserDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeAgeDetails()
        setUpUi()
        sharedUserViewModel.calculateAge()
        setClickListener()
    }

    private fun setUpUi() {
        binding.firstName.text = sharedUserViewModel.userDataModel.firstName
        binding.lastName.text = sharedUserViewModel.userDataModel.lastName
    }

    private fun setClickListener() {
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeAgeDetails() {
        sharedUserViewModel.ageLiveData.observe(viewLifecycleOwner, {
            updateUi(it)
        })
    }

    private fun updateUi(userAgeDetails: UserAgeModel) {
        binding.firstName.text = sharedUserViewModel.userDataModel.firstName
        binding.lastName.text = sharedUserViewModel.userDataModel.lastName
        binding.age.text = getString(
            R.string.age_text,
            userAgeDetails.years,
            userAgeDetails.months,
            userAgeDetails.days
        )
    }
}