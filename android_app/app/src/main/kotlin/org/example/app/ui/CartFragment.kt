package org.example.app.ui

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.example.app.R
import org.example.app.model.CartItem
import org.example.app.viewmodel.CartViewModel

/**
 * PUBLIC_INTERFACE
 * CartFragment
 *
 * Displays cart items and allows quantity updates/removal, and navigating to checkout.
 */
class CartFragment : Fragment() {

    private val cartViewModel: CartViewModel by viewModels()
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: CartAdapter
    private lateinit var totalView: TextView
    private lateinit var checkoutBtn: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recycler = view.findViewById(R.id.recycler_cart)
        totalView = view.findViewById(R.id.cart_total)
        checkoutBtn = view.findViewById(R.id.btn_checkout)
        recycler.layoutManager = LinearLayoutManager(requireContext())

        adapter = CartAdapter(
            onInc = { cartViewModel.increaseQty(it.product.id) },
            onDec = { cartViewModel.decreaseQty(it.product.id) },
            onRemove = { cartViewModel.removeFromCart(it.product.id) }
        )
        recycler.adapter = adapter

        cartViewModel.cartItems.observe(viewLifecycleOwner) { list ->
            adapter.submit(list)
            totalView.text = getString(R.string.price_format, cartViewModel.total())
            checkoutBtn.isEnabled = list.isNotEmpty()
        }

        checkoutBtn.setOnClickListener {
            startActivity(Intent(requireContext(), CheckoutActivity::class.java))
        }
    }

    companion object {
        fun newInstance(): CartFragment = CartFragment()
    }
}

private class CartAdapter(
    val onInc: (CartItem) -> Unit,
    val onDec: (CartItem) -> Unit,
    val onRemove: (CartItem) -> Unit
) : RecyclerView.Adapter<CartViewHolder>() {

    private val items = mutableListOf<CartItem>()

    // PUBLIC_INTERFACE
    fun submit(list: List<CartItem>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(v, onInc, onDec, onRemove)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}

private class CartViewHolder(
    v: View,
    val onInc: (CartItem) -> Unit,
    val onDec: (CartItem) -> Unit,
    val onRemove: (CartItem) -> Unit
) : RecyclerView.ViewHolder(v) {

    private val title: TextView = v.findViewById(R.id.cart_item_title)
    private val qty: TextView = v.findViewById(R.id.cart_item_qty)
    private val price: TextView = v.findViewById(R.id.cart_item_price)
    private val inc: ImageButton = v.findViewById(R.id.btn_inc)
    private val dec: ImageButton = v.findViewById(R.id.btn_dec)
    private val remove: ImageButton = v.findViewById(R.id.btn_remove)
    private val image: ImageView = v.findViewById(R.id.cart_item_image)
    private var bound: CartItem? = null

    init {
        inc.setOnClickListener { bound?.let(onInc) }
        dec.setOnClickListener { bound?.let(onDec) }
        remove.setOnClickListener { bound?.let(onRemove) }
    }

    fun bind(ci: CartItem) {
        bound = ci
        title.text = ci.product.name
        qty.text = "x${ci.quantity}"
        price.text = itemView.context.getString(R.string.price_format, ci.product.price * ci.quantity)
        image.setImageResource(ci.product.imageRes)
    }
}
