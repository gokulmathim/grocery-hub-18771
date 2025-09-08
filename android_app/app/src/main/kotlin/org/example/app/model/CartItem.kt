package org.example.app.model

/**
 * PUBLIC_INTERFACE
 * CartItem
 *
 * A cart item consisting of a product and its quantity.
 */
data class CartItem(
    val product: Product,
    val quantity: Int
)
