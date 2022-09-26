package com.nvc.orderfood.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.nvc.orderfood.databinding.FragmentMyProfileBinding
import com.nvc.orderfood.model.User
import com.nvc.orderfood.viewmodel.ProfileViewModel
import com.nvc.orderfood.viewmodel.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MyProfileFragment : Fragment() {
    private lateinit var binding: FragmentMyProfileBinding
    private val viewModel: ProfileViewModel by activityViewModels()
    private val signInViewModel: SignInViewModel by activityViewModels()
    private val PICK_IMAGE_REQUEST = 22
    private var pickedPhoto: Uri? = null
    private var pickedBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getDataProfile()
        viewModel.updateStatus()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentMyProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

        binding.apply {
            observe()
            viewModel.user.observe(viewLifecycleOwner) {
                profileEdt = it
                if (it.genderUser == "Male") {
                    binding.radioMale.isChecked = true
                } else {
                    binding.radioFemale.isChecked = true
                }
            }
            profileImage.setOnClickListener {
                SelectImage()
            }
            btnUpdateUserProfile.setOnClickListener {
                if (getUserObj() != null) {
                    if (pickedPhoto != null) {
                        viewModel.updateDataUserImage(pickedPhoto!!, getUserObj()!!)
                    } else {
                        viewModel.updateDataUser(getUserObj()!!)
                    }
                } else {
                    Toast.makeText(context, "Please fill in all information", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }

        return binding.root
    }

    private fun SelectImage() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        } else {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, PICK_IMAGE_REQUEST)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST
            && resultCode == RESULT_OK
            && data != null
        ) {
            pickedPhoto = data.data
            if (pickedPhoto != null) {
                if (Build.VERSION.SDK_INT >= 28) {
                    val source =
                        ImageDecoder.createSource(requireActivity().contentResolver, pickedPhoto!!)
                    pickedBitmap = ImageDecoder.decodeBitmap(source)
                    binding.profileImage.setImageBitmap(pickedBitmap)
                } else {
                    pickedBitmap = MediaStore.Images.Media.getBitmap(
                        requireActivity().contentResolver, pickedPhoto
                    )
                    binding.profileImage.setImageBitmap(pickedBitmap)
                }
            }
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun observe() {
        viewModel.statusUpdateUser.observe(viewLifecycleOwner) { state ->
            when (state) {
                true -> {
                    Toast.makeText(context, "Update Successfully", Toast.LENGTH_SHORT).show()
                }
                else -> {
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getUserObj(): User? {
        val gender: String = if (binding.radioMale.isChecked) {
            "Male"
        } else {
            "Female"
        }



        binding.apply {
            val id = edtIdProfile.text.toString()
            val name = edtNameUserEditProfile.text.toString()
            val address = edtAddressEditProfile.text.toString()
            val phone = edtPhoneEditProfile.text.toString()
            val image = edtImgEditProfile.text.toString()
            val timestamp = edtTimeStampEditProfile.text.toString()
            if (name == "" || address == "" || phone == "") {
                return null
            } else {
                return User(
                    id = id,
                    nameUser = name,
                    addressUser = address,
                    phoneUser = phone,
                    genderUser = gender,
                    imageUser = image,
                    timestamp = timestamp,
                )
            }
        }
    }
}