package org.example.app.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.example.app.R
import org.example.app.viewmodel.ProductViewModel
import org.example.app.model.Product

/**
 * PUBLIC_INTERFACE
 * ProductListFragment
 *
 * Displays a scrollable list of products. Selecting an item opens ProductDetailActivity.
 * Adds a personalized greeting at the top which is configurable via PreferencesHelper.
 * Provides horizontally scrollable quick filter chips (Organic, Discounts, Popular) without leaving the view.
 * No params. Returns: Fragment instance displaying list.
 */
class ProductListFragment : Fragment() {

    private val viewModel: ProductViewModel by viewModels()
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Greeting
        view.findViewById<TextView>(R.id.text_greeting)?.let { greeting ->
            val name = PreferencesHelper.getDisplayName(requireContext())
            greeting.text = getString(R.string.greeting_template, name)
        }

        recycler = view.findViewById(R.id.recycler_products)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        adapter = ProductAdapter { product ->
            val intent = Intent(requireContext(), ProductDetailActivity::class.java)
            intent.putExtra("product_id", product.id)
            startActivity(intent)
        }
        recycler.adapter = adapter

        val search: EditText = view.findViewById(R.id.input_search)
        search.addTextChangedListener {
            viewModel.filter(it?.toString().orEmpty())
        }

        // Setup quick filter chips
        setupQuickFilters(view)

        viewModel.products.observe(viewLifecycleOwner) { list ->
            adapter.submit(list)
        }
        viewModel.loadProducts()
    }

    private fun setupQuickFilters(root: View) {
        val container = root.findViewById<android.widget.LinearLayout>(R.id.chips_container)
        container.removeAllViews()

        val specs = listOf(
            Triple(getString(R.string.filter_organic), "organic", false),
            Triple(getString(R.string.filter_discounts), "discounts", false),
            Triple(getString(R.string.filter_popular), "popular", false),
        )

        var selectedKey: String? = null

        fun renderChip(title: String, key: String) : View {
            val tv = TextView(requireContext())
            tv.text = title
            tv.setTextColor(resources.getColor(R.color.textPrimary, null))
            tv.background = resources.getDrawable(R.drawable.bg_chip, null)
            tv.setPadding(24)
            val lp = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            lp.rightMargin = 12
            tv.layoutParams = lp
            tv.isSelected = false
            tv.setOnClickListener {
                selectedKey = if (selectedKey == key) null else key
                viewModel.setQuickFilter(selectedKey)
                // update visual selected state
                for (i in 0 until container.childCount) {
                    val child = container.getChildAt(i)
                    child.alpha = 1.0f
                }
                if (selectedKey != null) {
                    // dim others
                    for (i in 0 until container.childCount) {
                        val child = container.getChildAt(i)
                        child.alpha = if ((child as TextView).text.toString().equals(title).not() && key == selectedKey) 0.6f else 1.0f
                    }
                }
            }
            return tv
        }

        specs.forEach { (title, key, _) ->
            container.addView(renderChip(title, key))
        }
    }

    companion object {
        fun newInstance(): ProductListFragment = ProductListFragment()
    }
}

private class ProductAdapter(
    val onClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductViewHolder>() {

    private val items = mutableListOf<Product>()

    // PUBLIC_INTERFACE
    fun submit(list: List<Product>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(v, onClick)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}

private class ProductViewHolder(
    v: View,
    val onClick: (Product) -> Unit
) : RecyclerView.ViewHolder(v) {

    private val title: android.widget.TextView = v.findViewById(R.id.product_title)
    private val price: android.widget.TextView = v.findViewById(R.id.product_price)
    private val image: android.widget.ImageView = v.findViewById(R.id.product_image)
    private var bound: Product? = null

    init {
        v.setOnClickListener {
            bound?.let(onClick)
        }
    }

    fun bind(p: Product) {
        bound = p
        title.text = p.name
        price.text = itemView.context.getString(R.string.price_format, p.price)
        image.setImageResource(p.imageRes)
    }
}
