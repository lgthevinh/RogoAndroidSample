package com.sample.clone.rogo.utils

import com.google.gson.Gson
import com.google.gson.JsonObject

object JsonUtils {
    fun toJsonObject(jsonString: String): JsonObject {
        val gson = Gson()
        return gson.fromJson(jsonString, JsonObject::class.java)
    }
}