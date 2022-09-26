package com.nvc.orderfood.fragment

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DatabaseReference
import com.nvc.orderfood.adapter.CategoryAdapter
import com.nvc.orderfood.adapter.FoodAdapter
import com.nvc.orderfood.databinding.FragmentHomeBinding
import com.nvc.orderfood.listener.ItemCategoryListener
import com.nvc.orderfood.listener.ItemFoodListener
import com.nvc.orderfood.model.Cart
import com.nvc.orderfood.model.Category
import com.nvc.orderfood.model.Food
import com.nvc.orderfood.service.NotificationService
import com.nvc.orderfood.utils.CheckNetwork
import com.nvc.orderfood.utils.Constant
import com.nvc.orderfood.utils.MySharePre
import com.nvc.orderfood.viewmodel.CartViewModel
import com.nvc.orderfood.viewmodel.FavoriteViewModel
import com.nvc.orderfood.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Named


@AndroidEntryPoint
class HomeFragment : Fragment(), ItemFoodListener, ItemCategoryListener {
    private val viewModel: HomeViewModel by activityViewModels()
    private val cartViewModel: CartViewModel by activityViewModels()
    private val favoriteViewModel: FavoriteViewModel by activityViewModels()

    @Inject
    lateinit var foodAdapter: FoodAdapter

    @Inject
    lateinit var categoryAdapter: CategoryAdapter

    private lateinit var binding: FragmentHomeBinding

    @Inject
    @Named("FoodRef")
    lateinit var foodRef: DatabaseReference

    @Inject
    lateinit var mySharePre: MySharePre

    @Inject
    lateinit var checkNetwork: CheckNetwork

    override fun onStart() {
        super.onStart()
        cartViewModel.getDataRemote()
        favoriteViewModel.getDataRemote()
        if (context?.isMyServiceRunning(NotificationService::class.java) == false) {
            val intent = Intent(context, NotificationService::class.java)
            context?.startService(intent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        binding.apply {
            refreshData()
            toolBar.title = "Menu"
            categoryAdapter.setOnClickListener(this@HomeFragment)
            foodAdapter.setOnClickListener(this@HomeFragment)
            rcvListCategory.itemAnimator = null
            rcvListCategory.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rcvListCategory.adapter = categoryAdapter
            rcvListFood.itemAnimator = null
            rcvListFood.layoutManager =
                GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            rcvListFood.adapter = foodAdapter
            swipeLayout.setOnRefreshListener {
                refreshData()
            }

            btnSearch.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment())
            }
            cartViewModel.message.observe(viewLifecycleOwner) {
                if (it != null) {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    cartViewModel.resetMessage()
                }
            }
        }
        viewModel.apply {
            listFood.observe(viewLifecycleOwner) {
                foodAdapter.submitData(it)
            }
            listCategory.observe(viewLifecycleOwner) {
                categoryAdapter.submitData(it)
            }
            categoryIdActive.observe(viewLifecycleOwner) {
                this@HomeFragment.getDataFoodByCategory(it)
            }
        }
        return binding.root
    }

    private fun refreshData() {
        binding.swipeLayout.visibility = View.INVISIBLE
        binding.shimmerLayout.visibility = View.VISIBLE
        binding.shimmerLayout.startShimmer()
        lifecycleScope.launch(Dispatchers.Main) {
            val deferredList = listOf(
                async(Dispatchers.IO) {
                    delay(600)
                },
                async(Dispatchers.Main) {
                    viewModel.setCategoryActive(Constant.CATEGORY_ALL_ID)
                    categoryAdapter.item_selected = 0
                    viewModel.getDataRemote()
                    binding.rcvListCategory.scrollToPosition(0)
                    categoryAdapter.notifyDataSetChanged()

                }
            )
            deferredList.awaitAll()
            binding.swipeLayout.visibility = View.VISIBLE
            binding.shimmerLayout.visibility = View.INVISIBLE
            binding.swipeLayout.isRefreshing = false
            binding.shimmerLayout.stopShimmer()

        }
    }


    private fun getDataFoodByCategory(categoryID: String) {
        binding.rcvListFood.visibility = View.INVISIBLE
        binding.shimmerLayoutListFood.visibility = View.VISIBLE
        binding.shimmerLayoutListFood.startShimmer()
        lifecycleScope.launch(Dispatchers.Main) {
            val deferredList = listOf(
                async(Dispatchers.IO) {
                    delay(600)
                },
                async(Dispatchers.Main) {
                    viewModel.getDataFoodByCategory(categoryID)
                }
            )
            deferredList.awaitAll()
            binding.rcvListFood.visibility = View.VISIBLE
            binding.shimmerLayoutListFood.visibility = View.GONE
            binding.shimmerLayoutListFood.stopShimmer()

        }
    }

    override fun onClickFoodListener(food: Food) {
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment(food))
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
        cartViewModel.addToCart(newCart)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClickCategoryListener(category: Category) {
        if (checkNetwork.isNetworkAvailable()) {
            viewModel.setCategoryActive(category.id)
        } else {
            checkNetwork.showToastNetworkError()
            categoryAdapter.item_selected = 0
            categoryAdapter.notifyDataSetChanged()
        }

    }

    private fun Context.isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return manager.getRunningServices(Integer.MAX_VALUE)
            .any { it.service.className == serviceClass.name }
    }


}

