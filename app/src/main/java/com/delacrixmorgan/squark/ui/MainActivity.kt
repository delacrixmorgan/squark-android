package com.delacrixmorgan.squark.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.delacrixmorgan.squark.R
import com.google.firebase.analytics.FirebaseAnalytics

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseAnalytics.getInstance(this)
        setContentView(R.layout.activity_main)
    }
}