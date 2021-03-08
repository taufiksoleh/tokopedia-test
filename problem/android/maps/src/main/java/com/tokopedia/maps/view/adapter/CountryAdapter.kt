package com.tokopedia.maps.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.maps.R
import com.tokopedia.maps.data.models.Country

class CountryAdapter() : ListAdapter<Country,CountryAdapter.ViewHolder>(ProductDiffCallback) {

    var items: MutableList<Country> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(var context: Context, view: View) : RecyclerView.ViewHolder(view) {
        var textCountryName: TextView = view.findViewById(R.id.txtCountryName)
        var textCountryCapital: TextView = view.findViewById(R.id.txtCountryCapital)
        var textCountryPopulation: TextView = view.findViewById(R.id.txtCountryPopulation)
        var textCountryCallCode: TextView = view.findViewById(R.id.txtCountryCallCode)

        @SuppressLint("SetTextI18n")
        fun bind(item: Country) {
            val resource = context.resources
            textCountryName.text = item.countryName
            textCountryCapital.text = resource.getString(R.string.label_capital).format(item.capitalCity)

            var callingCode = ""
            item.callingCode?.let { code ->
                code.forEach {
                    callingCode += it
                }
            }
            textCountryCallCode.text = resource.getString(R.string.label_calling_code).format(callingCode)
            textCountryPopulation.text = resource.getString(R.string.label_popupation).format(item.population.toString())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.country_item, parent,false)
        return ViewHolder(parent.context, view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

object ProductDiffCallback : DiffUtil.ItemCallback<Country>() {
    override fun areItemsTheSame(oldItem: Country, newItem: Country): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Country, newItem: Country): Boolean {
        return oldItem.id == newItem.id
    }
}
