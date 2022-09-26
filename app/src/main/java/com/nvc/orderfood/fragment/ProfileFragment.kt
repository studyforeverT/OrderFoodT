package com.nvc.orderfood.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.nvc.orderfood.databinding.FragmentProfileBinding
import com.nvc.orderfood.service.NotificationService
import com.nvc.orderfood.utils.MySharePre
import com.nvc.orderfood.viewmodel.ProfileViewModel
import com.nvc.orderfood.viewmodel.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by activityViewModels()
    private val signInViewModel: SignInViewModel by activityViewModels()

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var mySharePre: MySharePre


    override fun onStart() {
        super.onStart()
        viewModel.getDataProfile()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

        binding.apply {
            viewModel.user.observe(viewLifecycleOwner) {
                profile = it
            }
            txtProfileUser.setOnClickListener {
                val action = ProfileFragmentDirections.actionProfileFragmentToMyProfileFragment()
                findNavController().navigate(action)
            }

            txtChangePasswordUser.setOnClickListener {
                val action =
                    ProfileFragmentDirections.actionProfileFragmentToChangePasswordFragment()
                findNavController().navigate(action)
            }

            txtHistoryCartUser.setOnClickListener {
                val action = ProfileFragmentDirections.actionProfileFragmentToOrderHistoryFragment()
                findNavController().navigate(action)
            }

            txtContactUs.setOnClickListener {
                val action = ProfileFragmentDirections.actionProfileFragmentToContactUsFragment()
                findNavController().navigate(action)
            }

            btnLogOut.setOnClickListener {
                val user = firebaseAuth.currentUser
                if (user != null) {
                    signInViewModel.logout()
                    mySharePre.removeKey("user")
                    stopNotificationService()
                    findNavController().popBackStack()
                }

            }
            return binding.root
        }

    }

    private fun stopNotificationService() {
        val intent = Intent(context, NotificationService::class.java)
        context?.stopService(intent)

    }
}