package com.nvc.orderfood.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.nvc.orderfood.databinding.FragmentSignupBinding
import com.nvc.orderfood.model.User
import com.nvc.orderfood.utils.Convert.Companion.toTimeStampString
import com.nvc.orderfood.utils.UiState
import com.nvc.orderfood.viewmodel.SignInViewModel
import com.nvc.orderfood.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private val viewModelSignIn: SignInViewModel by activityViewModels()
    private val viewModelSignUp: SignUpViewModel by activityViewModels()
    private lateinit var binding: FragmentSignupBinding

    @Inject
    @Named("SignUpRef")
    lateinit var signUpRef: DatabaseReference
    lateinit var firebaseAuth: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

        binding.apply {
            signin.setOnClickListener {
                findNavController().popBackStack()
            }
            check()
            btnSignup.setOnClickListener {
                progress.visibility = View.VISIBLE
                viewModelSignUp.register(
                    edtEmail.text.toString(),
                    edtPassword.text.toString(),
                    getUserObj()
                )
            }
        }

        return binding.root
    }

    override fun onStart() {
        viewModelSignUp.reRegister()
        super.onStart()
    }


    private fun check() {
        viewModelSignUp.register.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                }
                is UiState.Success -> {
                    viewModelSignIn.logout()
                    findNavController().popBackStack()
                    Toast.makeText(context, state.data, Toast.LENGTH_SHORT).show()
                    binding.progress.visibility = View.GONE
                }
                is UiState.Failure -> {
                    Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
                    binding.progress.visibility = View.GONE
                }
                else -> {}
            }
        }
    }

    private fun getUserObj(): User {
        val gender: String
        val img: String
        if (binding.radioMale.isChecked) {
            gender = "Male"
            img = "https://tyhoang.ga/images/uploads/avt_b.png"
        } else {
            gender = "Female"
            img = "https://tyhoang.ga/images/uploads/avt_g.png"
        }
        binding.apply {
            return User(
                nameUser = edtName.text.toString(),
                addressUser = edtAddress.text.toString(),
                phoneUser = edtPhone.text.toString(),
                genderUser = gender,
                imageUser = img,
                timestamp = toTimeStampString(),
            )
        }
    }

    override fun onResume() {
        binding.apply {
            edtEmail.text.clear()
            edtPassword.text.clear()
            edtAddress.text.clear()
            edtPhone.text.clear()
            edtName.text.clear()
        }
        super.onResume()
    }
}