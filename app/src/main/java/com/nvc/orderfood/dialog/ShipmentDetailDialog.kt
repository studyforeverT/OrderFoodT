package com.nvc.orderfood.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.nvc.orderfood.databinding.DialogShipmentDetailBinding
import com.nvc.orderfood.viewmodel.PaymentViewModel


class ShipmentDetailDialog : DialogFragment() {
    private lateinit var binding: DialogShipmentDetailBinding
    private val viewModel : PaymentViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogShipmentDetailBinding.inflate(inflater, container, false)
        binding.apply {
            paymentVM = viewModel
            btnSubmit.setOnClickListener {
                val name =  edtName.text.toString()
                val phone =  edtPhone.text.toString()
                val address =  edtAddress.text.toString()
                if(name!=""&&phone!=""&&address!=""){
                    viewModel.changeUserInfo(name, phone, address)
                    dialog?.dismiss()
                }else{
                    Toast.makeText(context,"Please fill in all information",Toast.LENGTH_LONG).show()
                }

            }
        }
        return binding.root
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }

}