package com.nvc.orderfood.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nvc.orderfood.databinding.ItemSearchBinding
import com.nvc.orderfood.diff.FoodDiff
import com.nvc.orderfood.listener.ItemFoodListener
import com.nvc.orderfood.model.Food
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SearchAdapter @Inject constructor() : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    private val listFood = arrayListOf<Food>()
    private var mItemCategoryListener: ItemFoodListener? = null

    fun setOnClickListener(onClickListener: ItemFoodListener) {
        this.mItemCategoryListener = onClickListener
    }

    class ViewHolder(private val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(food: Food,mListener: ItemFoodListener) {
            binding.search = food
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
            ItemSearchBinding.inflate(
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
            setFadeAnimation(holder.itemView)
        }
    }

    override fun getItemCount(): Int {
        return listFood.size
    }

    private fun setFadeAnimation(view: View) {
        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 500
        view.startAnimation(anim)
    }
}