package com.nvc.orderfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nvc.orderfood.databinding.ItemOrderHisoryBinding
import com.nvc.orderfood.diff.OrderDiff
import com.nvc.orderfood.listener.ItemOrderListener
import com.nvc.orderfood.model.Order
import javax.inject.Inject

class OrderAdapter @Inject constructor() : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {
    private val listOrder = arrayListOf<Order>()
    private var mItemCartListener: ItemOrderListener? = null

    fun setOnClickListener(onClickListener: ItemOrderListener) {
        this.mItemCartListener = onClickListener
    }

    class ViewHolder(private val binding: ItemOrderHisoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.item = order
            binding.executePendingBindings()
        }
    }

    fun submitData(temp: List<Order>) {
        val diff = DiffUtil.calculateDiff(OrderDiff(listOrder, temp))
        listOrder.clear()
        listOrder.addAll(temp)
        diff.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemOrderHisoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        listOrder[position].let { order ->
            holder.bind(order)
            holder.itemView.setOnClickListener {
                mItemCartListener?.onClickOrderListener(order)
            }
        }
    }

    override fun getItemCount(): Int {
        return listOrder.size
    }

}