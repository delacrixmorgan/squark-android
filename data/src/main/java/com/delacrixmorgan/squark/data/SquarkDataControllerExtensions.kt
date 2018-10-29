package com.delacrixmorgan.squark.data

import android.content.Context
import org.json.JSONObject
import java.io.BufferedReader

fun Context.getJsonMap(rawFile: Int, key: String): Map<String, String> {
    val inputStream = resources.openRawResource(rawFile)
    val responseObject = inputStream.bufferedReader().use(BufferedReader::readText)

    val map = HashMap<String, String>()
    val jsonObject = JSONObject(responseObject).optJSONObject(key)

    jsonObject.keys().forEach {
        map[it] = "${jsonObject[it]}"
    }

    return map
}