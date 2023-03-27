package com.example.androidtest

import android.os.Bundle
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.androidtest.databinding.ActivityMainBinding
import com.example.androidtest.navigation.Navigator
import com.example.mycontacts.ui.details.Action
import com.example.mycontacts.ui.details.HasCustomActionToolbar

class MainActivity : AppCompatActivity(), Navigator {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    private lateinit var currentFragment: Fragment

    private val fragmentListener = object : FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            if (f is NavHostFragment) return
            currentFragment = f
            updateToolbar()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        setSupportActionBar(binding.materialToolbar)

        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHost.navController

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, true)
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
    }
    override fun onSupportNavigateUp(): Boolean = navController.navigateUp() || super.onSupportNavigateUp()

    private fun updateToolbar() {
        supportActionBar?.title = navController.currentDestination?.label ?: getString(R.string.app_name)

        if (navController.currentDestination?.id == navController.graph.startDestinationId)
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        else
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragment = currentFragment
        binding.materialToolbar.menu.clear()
        if (fragment is HasCustomActionToolbar)
            createCustomToolbarAction(fragment.getCustomAction())
    }

    private fun createCustomToolbarAction(action: Action) {
        val iconDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(this, action.icon) ?: return)

        val typedValue = TypedValue()
        theme.resolveAttribute(com.google.android.material.R.attr.colorOnPrimary, typedValue, true)
        @ColorInt val color = typedValue.data
        iconDrawable.setTint(color)

        val menuItem = binding.materialToolbar.menu.add(action.title)
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        menuItem.icon = iconDrawable
        menuItem.setOnMenuItemClickListener {
            action.onAction.run()
            true
        }
    }

    // Navigation

    override fun launchGameScreen(startNumber: Int) {
        val direction = MenuFragmentDirections.actionMenuFragmentToGameFragment(startNumber)
        navController.navigate(direction)
    }

    /* Fragment can be launched by destination, action and direction
        launch by destination make function more abstract
        and not dependent on real navigation graph
         */
    override fun launchAboutScreen() {

        navController.navigate(R.id.aboutFragment)
    }

    override fun back() {
       if (navController.popBackStack()) return
        finish()
    }

}