package org.example.app.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import org.example.app.R

/**
 * PUBLIC_INTERFACE
 * MainActivity
 *
 * Hosts the primary UI with bottom navigation between Product List and Cart.
 * Adds a top app bar action to toggle dark mode, persists the preference,
 * and applies a soft gradient background to the main content.
 *
 * Returns: No explicit return. Sets the content view, configures theme toggle, and initializes navigation.
 */
class MainActivity : AppCompatActivity() {

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
        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_nav)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.tab_products -> {
                    openFragment(ProductListFragment.newInstance())
                    true
                }
                R.id.tab_cart -> {
                    openFragment(CartFragment.newInstance())
                    true
                }
                else -> false
            }
        }

        if (savedInstanceState == null) {
            bottomNav.selectedItemId = R.id.tab_products
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
