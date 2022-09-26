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
import com.nvc.orderfood.adapter.NotificationAdapter
import com.nvc.orderfood.databinding.FragmentNotificationBinding
import com.nvc.orderfood.listener.ItemNotificationListener
import com.nvc.orderfood.model.Notification
import com.nvc.orderfood.viewmodel.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class NotificationFragment : Fragment(), ItemNotificationListener {
    private lateinit var binding: FragmentNotificationBinding
    private val notificationViewModel: NotificationViewModel by activityViewModels()

    @Inject
    lateinit var notificationAdapter: NotificationAdapter

    override fun onStart() {
        super.onStart()
        notificationViewModel.getDataRemote()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationBinding.inflate(layoutInflater)
        notificationAdapter.setOnClickListener(this)
        notificationViewModel.listNotifications.observe(viewLifecycleOwner) {
            notificationAdapter.submitData(it)
            binding.layoutCartNull.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE

        }
        binding.apply {
            rcvListNotification.itemAnimator = null
            rcvListNotification.layoutManager = LinearLayoutManager(context)
            rcvListNotification.adapter = notificationAdapter
        }
        return binding.root
    }


    override fun onClickItem(notification: Notification) {
        lifecycleScope.launch(Dispatchers.IO) {
            notificationViewModel.getOrder(notification.orderID).collect{
                withContext(Dispatchers.Main){
                    if(it!=null) findNavController().navigate(NotificationFragmentDirections.actionGlobalOrderDetailFragment(it))
                }

            }
        }
    }
}