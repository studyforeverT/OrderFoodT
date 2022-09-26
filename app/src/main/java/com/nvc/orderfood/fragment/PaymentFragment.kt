package com.nvc.orderfood.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nvc.orderfood.adapter.CartAdapter
import com.nvc.orderfood.databinding.FragmentPaymentBinding
import com.nvc.orderfood.dialog.ShipmentDetailDialog
import com.nvc.orderfood.listener.ItemCartListener
import com.nvc.orderfood.model.Cart
import com.nvc.orderfood.model.Food
import com.nvc.orderfood.utils.Convert.Companion.toMoneyFormat
import com.nvc.orderfood.utils.MySharePre
import com.nvc.orderfood.viewmodel.CartViewModel
import com.nvc.orderfood.viewmodel.PaymentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PaymentFragment : Fragment(), ItemCartListener {
    private lateinit var binding: FragmentPaymentBinding
    private val paymentViewModel: PaymentViewModel by activityViewModels()
    private val cartViewModel: CartViewModel by activityViewModels()
    @Inject
    lateinit var cartAdapter: CartAdapter

    override fun onStart() {
        super.onStart()
        paymentViewModel.resetUserInfo()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentBinding.inflate(layoutInflater)

        binding.apply {
            rcvListFoodOrder.layoutManager = LinearLayoutManager(context)
            rcvListFoodOrder.itemAnimator = null
            cartAdapter.setOnClickListener(this@PaymentFragment)
            rcvListFoodOrder.adapter = cartAdapter
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
            paymentViewModel.apply {
                paymentViewModel.userInfo.observe(viewLifecycleOwner){
                    binding.user = it
                }
                btnPay.setOnClickListener {
                    pay()
                }
                listOrderItems.observe(viewLifecycleOwner) {
                    cartAdapter.submitData(it)
                }
                totalPrice.observe(viewLifecycleOwner) {
                    binding.totalStringFormat = it?.toMoneyFormat() ?: "0"
                }
                orderedDish.observe(viewLifecycleOwner) {
                    if (it != null) {
                        findNavController()
                            .navigate(
                                PaymentFragmentDirections
                                    .actionPaymentFragmentToOrderDetailFragment(it)
                            )
                    }


                }
            }
            btnChange.setOnClickListener {
                val dialogFragment = ShipmentDetailDialog()
                fragmentManager?.let { it1 -> dialogFragment.show(it1,"Shipment_Info") }
            }

        }
        return binding.root
    }

    override fun onClickFoodListener(cart: Cart) {
        val food = Food(
            cart.id,
            cart.name,
            cart.categoryID,
            cart.description,
            cart.image,
            cart.price,
            cart.rating
        )
        findNavController().navigate(
            PaymentFragmentDirections.actionPaymentFragmentToDetailFragment(
                food
            )
        )
    }

    override fun onPlus(cart: Cart) {
        if (cart.quantity < 99) {
            cartViewModel.plusCart(cart)
        }

    }

    override fun onMinus(cart: Cart) {
        if (cart.quantity > 1) {
            cartViewModel.minusCart(cart)
        }
    }
}