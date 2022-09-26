package com.nvc.orderfood.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nvc.orderfood.adapter.SearchAdapter
import com.nvc.orderfood.databinding.FragmentSearchBinding
import com.nvc.orderfood.listener.ItemFoodListener
import com.nvc.orderfood.model.Cart
import com.nvc.orderfood.model.Food
import com.nvc.orderfood.viewmodel.CartViewModel
import com.nvc.orderfood.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment(), ItemFoodListener{

    private lateinit var binding: FragmentSearchBinding
    private val searchViewModel: SearchViewModel by viewModels()
    private val cartViewModel: CartViewModel by activityViewModels()

    @Inject
    lateinit var searchAdapter : SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentSearchBinding.inflate(layoutInflater)

        binding.apply {

            btnSearch.setOnClickListener {
                if (binding.edtSearch.text.toString() != "") {
                    searchViewModel.updateKey(edtSearch.text.toString())
                } else {
                    Toast.makeText(context, "Please enter key!", Toast.LENGTH_LONG).show()
                }
            }

            lifecycleScope.launch {
                searchViewModel.key.debounce(350L).collect{
                        searchViewModel.searchByName(it)
                }
            }

            rcvsearch.layoutManager = LinearLayoutManager(context)
            rcvsearch.adapter = searchAdapter
            rcvsearch.itemAnimator = null
            searchAdapter.setOnClickListener(this@SearchFragment)
            searchViewModel.result.observe(viewLifecycleOwner) {
                searchAdapter.submitData(it)
            }
            backTimKiem.setOnClickListener {
                findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToHomeFragment())
            }

        }

        return binding.root
    }

    override fun onClickFoodListener(food: Food) {
        findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToDetailFragment(food))
    }

    override fun onClickAddToCart(food: Food) {
        val newCart = Cart(
            food.id,
            food.name,
            food.categoryID,
            food.description,
            food.image,
            food.price
        )
        lifecycleScope.launch {
            cartViewModel.addToCart(newCart)
            Toast.makeText(context, "Add Successfully", Toast.LENGTH_SHORT).show()
        }

    }

}