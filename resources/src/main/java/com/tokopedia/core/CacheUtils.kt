package com.tokopedia.core

import android.content.Context
import android.content.SharedPreferences

class CacheUtils(context: Context) {
    private var PRIVATE_MODE = 0
    private val PREF_NAME = context.packageName
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)

    fun setBool(key: String, value: Boolean) {
        val editor = sharedPref.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBool(key: String): Boolean {
        return sharedPref.getBoolean(key, false)
    }

    fun setString(key: String, value: String) {
        val editor = sharedPref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String): String? {
        return sharedPref.getString(key, "")
    }

    fun setFloat(key: String, value: Float) {
        val editor = sharedPref.edit()
        editor.putFloat(key, value)
        editor.apply()
    }

    fun getFloat(key: String): Float {
        return sharedPref.getFloat(key, 0f)
    }
}