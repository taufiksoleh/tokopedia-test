package com.tokopedia.filter.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.tokopedia.filter.R
import com.tokopedia.filter.data.models.City

class CityAdapter(private var itemClickListener:(City) -> Unit) : ListAdapter<City,CityAdapter.ViewHolder>(CityDiffCallback) {

    var items: MutableList<City> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var mCityChip: Chip = view.findViewById(R.id.city)

        fun bind(city: City, itemClickListener: (City) -> Unit) {
            mCityChip.text = city.name
            mCityChip.setOnClickListener {
                itemClickListener(city)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.city_item, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], itemClickListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

object CityDiffCallback : DiffUtil.ItemCallback<City>() {
    override fun areItemsTheSame(oldItem: City, newItem: City): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: City, newItem: City): Boolean {
        return oldItem.name == newItem.name
    }
}
