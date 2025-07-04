package com.mohamed.data.utils

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.mohamed.data.model.CityResponse
import java.io.InputStream
import java.io.InputStreamReader

class JsonParser {
    fun parseCitiesStream(inputStream: InputStream): Sequence<CityResponse> = sequence {
        val gson = Gson()
        val reader = JsonReader(InputStreamReader(inputStream))
        reader.beginArray()
        while (reader.hasNext()) {
            val city: CityResponse = gson.fromJson(reader, CityResponse::class.java)
            yield(city)
        }
        reader.endArray()
        reader.close()
    }
}