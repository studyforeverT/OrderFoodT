package com.nvc.orderfood.fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nvc.orderfood.databinding.FragmentContactUsBinding


class ContactUsFragment : Fragment() {

    private lateinit var binding : FragmentContactUsBinding
    private var isAllFabsVisible: Boolean? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactUsBinding.inflate(layoutInflater)
        binding.apply {

            addCallFab.visibility = View.GONE
            addEmailFab.visibility = View.GONE

            isAllFabsVisible = false

            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }

            addFab.setOnClickListener {
                if (!isAllFabsVisible!!) {
                    addEmailFab.show()
                    addCallFab.show()
                    isAllFabsVisible = true
                } else {
                    addEmailFab.hide()
                    addCallFab.hide()
                    isAllFabsVisible = false
                }
            }

            addCallFab.setOnClickListener {
                val actionCall = Intent(Intent.ACTION_DIAL)
                actionCall.data = Uri.parse("tel:0769956471")
                startActivity(actionCall)
            }

            addEmailFab.setOnClickListener {
                val mailto = "mailto:nhomvoicon@gmail.com" +
                        "?cc=" +
                        "&subject=" + Uri.encode("Gửi phản hồi cho chúng tôi nhé!") +
                        "&body=" + Uri.encode("Phản hồi của bạn giúp chúng tôi cải thiện hơn.")
                val emailIntent = Intent(Intent.ACTION_SENDTO)
                emailIntent.data = Uri.parse(mailto)
                try {
                    startActivity(emailIntent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(context, "Error to open email app", Toast.LENGTH_SHORT).show()
                }
            }

        }
        return binding.root
    }
}