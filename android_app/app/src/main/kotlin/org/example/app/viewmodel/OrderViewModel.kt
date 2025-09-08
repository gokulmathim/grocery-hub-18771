package org.example.app.viewmodel

import androidx.lifecycle.ViewModel
import org.example.app.model.CartItem
import org.example.app.repository.OrderRepository

/**
 * PUBLIC_INTERFACE
 * OrderViewModel
 *
 * Places orders (mock) and stores them locally via repository.
 */
class OrderViewModel : ViewModel() {

    private val repo = OrderRepository()

    // PUBLIC_INTERFACE
    fun placeOrder(items: List<CartItem>, address: String, mode: String, total: Double): Boolean {
        return try {
            repo.placeOrder(items, address, mode, total)
            true
        } catch (_: Exception) {
            false
        }
    }
}
