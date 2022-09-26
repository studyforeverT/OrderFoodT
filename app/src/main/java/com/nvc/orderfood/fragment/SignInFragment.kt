package com.nvc.orderfood.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.nvc.orderfood.databinding.FragmentSigninBinding
import com.nvc.orderfood.utils.MySharePre
import com.nvc.orderfood.utils.UiState
import com.nvc.orderfood.viewmodel.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : Fragment() {
    private val viewModel: SignInViewModel by activityViewModels()
    private lateinit var binding: FragmentSigninBinding
    private var rememberMe: Boolean = false

    @Inject
    lateinit var mySharePre: MySharePre

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSigninBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        rememberMe = mySharePre.getBoolean("rememberMe")
        if (rememberMe) {
            val user = firebaseAuth.currentUser
            if (user != null) {
                val action = SignInFragmentDirections.actionSignInFragmentToHomeFragment()
                findNavController().navigate(action)
            }
        }

        binding.apply {
            cbRemember.isChecked = rememberMe
            val usernamePrefs = mySharePre.getString("username")
            val passwordPrefs = mySharePre.getString("password")
            if (usernamePrefs != "" && passwordPrefs != "") {
                edtEmail.setText(usernamePrefs)
                edtPassword.setText(passwordPrefs)
            }
            cbRemember.setOnCheckedChangeListener { _, isChecked ->
                rememberMe = isChecked
            }
            signup.setOnClickListener {
                val action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
                findNavController().navigate(action)
            }
            check()
            btnLogin.setOnClickListener {
                binding.progress.visibility = View.VISIBLE
                viewModel.login(edtEmail.text.toString(), edtPassword.text.toString())
            }

            tvForgotPassword.setOnClickListener {
                val action = SignInFragmentDirections.actionSignInFragmentToForgotPasswordFragment()
                findNavController().navigate(action)
            }
        }
        return binding.root
    }


    private fun check() {
        viewModel.login.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                }
                is UiState.Success -> {
                    val action = SignInFragmentDirections.actionSignInFragmentToHomeFragment()
                    findNavController().navigate(action)
                    viewModel.resetState()
                    saveData(rememberMe)
                    binding.progress.visibility = View.GONE
                }
                is UiState.Failure -> {
                    Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
                    binding.progress.visibility = View.GONE
                }

            }

        }
    }

    private fun saveData(isRemember: Boolean) {
        mySharePre.putBoolean("rememberMe", isRemember)
    }

    override fun onResume() {
        binding.apply {
            edtEmail.text.clear()
            edtPassword.text.clear()
        }
        super.onResume()
    }

}