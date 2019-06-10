package com.delacrixmorgan.squark.data

import android.content.Context
import org.json.JSONObject
import java.io.BufferedReader

/**
 * SquarkDataControllerExtensions
 * squark-android
 *
 * Created by Delacrix Morgan on 10/06/2019.
 * Copyright (c) 2019 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

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