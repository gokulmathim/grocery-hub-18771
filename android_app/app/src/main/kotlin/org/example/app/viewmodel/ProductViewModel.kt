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

    private var searchQuery: String = ""
    private var activeFilter: String? = null // "organic", "discounts", "popular"

    // PUBLIC_INTERFACE
    fun loadProducts() {
        all = repo.getProducts()
        emitFiltered()
    }

    // PUBLIC_INTERFACE
    fun filter(query: String) {
        searchQuery = query.trim()
        emitFiltered()
    }

    // PUBLIC_INTERFACE
    fun setQuickFilter(key: String?) {
        activeFilter = key
        emitFiltered()
    }

    private fun emitFiltered() {
        var list = all

        // Apply quick filter
        when (activeFilter) {
            "organic" -> {
                list = list.filter { it.description.contains("organic", ignoreCase = true) || it.name.contains("organic", ignoreCase = true) }
            }
            "discounts" -> {
                // simulate: items under $3 as "discounts"
                list = list.filter { it.price < 3.0 }
            }
            "popular" -> {
                // simulate: specific known popular ids
                list = list.filter { it.id in setOf("p1", "p2", "p4") }
            }
        }

        // Apply text search
        val q = searchQuery.lowercase()
        list = if (q.isEmpty()) list else list.filter {
            it.name.lowercase().contains(q) || it.category.lowercase().contains(q) || it.description.lowercase().contains(q)
        }

        _products.value = list
    }

    // PUBLIC_INTERFACE
    fun getProductById(id: String): Product? = (if (all.isEmpty()) repo.getProducts() else all).find { it.id == id }
}
