package com.tokopedia.filter.data.local

import android.annotation.SuppressLint
import android.content.Context
import com.tokopedia.filter.R
import com.tokopedia.filter.data.models.*
import com.tokopedia.filter.utils.getObjectFromJson

class ProductRepository(private var context: Context) {
    @SuppressLint("CheckResult")
    fun getProduct(): ProductResource? {
        return parseProduct()
    }

    fun getFilteredProduct(city: String, priceMin: Int, priceMax: Int): ProductResource {
        val res: ProductResource? = parseProduct()
        val products: MutableList<Product> = res?.data?.products as MutableList<Product>
        val filteredProducts: List<Product> = when {
            city.isEmpty() -> {
                products.filter {
                    it.priceInt in priceMin..priceMax
                }
            }
            city == "Other" -> {
                val mostCity = getMostCity(3)
                val filter: MutableList<String> = mutableListOf()
                mostCity.forEach {
                    if (city != it.name) {
                        filter.add(it.name)
                    }
                }

                products.filter {
                    it.shop?.city !in filter
                            && it.priceInt in priceMin..priceMax
                }
            }
            else -> {
                products.filter {
                    it.shop?.city.equals(city, ignoreCase = true)
                            && it.priceInt in priceMin..priceMax
                }
            }
        }
        return ProductResource(data = ProductList(filteredProducts))
    }

    fun getFilteredProductWithName(city: String, priceMin: Int, priceMax: Int, name: String): ProductResource {
        val res: ProductResource? = parseProduct()
        val products: MutableList<Product> = res?.data?.products as MutableList<Product>
        val filteredProducts: List<Product> = when {
            city.isEmpty() -> {
                products.filter {
                    it.name?.contains(name, ignoreCase = true) == true
                            && it.priceInt in priceMin..priceMax
                }
            }
            else -> {
                products.filter {
                    it.name?.contains(name,ignoreCase = true) == true
                            && it.shop?.city.equals(city, ignoreCase = true)
                            && it.priceInt in priceMin..priceMax
                }
            }
        }
        return ProductResource(data = ProductList(filteredProducts))
    }

    fun getMostCity(limit: Int): List<City> {
        val res: ProductResource? = parseProduct()
        val products: List<Product>? = res?.data?.products
        val city: MutableList<City> = mutableListOf()

        products?.forEach { item ->
            item.shop?.city?.let { cityName ->
                val findCity = city.find { it.name.equals(cityName, ignoreCase = true) }
                if (findCity != null) {
                    findCity.frequent += 1
                } else {
                    city.add(City(cityName, 1))
                }
            }
        }

        // sort city most frequent
        city.sortByDescending { it.frequent }

        // limit city list
        val result = city.take(limit - 1).toMutableList()
        result.add(City("Other", 0))
        return result
    }

    fun getPrice(): Price {
        val res: ProductResource? = parseProduct()
        val products: List<Product>? = res?.data?.products
        val min = products?.minBy { it.priceInt!!.toFloat() }
        val max = products?.maxBy { it.priceInt!!.toFloat() }
        return Price(priceMin = min?.priceInt!!.toFloat(), priceMax = max?.priceInt!!.toFloat())
    }

    private fun parseProduct(): ProductResource? {
        return context.getObjectFromJson(R.raw.products)
    }
}