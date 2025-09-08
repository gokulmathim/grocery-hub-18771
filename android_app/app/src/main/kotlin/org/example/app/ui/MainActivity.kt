package org.example.app.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.badge.BadgeDrawable
import org.example.app.R
import org.example.app.viewmodel.CartViewModel

/**
 * PUBLIC_INTERFACE
 * MainActivity
 *
 * Hosts the primary UI with bottom navigation (Home, Search, Cart, Profile).
 * Adds a top app bar action to toggle dark mode, persists the preference,
 * floating cart action button with dynamic item count badge, and gradient background.
 *
 * Returns: No explicit return. Sets the content view, configures theme toggle, and initializes navigation.
 */
class MainActivity : AppCompatActivity() {

    private val cartViewModel: CartViewModel by viewModels()

    private var cartFab: FloatingActionButton? = null
    private var cartBadge: BadgeDrawable? = null // kept for future optional use
    private var cartBadgeView: android.widget.TextView? = null
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        // Apply saved theme mode before view inflation
        AppCompatDelegate.setDefaultNightMode(
            if (PreferencesHelper.isDarkMode(this)) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_GroceryHub_NoSplash)
        setContentView(R.layout.activity_main)

        // Hook toolbar as the support action bar to display menu actions
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.topAppBar)
        setSupportActionBar(toolbar)

        // Setup bottom navigation
        bottomNav = findViewById(R.id.bottom_nav)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.tab_home -> {
                    openFragment(ProductListFragment.newInstance())
                    true
                }
                R.id.tab_search -> {
                    openFragment(SearchFragment.newInstance())
                    true
                }
                R.id.tab_cart -> {
                    openFragment(CartFragment.newInstance())
                    true
                }
                R.id.tab_profile -> {
                    openFragment(ProfileFragment.newInstance())
                    true
                }
                else -> false
            }
        }

        // Floating Cart Action Button
        cartFab = findViewById(R.id.fab_cart)
        cartFab?.setOnClickListener {
            // Instantly navigate to Cart tab
            bottomNav.selectedItemId = R.id.tab_cart
        }
        // Create a simple badge view to overlay on top of the FAB without experimental APIs
        val rootContainer = findViewById<androidx.coordinatorlayout.widget.CoordinatorLayout>(R.id.root)
        cartBadgeView = android.widget.TextView(this).apply {
            background = ContextCompat.getDrawable(this@MainActivity, R.drawable.bg_badge_circle)
            setTextColor(ContextCompat.getColor(this@MainActivity, android.R.color.white))
            textSize = 12f
            setPadding(8, 2, 8, 2)
            visibility = android.view.View.GONE
        }
        val lp = androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams(
            androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams.WRAP_CONTENT,
            androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            // position near FAB's top-end; margins tuned to sit on the corner
            gravity = android.view.Gravity.END or android.view.Gravity.BOTTOM
            marginEnd = 32
            bottomMargin = 96
        }
        rootContainer.addView(cartBadgeView, lp)

        // Observe cart item count to update badge
        cartViewModel.cartItems.observe(this) { list ->
            val count = list.sumOf { it.quantity }
            updateCartFabBadge(count)
            // Show FAB on home/search screens only
            val currentId = bottomNav.selectedItemId
            cartFab?.isVisible = currentId == R.id.tab_home || currentId == R.id.tab_search
        }

        bottomNav.setOnItemReselectedListener { } // no-op

        if (savedInstanceState == null) {
            bottomNav.selectedItemId = R.id.tab_home
        }

        bottomNav.setOnItemSelectedListener { item ->
            val handled = when (item.itemId) {
                R.id.tab_home -> {
                    openFragment(ProductListFragment.newInstance()); true
                }
                R.id.tab_search -> {
                    openFragment(SearchFragment.newInstance()); true
                }
                R.id.tab_cart -> {
                    openFragment(CartFragment.newInstance()); true
                }
                R.id.tab_profile -> {
                    openFragment(ProfileFragment.newInstance()); true
                }
                else -> false
            }
            val showFab = item.itemId == R.id.tab_home || item.itemId == R.id.tab_search
            cartFab?.isVisible = showFab
            cartBadgeView?.isVisible = showFab && (cartViewModel.cartItems.value?.sumOf { it.quantity } ?: 0) > 0
            handled
        }
    }

    private fun updateCartFabBadge(count: Int) {
        val badge = cartBadgeView ?: return
        if (count > 0) {
            badge.text = if (count > 99) "99+" else count.toString()
            badge.visibility = android.view.View.VISIBLE
        } else {
            badge.visibility = android.view.View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        // Update icon and content description based on current mode for accessibility
        val isDark = PreferencesHelper.isDarkMode(this)
        menu?.findItem(R.id.action_toggle_theme)?.let { item ->
            item.icon = getDrawable(if (isDark) R.drawable.ic_light_mode else R.drawable.ic_dark_mode)
            item.title = if (isDark) "Light mode" else "Dark mode"
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_toggle_theme -> {
                val current = PreferencesHelper.isDarkMode(this)
                PreferencesHelper.setDarkMode(this, !current)
                // Recreate to apply theme change across the activity
                recreate()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, fragment::class.java.simpleName)
            .commit()
    }
}

/**
 * PUBLIC_INTERFACE
 * SearchFragment
 * Simple placeholder for a search page to fit bottom navigation "Search" tab.
 */
class SearchFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: android.view.LayoutInflater, container: android.view.ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        val tv = android.widget.TextView(requireContext())
        tv.text = getString(R.string.search)
        tv.setTextColor(resources.getColor(R.color.textPrimary, null))
        tv.textSize = 18f
        tv.gravity = android.view.Gravity.CENTER
        val frame = android.widget.FrameLayout(requireContext())
        frame.addView(tv, android.widget.FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT))
        frame.setBackgroundResource(R.drawable.bg_soft_gradient)
        return frame
    }
    companion object {
        // PUBLIC_INTERFACE
        fun newInstance(): SearchFragment = SearchFragment()
    }
}

/**
 * PUBLIC_INTERFACE
 * ProfileFragment
 * Simple placeholder for a profile page to fit bottom navigation "Profile" tab.
 */
class ProfileFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: android.view.LayoutInflater, container: android.view.ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        val tv = android.widget.TextView(requireContext())
        tv.text = getString(R.string.profile)
        tv.setTextColor(resources.getColor(R.color.textPrimary, null))
        tv.textSize = 18f
        tv.gravity = android.view.Gravity.CENTER
        val frame = android.widget.FrameLayout(requireContext())
        frame.addView(tv, android.widget.FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT))
        frame.setBackgroundResource(R.drawable.bg_soft_gradient)
        return frame
    }
    companion object {
        // PUBLIC_INTERFACE
        fun newInstance(): ProfileFragment = ProfileFragment()
    }
}
