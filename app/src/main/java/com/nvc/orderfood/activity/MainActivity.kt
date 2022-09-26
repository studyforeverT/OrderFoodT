package com.nvc.orderfood.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.nvc.orderfood.R
import com.nvc.orderfood.databinding.ActivityMainBinding
import com.nvc.orderfood.utils.CheckNetwork
import com.nvc.orderfood.viewmodel.CartViewModel
import com.nvc.orderfood.viewmodel.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfig: AppBarConfiguration

    @Inject
    lateinit var checkNetwork: CheckNetwork
    private val cartViewModel: CartViewModel by viewModels()
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHost.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bnvMain.visibility = when (destination.id) {
                R.id.homeFragment,
                R.id.favoriteFragment,
                R.id.cartFragment,
                R.id.notificationFragment,
                R.id.profileFragment -> View.VISIBLE
                else -> View.GONE
            }
        }
        appBarConfig = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.cartFragment,
                R.id.favoriteFragment,
                R.id.profileFragment,
                R.id.notificationFragment
            )
        )
        binding.bnvMain.setupWithNavController(navController)
        cartViewModel.getDataRemote()
        favoriteViewModel.favoriteList.observe(this) {}
        cartViewModel.listCart.observe(this) {
            binding.bnvMain.getOrCreateBadge(R.id.cartFragment).apply {
                isVisible = it.isNotEmpty()
                number = it.size
            }
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfig) || super.onNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(navController.currentDestination?.id==R.id.paymentFragment){
            navController.navigate(R.id.cartFragment)
        }
    }
}