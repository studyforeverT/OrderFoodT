package com.nvc.orderfood.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nvc.orderfood.adapter.OrderAdapter
import com.nvc.orderfood.databinding.FragmentOrderHistoryBinding
import com.nvc.orderfood.listener.ItemOrderListener
import com.nvc.orderfood.model.Order
import com.nvc.orderfood.viewmodel.OrderHistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class OrderHistoryFragment : Fragment(), ItemOrderListener {

    private lateinit var binding: FragmentOrderHistoryBinding
    private val viewModel: OrderHistoryViewModel by activityViewModels()

    @Inject
    lateinit var orderAdapter: OrderAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentOrderHistoryBinding.inflate(layoutInflater)
        orderAdapter.setOnClickListener(this)
        refreshData()
        viewModel.listOrder.observe(viewLifecycleOwner) {
            orderAdapter.submitData(it)
            binding.layoutCartNull.visibility =
                if (it.isEmpty()) View.VISIBLE else View.GONE
        }

        binding.apply {
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
            rcvListOrderHistory.itemAnimator = null
            rcvListOrderHistory.adapter = orderAdapter
            rcvListOrderHistory.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true).apply {
                    stackFromEnd = true
                }
            swipeLayout.setOnRefreshListener {
                refreshData()
            }
        }
        return binding.root
    }

    private fun refreshData() {
        binding.swipeLayout.visibility = View.INVISIBLE
        binding.layoutCartNull.visibility = View.INVISIBLE
        binding.shimmerLayout.visibility = View.VISIBLE
        binding.shimmerLayout.startShimmer()
        lifecycleScope.launch(Dispatchers.Main) {
            val deferredList = listOf(
                async(Dispatchers.IO) {
                    delay(500)
                },
                async(Dispatchers.Main) {
                    binding.layoutCartNull.visibility = View.INVISIBLE
                    viewModel.getDataRemote()
                }
            )
            deferredList.awaitAll()
            if(viewModel.listOrder.value!!.isEmpty()){
                binding.layoutCartNull.visibility = View.VISIBLE
                binding.swipeLayout.visibility = View.INVISIBLE
            }
            binding.swipeLayout.visibility = View.VISIBLE
            binding.shimmerLayout.visibility = View.INVISIBLE
            binding.swipeLayout.isRefreshing = false
            binding.shimmerLayout.stopShimmer()

        }
    }

    override fun onClickOrderListener(order: Order) {
//        val order = Order(
//            order.id,
//            order.userID,
//            order.timestamp,
//            order.address,
//            order.phone,
//            order.receiverName,
//            order.status,
//            order.totalPrice,
//        )
        val action =
            OrderHistoryFragmentDirections.actionOrderHistoryFragmentToOrderDetailFragment(order)
        findNavController().navigate(action)
    }

}