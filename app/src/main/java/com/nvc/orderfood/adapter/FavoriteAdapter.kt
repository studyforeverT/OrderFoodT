package com.nvc.orderfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nvc.orderfood.databinding.ItemFavoriteBinding
import com.nvc.orderfood.diff.FavoriteDiff
import com.nvc.orderfood.listener.ItemFavoriteListener
import com.nvc.orderfood.model.Favorite
import com.nvc.orderfood.model.Food
import javax.inject.Inject

class FavoriteAdapter @Inject constructor() : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    private val listFavorite = arrayListOf<Favorite>()
    private var mItemFavoriteListener: ItemFavoriteListener? = null

    fun setOnClickListener(onClickListener: ItemFavoriteListener) {
        this.mItemFavoriteListener = onClickListener
    }

    class ViewHolder(private val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(favorite: Favorite, mItemFavoriteListener: ItemFavoriteListener) {
            binding.favorite = favorite
            binding.btnFavorite.setOnClickListener {
                mItemFavoriteListener.onClickFavoriteListener(favorite)
            }
            binding.tvPt.text = "(${String.format("%.0f", ((favorite.rating/5)*100))}%)"
            binding.executePendingBindings()
        }
    }

    fun submitData(temp: List<Favorite>) {
        val diff = DiffUtil.calculateDiff(FavoriteDiff(listFavorite, temp))
        listFavorite.clear()
        listFavorite.addAll(temp)
        diff.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemFavoriteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        listFavorite[position].let { favorite ->
            holder.bind(favorite, mItemFavoriteListener!!)
            holder.itemView.setOnClickListener {
                mItemFavoriteListener?.onClickItemListener(favorite)
            }
        }
    }

    override fun getItemCount(): Int {
        return listFavorite.size
    }

}