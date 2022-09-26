package com.nvc.orderfood.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.nvc.orderfood.R
import com.nvc.orderfood.databinding.FragmentDetailBinding
import com.nvc.orderfood.model.Cart
import com.nvc.orderfood.model.Favorite
import com.nvc.orderfood.viewmodel.CartViewModel
import com.nvc.orderfood.viewmodel.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private lateinit var _binding: FragmentDetailBinding
    private val binding get() = _binding
    private val cartViewModel: CartViewModel by activityViewModels()
    private val favoriteViewModel: FavoriteViewModel by activityViewModels()
    private val args: DetailFragmentArgs by navArgs()
    lateinit var firebaseAuth: FirebaseAuth
    private var favoriteList: MutableList<Favorite> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        getData()
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        binding.apply {
            favoriteViewModel.loveOfFavoriteItems.observe(viewLifecycleOwner) {
                if (it) {
                    btnFavorite.setImageResource(R.drawable.ic_favorite_2)
                } else {
                    btnFavorite.setImageResource(R.drawable.ic_favorite_1)
                }
            }
            val item = args.food
            food = args.food
            btnAddCart.setOnClickListener {
                val newCart = Cart(
                    item.id,
                    item.name,
                    item.categoryID,
                    item.description,
                    item.image,
                    item.price
                )
                cartViewModel.addToCart(newCart)
            }
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
            btnFavorite.setOnClickListener {
                val favorite = Favorite(
                    item.id,
                    item.name,
                    item.categoryID,
                    item.description,
                    item.image,
                    item.price
                )
                if (favoriteList.any { it.id == item.id }) {
                    favoriteViewModel.removeItemFavorite(favorite)
                } else {
                    favoriteViewModel.addToFavorite(favorite)
                }
            }
        }
        return binding.root
    }

    private fun getData() {
        favoriteViewModel.favoriteList.observe(viewLifecycleOwner) { items ->
            if (items.any { it.id == args.food.id }) {
                favoriteViewModel.loveOfFavoriteItems.postValue(true)
            } else {
                favoriteViewModel.loveOfFavoriteItems.postValue(false)
            }
            favoriteList.clear()
            favoriteList.addAll(items.toMutableList())
        }
    }
}