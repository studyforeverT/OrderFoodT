package com.nvc.orderfood.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.nvc.orderfood.databinding.FragmentForgotPasswordBinding
import com.nvc.orderfood.utils.UiState
import com.nvc.orderfood.viewmodel.ForgotPasswordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment : Fragment() {

    private lateinit var binding: FragmentForgotPasswordBinding
    private val viewModel: ForgotPasswordViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentForgotPasswordBinding.inflate(layoutInflater)
        binding.apply {
            btnChangePassword.setOnClickListener {
                val email = edtEmail.text.toString()
                if (email!=""){
                    viewModel.sendEmailToResetPasswordUser(email)
                }else{
                    Toast.makeText(context,"Please enter your email",Toast.LENGTH_LONG).show()
                }
            }
            check()
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
        return binding.root
    }

    fun check() {
        viewModel.forgotPasswordUser.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    //loading
                }
                is UiState.Success -> {
                    binding.btnChangePassword.visibility = View.GONE
                    binding.btnBack.visibility = View.VISIBLE
                }
                is UiState.Failure -> {
                    Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }
}