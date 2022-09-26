package com.nvc.orderfood.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.nvc.orderfood.databinding.DialogRateBinding
import com.nvc.orderfood.model.OrderItem
import com.nvc.orderfood.viewmodel.OrderDetailViewModel

class RateDialog : DialogFragment() {
    private lateinit var binding: DialogRateBinding
    private val viewModel : OrderDetailViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogRateBinding.inflate(inflater, container, false)
        binding.apply {
            val orderItem = arguments?.get("orderItem") as OrderItem
            item = orderItem
            var rate = orderItem.rate
            rating.onRatingBarChangeListener =
                RatingBar.OnRatingBarChangeListener { _, _, _ ->
                    rate = rating.rating.toInt()
                }
            btnSubmit.setOnClickListener {
                if(rate!=0){
                    viewModel.rateFood(orderItem,  rate)
                    dismiss()
                }else{
                    Toast.makeText(context,"Please rate star",Toast.LENGTH_SHORT).show()
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