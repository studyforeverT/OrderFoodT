package com.nvc.orderfood.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.nvc.orderfood.adapter.CartAdapter
import com.nvc.orderfood.databinding.FragmentCartBinding
import com.nvc.orderfood.listener.ItemCartListener
import com.nvc.orderfood.model.Cart
import com.nvc.orderfood.model.Food
import com.nvc.orderfood.utils.Convert.Companion.toMoneyFormat
import com.nvc.orderfood.viewmodel.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CartFragment : Fragment(), ItemCartListener {
    private lateinit var binding: FragmentCartBinding
    private val cartViewModel: CartViewModel by activityViewModels()

    @Inject
    lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(layoutInflater)
        cartAdapter.setOnClickListener(this)
        cartViewModel.listCart.observe(viewLifecycleOwner) {
            cartAdapter.submitData(it)
            binding.quantity = it.size
            binding.layoutCartNull.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE

        }
        cartViewModel.total.observe(viewLifecycleOwner) {
            binding.total = it?.toMoneyFormat() ?: "0"
        }
        binding.apply {
            rcvListCart.itemAnimator = null
            rcvListCart.layoutManager = LinearLayoutManager(context)
            rcvListCart.adapter = cartAdapter
            layoutTotalPrice.setOnClickListener{
                if( cartViewModel.total.value!=null){
                    findNavController().navigate(CartFragmentDirections.actionCartFragmentToPaymentFragment())
                }else{
                    Toast.makeText(context,"There is no products in your cart",Toast.LENGTH_SHORT).show()
                }

            }

        }
        enableSwipeToDeleteAndUndo()
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
        Log.d("TAGGGG", "onClickFoodListener: ${cart.rating}")
        findNavController().navigate(CartFragmentDirections.actionCartFragmentToDetailFragment(food))
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

    private fun enableSwipeToDeleteAndUndo() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.layoutPosition
                val deletedCart: Cart? = cartViewModel.listCart.value?.get(position)
                if (deletedCart != null) {
                    cartViewModel.deleteCart(deletedCart)
                }
                Snackbar.make(
                    binding.rcvListCart,
                    "Deleted " + deletedCart?.name,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(
                        "Undo"
                    ) {
                        if (deletedCart != null) {
                            cartViewModel.insertCart(deletedCart)
                        }
                    }.show()
            }
        }).attachToRecyclerView(binding.rcvListCart)
    }
}