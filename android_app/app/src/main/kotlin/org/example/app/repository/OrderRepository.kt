package org.example.app.repository

import org.example.app.model.CartItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * PUBLIC_INTERFACE
 * OrderRepository
 *
 * Places orders by appending to an in-memory list.
 */
class OrderRepository {

    private val orders = mutableListOf<String>()

    // PUBLIC_INTERFACE
    fun placeOrder(items: List<CartItem>, address: String, mode: String, total: Double) {
        val ts = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date())
        val summary = items.joinToString { "${it.product.name} x${it.quantity}" }
        orders.add("[$ts] $mode | $summary | $$total | $address")
    }

    // PUBLIC_INTERFACE
    fun history(): List<String> = orders.toList()
}
