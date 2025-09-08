package org.example.app.repository

import android.content.SharedPreferences
import org.example.app.model.CartItem
import org.example.app.model.Product
import org.json.JSONArray
import org.json.JSONObject

/**
 * PUBLIC_INTERFACE
 * CartRepository
 *
 * Persists cart data using SharedPreferences and JSON encoding.
 */
class CartRepository(private val prefs: SharedPreferences) {

    private var cart: MutableList<CartItem> = mutableListOf()

    init {
        cart = loadCart().toMutableList()
    }

    // PUBLIC_INTERFACE
    fun loadCart(): List<CartItem> {
        val raw = prefs.getString(KEY, "[]") ?: "[]"
        val arr = JSONArray(raw)
        val list = mutableListOf<CartItem>()
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            val p = JSONObject(o.getString("product"))
            val product = Product(
                id = p.getString("id"),
                name = p.getString("name"),
                description = p.getString("description"),
                category = p.getString("category"),
                price = p.getDouble("price"),
                imageRes = p.optInt("imageRes", 0)
            )
            list.add(CartItem(product, o.getInt("quantity")))
        }
        return list
    }

    private fun persist() {
        val arr = JSONArray()
        cart.forEach { item ->
            val p = JSONObject()
            p.put("id", item.product.id)
            p.put("name", item.product.name)
            p.put("description", item.product.description)
            p.put("category", item.product.category)
            p.put("price", item.product.price)
            p.put("imageRes", item.product.imageRes)

            val o = JSONObject()
            o.put("product", p.toString())
            o.put("quantity", item.quantity)
            arr.put(o)
        }
        prefs.edit().putString(KEY, arr.toString()).apply()
    }

    // PUBLIC_INTERFACE
    fun add(product: Product): List<CartItem> {
        val idx = cart.indexOfFirst { it.product.id == product.id }
        if (idx >= 0) {
            val current = cart[idx]
            cart[idx] = current.copy(quantity = current.quantity + 1)
        } else {
            cart.add(CartItem(product, 1))
        }
        persist()
        return cart.toList()
    }

    // PUBLIC_INTERFACE
    fun increase(productId: String): List<CartItem> {
        val idx = cart.indexOfFirst { it.product.id == productId }
        if (idx >= 0) {
            val c = cart[idx]
            cart[idx] = c.copy(quantity = c.quantity + 1)
            persist()
        }
        return cart.toList()
    }

    // PUBLIC_INTERFACE
    fun decrease(productId: String): List<CartItem> {
        val idx = cart.indexOfFirst { it.product.id == productId }
        if (idx >= 0) {
            val c = cart[idx]
            val q = (c.quantity - 1).coerceAtLeast(0)
            if (q == 0) {
                cart.removeAt(idx)
            } else {
                cart[idx] = c.copy(quantity = q)
            }
            persist()
        }
        return cart.toList()
    }

    // PUBLIC_INTERFACE
    fun remove(productId: String): List<CartItem> {
        val idx = cart.indexOfFirst { it.product.id == productId }
        if (idx >= 0) {
            cart.removeAt(idx)
            persist()
        }
        return cart.toList()
    }

    // PUBLIC_INTERFACE
    fun total(): Double = cart.sumOf { it.product.price * it.quantity }

    // PUBLIC_INTERFACE
    fun clear() {
        cart.clear()
        persist()
    }

    companion object {
        private const val KEY = "cart"
    }
}
