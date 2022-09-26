package com.nvc.orderfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nvc.orderfood.databinding.ItemCartBinding
import com.nvc.orderfood.diff.CartDiff
import com.nvc.orderfood.listener.ItemCartListener
import com.nvc.orderfood.model.Cart
import javax.inject.Inject

class CartAdapter @Inject constructor() : RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    private val listOrder = arrayListOf<Cart>()
    private var mItemCartListener: ItemCartListener? = null

    fun setOnClickListener(onClickListener: ItemCartListener) {
        this.mItemCartListener = onClickListener
    }

    class ViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cart: Cart,mItemCartListener: ItemCartListener) {
            binding.btnPlus.setOnClickListener{
                mItemCartListener.onPlus(cart)
            }
            binding.btnMinus.setOnClickListener{
                mItemCartListener.onMinus(cart)
            }
            binding.item = cart
            binding.position = bindingAdapterPosition
            binding.executePendingBindings()
        }
    }

    fun submitData(temp: List<Cart>) {
        val diff = DiffUtil.calculateDiff(CartDiff(listOrder, temp))
        listOrder.clear()
        listOrder.addAll(temp)
        diff.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCartBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        listOrder[position].let { cart ->
            holder.bind(cart, mItemCartListener!!)
            holder.itemView.setOnClickListener {
                mItemCartListener?.onClickFoodListener(cart)
            }
        }
    }

    override fun getItemCount(): Int {
        return listOrder.size
    }

}