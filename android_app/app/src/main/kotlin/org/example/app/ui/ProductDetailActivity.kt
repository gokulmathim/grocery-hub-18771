package org.example.app.ui

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import org.example.app.R
import org.example.app.viewmodel.CartViewModel
import org.example.app.viewmodel.ProductViewModel
import org.example.app.model.Product

/**
 * PUBLIC_INTERFACE
 * ProductDetailActivity
 *
 * Displays details of a product passed via intent extras and allows adding it to the cart.
 * Intent extras:
 *  - "product_id": String - ID of the product to display
 * Returns: none
 */
class ProductDetailActivity : AppCompatActivity() {

    private val productViewModel: ProductViewModel by viewModels()
    private val cartViewModel: CartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        val productId = intent.getStringExtra("product_id")
        val image: ImageView = findViewById(R.id.product_image)
        val title: TextView = findViewById(R.id.product_title)
        val price: TextView = findViewById(R.id.product_price)
        val desc: TextView = findViewById(R.id.product_description)
        val add: Button = findViewById(R.id.btn_add_to_cart)

        var product: Product? = null
        productId?.let {
            product = productViewModel.getProductById(it)
            product?.let { p ->
                image.setImageResource(p.imageRes)
                title.text = p.name
                price.text = getString(R.string.price_format, p.price)
                desc.text = p.description
            }
        }

        add.setOnClickListener {
            product?.let {
                cartViewModel.addToCart(it)
                finish()
            }
        }
    }
}
