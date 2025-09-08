package org.example.app.ui

import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import org.example.app.R
import org.example.app.viewmodel.CartViewModel
import org.example.app.viewmodel.OrderViewModel

/**
 * PUBLIC_INTERFACE
 * CheckoutActivity
 *
 * Allows user to review cart summary, enter address, choose delivery or pickup, and confirm order.
 * Returns: none
 */
class CheckoutActivity : AppCompatActivity() {

    private val cartViewModel: CartViewModel by viewModels()
    private val orderViewModel: OrderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        val items: TextView = findViewById(R.id.summary_items)
        val total: TextView = findViewById(R.id.summary_total)
        val address: EditText = findViewById(R.id.input_address)
        val delivery: RadioButton = findViewById(R.id.radio_delivery)
        val pickup: RadioButton = findViewById(R.id.radio_pickup)
        val place: Button = findViewById(R.id.btn_place_order)

        val summaryText = cartViewModel.cartItems.value.orEmpty().joinToString("\n") {
            "${it.product.name} x${it.quantity}"
        }
        items.text = summaryText
        total.text = getString(R.string.price_format, cartViewModel.total())

        place.setOnClickListener {
            val mode = if (delivery.isChecked) "Delivery" else "Pickup"
            val addr = address.text?.toString()?.trim().orEmpty()
            if (mode == "Delivery" && addr.isEmpty()) {
                Toast.makeText(this, R.string.enter_address, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val success = orderViewModel.placeOrder(
                items = cartViewModel.cartItems.value.orEmpty(),
                address = addr,
                mode = mode,
                total = cartViewModel.total()
            )
            if (success) {
                cartViewModel.clearCart()
                Toast.makeText(this, R.string.order_placed, Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, R.string.order_failed, Toast.LENGTH_LONG).show()
            }
        }
    }
}
