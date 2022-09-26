package com.nvc.orderfood.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.nvc.orderfood.databinding.FragmentChangePasswordBinding
import com.nvc.orderfood.utils.UiState
import com.nvc.orderfood.viewmodel.ChangePasswordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment : Fragment() {
    private lateinit var binding: FragmentChangePasswordBinding
    private val viewModel: ChangePasswordViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentChangePasswordBinding.inflate(layoutInflater)
        binding.apply {
            btnChangePassword.setOnClickListener {
                if (edtPassword.text.toString()!=""){
                    viewModel.updatePassword(edtPassword.text.toString())
                }else{
                    Toast.makeText(context,"Please enter the new password",Toast.LENGTH_LONG).show()
                }

            }
            observe()
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
        return binding.root
    }

    private fun observe() {
        viewModel.changePasswordUser.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                }
                is UiState.Success -> {
                    Toast.makeText(context, state.data, Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                is UiState.Failure -> {
                    Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }
}