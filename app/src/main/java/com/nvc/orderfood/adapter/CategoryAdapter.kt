package com.nvc.orderfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nvc.orderfood.R
import com.nvc.orderfood.databinding.ItemCategoryBinding
import com.nvc.orderfood.diff.CategoryDiff
import com.nvc.orderfood.listener.ItemCategoryListener
import com.nvc.orderfood.model.Category
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryAdapter @Inject constructor() : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    private val listCategory = arrayListOf<Category>()
    private var mItemCategoryListener: ItemCategoryListener? = null
    var item_selected = 0
    fun setOnClickListener(onClickListener: ItemCategoryListener) {
        this.mItemCategoryListener = onClickListener
    }

    class ViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category, itemSelected: Int) {
            binding.category = category
            binding.cardCategory
                .setCardBackgroundColor(
                    binding
                        .cardCategory
                        .resources
                        .getColor(
                            if (itemSelected == layoutPosition)
                                R.color.orange
                            else R.color.bg_cart
                        )
                )

            binding.executePendingBindings()

        }
    }

    fun submitData(temp: List<Category>) {
        val diff = DiffUtil.calculateDiff(CategoryDiff(listCategory, temp))
        listCategory.clear()
        listCategory.addAll(temp)
        diff.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        listCategory[position].let { ctg ->
            holder.itemView.setOnClickListener {
                item_selected = holder.layoutPosition
                notifyDataSetChanged()
                mItemCategoryListener?.onClickCategoryListener(ctg)
            }
            holder.bind(ctg, item_selected)

        }
    }

    override fun getItemCount(): Int {
        return listCategory.size
    }
}