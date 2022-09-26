package com.nvc.orderfood.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nvc.orderfood.databinding.ItemOrderItemBinding
import com.nvc.orderfood.diff.OrderItemDiff
import com.nvc.orderfood.listener.ItemOrderItemListener
import com.nvc.orderfood.model.Order
import com.nvc.orderfood.model.OrderItem

class OrderItemAdapter(private val order : Order) : RecyclerView.Adapter<OrderItemAdapter.ViewHolder>() {
    private val listOrderItems = arrayListOf<OrderItem>()
    private var mItemCartListener: ItemOrderItemListener? = null
    private lateinit var binding: ItemOrderItemBinding

    fun setOnClickListener(onClickListener: ItemOrderItemListener) {
        this.mItemCartListener = onClickListener
    }

    class ViewHolder(private val binding: ItemOrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(orderItem: OrderItem, mItemCartListener: ItemOrderItemListener) :ItemOrderItemBinding{
            binding.item = orderItem
            binding.position = bindingAdapterPosition
            binding.btnRate.setOnClickListener{
                mItemCartListener.onRate(orderItem)
            }
            binding.executePendingBindings()
            return binding
        }
    }



    fun submitData(temp: List<OrderItem>) {
        val diff = DiffUtil.calculateDiff(OrderItemDiff(listOrderItems, temp))
        listOrderItems.clear()
        listOrderItems.addAll(temp)
        diff.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding=
            ItemOrderItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        listOrderItems[position].let { orderItem ->
            holder.bind(orderItem, mItemCartListener!!).apply {
                btnRate.visibility = if(order.status==1 && orderItem.rate==0) View.VISIBLE else View.GONE
            }
            holder.itemView.setOnClickListener {
                mItemCartListener?.onClickFoodListener(orderItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return listOrderItems.size
    }

}