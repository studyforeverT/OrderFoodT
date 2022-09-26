package com.nvc.orderfood.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.nvc.orderfood.R
import com.nvc.orderfood.adapter.OrderItemAdapter
import com.nvc.orderfood.databinding.FragmentOrderDetailBinding
import com.nvc.orderfood.dialog.RateDialog
import com.nvc.orderfood.listener.ItemOrderItemListener
import com.nvc.orderfood.model.OrderItem
import com.nvc.orderfood.viewmodel.OrderDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*



@AndroidEntryPoint
class OrderDetailFragment : Fragment(), ItemOrderItemListener {
    private lateinit var binding: FragmentOrderDetailBinding
    private val orderDetailViewModel: OrderDetailViewModel by activityViewModels()
    lateinit var orderItemAdapter: OrderItemAdapter
    private val args: OrderDetailFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderDetailBinding.inflate(layoutInflater)
        binding.apply {
            refreshData()
            order = args.order
            btnBack.setOnClickListener {
                if(findNavController().previousBackStackEntry?.destination?.id==R.id.paymentFragment){
                    findNavController().navigate(R.id.action_orderDetailFragment_to_cartFragment)
                }else{
                    findNavController().popBackStack()
                }

            }
            orderItemAdapter = OrderItemAdapter(args.order)
            orderItemAdapter.setOnClickListener(this@OrderDetailFragment)
            rcvListFoodOrder.layoutManager = LinearLayoutManager(context)
            rcvListFoodOrder.adapter = orderItemAdapter
            orderDetailViewModel.listOrderItems.observe(viewLifecycleOwner) {
                orderItemAdapter.submitData(it)
            }

        }
        return binding.root
    }


    override fun onClickFoodListener(orderItem: OrderItem) {
        Log.d("TAGGG", "áddasdsa")
    }

    override fun onRate(orderItem: OrderItem) {
        val dialogFragment = RateDialog()
        val args = Bundle()
        args.putSerializable("orderItem", orderItem)
        dialogFragment.arguments = args
        fragmentManager?.let { it1 -> dialogFragment.show(it1, "Rate_food") }
    }
    private fun refreshData() {
        binding.shimmerOrderDetail.visibility = View.VISIBLE
        binding.shimmerOrderDetail.startShimmer()
        lifecycleScope.launch(Dispatchers.Main) {
            val deferredList = listOf(
                async(Dispatchers.IO) {
                    delay(600)
                },
                async(Dispatchers.Main) {
                    orderDetailViewModel.getListOrderItems(args.order.id)
                }
            )
            deferredList.awaitAll()
            binding.shimmerOrderDetail.visibility = View.INVISIBLE
            binding.wrapContainer.visibility=View.VISIBLE
            binding.shimmerOrderDetail.stopShimmer()
            orderDetailViewModel.listOrderItems.observe(viewLifecycleOwner){
                Log.d("TAGGGGG", "refreshData: update")
                orderItemAdapter.submitData(it)
            }
        }
    }

}