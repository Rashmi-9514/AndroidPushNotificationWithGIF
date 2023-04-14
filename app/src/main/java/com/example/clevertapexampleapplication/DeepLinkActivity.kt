package com.example.clevertapexampleapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class DeepLinkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deep_link)
        Log.d("CleverTap", "onCreate: Deeplink Activity")
    }
}