package com.delacrixmorgan.squark.service.remoteconfig

import com.delacrixmorgan.squark.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

class FirebaseRemoteConfigManager {
    private val remoteConfig = Firebase.remoteConfig
    private val FETCH_TIMEOUT = 15L

    interface FetchRemoteConfigDataListener {
        fun onSuccess()
        fun onFailure(e: Exception?)
    }

    init {
        with(remoteConfig) {
            setConfigSettingsAsync(
                remoteConfigSettings { fetchTimeoutInSeconds = FETCH_TIMEOUT }
            )
            setDefaultsAsync(R.xml.remote_config_defaults)
        }
    }

    fun fetchAndActivate(listener: FetchRemoteConfigDataListener) {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    listener.onSuccess()
                } else {
                    listener.onFailure(task.exception)
                }
            }
    }

    fun getString(key: String) = remoteConfig.getString(key)
}