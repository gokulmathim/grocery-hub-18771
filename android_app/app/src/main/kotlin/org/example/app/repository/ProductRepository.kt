package org.example.app.repository

import org.example.app.R
import org.example.app.model.Product

/**
 * PUBLIC_INTERFACE
 * ProductRepository
 *
 * Provides product data (mocked locally).
 */
class ProductRepository {

    // PUBLIC_INTERFACE
    fun getProducts(): List<Product> = listOf(
        Product("p1", "Bananas", "Fresh organic bananas", "Fruits", 1.99, R.drawable.ic_banana),
        Product("p2", "Apples", "Crisp red apples", "Fruits", 2.49, R.drawable.ic_apple),
        Product("p3", "Milk 2%", "1 gallon of 2% milk", "Dairy", 3.19, R.drawable.ic_milk),
        Product("p4", "Whole Wheat Bread", "Soft whole wheat loaf", "Bakery", 2.99, R.drawable.ic_bread),
        Product("p5", "Eggs (Dozen)", "Grade A large eggs", "Dairy", 2.89, R.drawable.ic_eggs),
        Product("p6", "Spinach", "Fresh baby spinach 16oz", "Produce", 3.49, R.drawable.ic_spinach),
        Product("p7", "Chicken Breast", "Boneless skinless chicken", "Meat", 6.99, R.drawable.ic_chicken)
    )
}
