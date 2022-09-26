package com.nvc.orderfood.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.nvc.orderfood.adapter.FavoriteAdapter
import com.nvc.orderfood.databinding.FragmentFavoriteBinding
import com.nvc.orderfood.listener.ItemFavoriteListener
import com.nvc.orderfood.model.Favorite
import com.nvc.orderfood.model.Food
import com.nvc.orderfood.viewmodel.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteFragment : Fragment(), ItemFavoriteListener {
    private lateinit var binding: FragmentFavoriteBinding
    private val viewModel: FavoriteViewModel by activityViewModels()

    @Inject
    lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(layoutInflater)
        favoriteAdapter.setOnClickListener(this)
        viewModel.favoriteList.observe(viewLifecycleOwner) {
            favoriteAdapter.submitData(it)
            binding.layoutFavoriteNull.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        }
        binding.apply {
            rcvListFavorite.itemAnimator = null
            rcvListFavorite.layoutManager =
                GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            rcvListFavorite.adapter = favoriteAdapter

        }
        return binding.root
    }

    override fun onClickFavoriteListener(favorite: Favorite) {
        viewModel.removeItemFavorite(favorite)

    }

    override fun onClickItemListener(favorite: Favorite) {
        val food = Food(
            favorite.id,
            favorite.name,
            favorite.categoryID,
            favorite.description,
            favorite.image,
            favorite.price,
            favorite.rating
        )
        findNavController().navigate(
            FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(
                food
            )
        )
    }

    override fun onClickRatingListener(rating: Float) {
        Toast.makeText(requireActivity().applicationContext, "$rating", Toast.LENGTH_SHORT).show()
    }
}