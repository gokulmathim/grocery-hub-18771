package org.example.app.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.example.app.model.CartItem
import org.example.app.model.Product
import org.example.app.repository.CartRepository

/**
 * PUBLIC_INTERFACE
 * CartViewModel
 *
 * Manages cart state with persistence using SharedPreferences through CartRepository.
 */
class CartViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = CartRepository(application.getSharedPreferences("cart_prefs", Context.MODE_PRIVATE))

    private val _cart = MutableLiveData<List<CartItem>>(repo.loadCart())
    val cartItems: LiveData<List<CartItem>> = _cart

    // PUBLIC_INTERFACE
    fun addToCart(product: Product) {
        val updated = repo.add(product)
        _cart.value = updated
    }

    // PUBLIC_INTERFACE
    fun increaseQty(productId: String) {
        val updated = repo.increase(productId)
        _cart.value = updated
    }

    // PUBLIC_INTERFACE
    fun decreaseQty(productId: String) {
        val updated = repo.decrease(productId)
        _cart.value = updated
    }

    // PUBLIC_INTERFACE
    fun removeFromCart(productId: String) {
        val updated = repo.remove(productId)
        _cart.value = updated
    }

    // PUBLIC_INTERFACE
    fun total(): Double = repo.total()

    // PUBLIC_INTERFACE
    fun clearCart() {
        repo.clear()
        _cart.value = repo.loadCart()
    }
}
