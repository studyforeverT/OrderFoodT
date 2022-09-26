package com.nvc.orderfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nvc.orderfood.databinding.ItemFoodBinding
import com.nvc.orderfood.diff.FoodDiff
import com.nvc.orderfood.listener.ItemFoodListener
import com.nvc.orderfood.model.Food
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodAdapter @Inject constructor() : RecyclerView.Adapter<FoodAdapter.ViewHolder>() {
    private val listFood = arrayListOf<Food>()
    private var mItemCategoryListener: ItemFoodListener? = null

    fun setOnClickListener(onClickListener: ItemFoodListener) {
        this.mItemCategoryListener = onClickListener
    }

    class ViewHolder(private val binding: ItemFoodBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(food: Food,mListener: ItemFoodListener) {
            binding.food = food
            binding.btnAddCart.setOnClickListener{
                mListener.onClickAddToCart(food)
            }
            binding.executePendingBindings()
        }
    }

    fun submitData(temp: List<Food>) {
        val diff = DiffUtil.calculateDiff(FoodDiff(listFood, temp))
        listFood.clear()
        listFood.addAll(temp)
        diff.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemFoodBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        listFood[position].let { ctg ->
            holder.bind(ctg,mItemCategoryListener!!)
            holder.itemView.setOnClickListener {
                mItemCategoryListener?.onClickFoodListener(ctg)
            }
        }
    }

    override fun getItemCount(): Int {
        return listFood.size
    }
}