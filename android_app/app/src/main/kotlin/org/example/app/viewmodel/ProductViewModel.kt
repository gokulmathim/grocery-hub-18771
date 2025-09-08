package org.example.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.example.app.model.Product
import org.example.app.repository.ProductRepository

/**
 * PUBLIC_INTERFACE
 * ProductViewModel
 *
 * Exposes the product list and filter operations.
 */
class ProductViewModel : ViewModel() {
    private val repo = ProductRepository()

    private val _products = MutableLiveData<List<Product>>(emptyList())
    val products: LiveData<List<Product>> = _products

    private var all: List<Product> = emptyList()

    // PUBLIC_INTERFACE
    fun loadProducts() {
        all = repo.getProducts()
        _products.value = all
    }

    // PUBLIC_INTERFACE
    fun filter(query: String) {
        val q = query.trim().lowercase()
        _products.value = if (q.isEmpty()) all else all.filter {
            it.name.lowercase().contains(q) || it.category.lowercase().contains(q)
        }
    }

    // PUBLIC_INTERFACE
    fun getProductById(id: String): Product? = (if (all.isEmpty()) repo.getProducts() else all).find { it.id == id }
}
