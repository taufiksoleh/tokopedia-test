package com.tokopedia.filter.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tokopedia.core.fromHtml
import com.tokopedia.core.toCurrency
import com.tokopedia.filter.R
import com.tokopedia.filter.data.models.Product
import com.tokopedia.filter.view.listener.ProductItemListener

class ProductAdapter() : ListAdapter<Product, ProductAdapter.ViewHolder>(ProductDiffCallback) {

    var items: MutableList<Product> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val onItemClickListener: ProductItemListener = object : ProductItemListener {
        override fun onItemSelected(product: Product) {

        }
    }

    class ViewHolder(var view: View, var context: Context) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.name)
        var image: ImageView = view.findViewById(R.id.image)
        var price: TextView = view.findViewById(R.id.price)
        var slashPrice: TextView = view.findViewById(R.id.splashPrice)
        var discount: TextView = view.findViewById(R.id.discount)
        var city: TextView = view.findViewById(R.id.city)

        @SuppressLint("SetTextI18n")
        fun bind(product: Product, productItemListener: ProductItemListener) {
            view.setOnClickListener {
                productItemListener.onItemSelected(product)
            }

            name.text = product.name
            price.text = context.resources.getString(R.string.text_price).format(product.priceInt?.toCurrency())
            slashPrice.apply {
                text = context.resources.getString(R.string.text_slash_price).format(product.slashedPriceInt?.toCurrency())
                slashPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }

            if (product.slashedPriceInt == 0) {
                slashPrice.visibility = View.GONE
            } else {
                slashPrice.visibility = View.VISIBLE
            }

            if (product.discountPercentage == 0) {
                discount.visibility = View.GONE
            } else {
                discount.visibility = View.VISIBLE
            }

            discount.text = product.discountPercentage.toString().plus("%")
            city.text = product.shop?.city

            Glide.with(context)
                    .load(product.imageUrl)
                    .into(image)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ViewHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], onItemClickListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

object ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }
}
