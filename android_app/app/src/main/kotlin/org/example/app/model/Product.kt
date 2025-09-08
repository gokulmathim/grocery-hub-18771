package org.example.app.model

import androidx.annotation.DrawableRes
import org.example.app.R

/**
 * PUBLIC_INTERFACE
 * Product
 *
 * Represents a product that can be browsed and added to cart.
 */
data class Product(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val price: Double,
    @DrawableRes val imageRes: Int = R.drawable.ic_placeholder
)
