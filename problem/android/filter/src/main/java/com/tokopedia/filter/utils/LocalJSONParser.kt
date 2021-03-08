package com.tokopedia.filter.utils

import android.content.Context
import com.google.gson.Gson
import java.io.IOException
import java.io.InputStream

class LocalJSONParser {
    companion object {
        fun inputStreamToString(inputStream: InputStream): String {
            try {
                val bytes = ByteArray(inputStream.available())
                inputStream.read(bytes, 0, bytes.size)
                return String(bytes)
            } catch (e: IOException) {
                return ""
            }
        }
    }
}

// jsonFileName = "data.json"
inline fun <reified T> Context.getObjectFromJson(jsonFileName: Int): T {
    val myJson = LocalJSONParser.inputStreamToString(this.resources.openRawResource(jsonFileName))
    return Gson().fromJson(myJson, T::class.java)
}