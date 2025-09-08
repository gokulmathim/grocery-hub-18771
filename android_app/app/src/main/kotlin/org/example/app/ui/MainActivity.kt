package org.example.app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import org.example.app.R

/**
 * PUBLIC_INTERFACE
 * MainActivity
 *
 * This activity hosts the main UI with bottom navigation between Product List and Cart.
 * It manages fragment transactions for:
 * - ProductListFragment
 * - CartFragment
 *
 * Returns: No explicit return. Sets the content view and initializes navigation.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_GroceryHub_NoSplash)
        setContentView(R.layout.activity_main)

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

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, fragment::class.java.simpleName)
            .commit()
    }
}
