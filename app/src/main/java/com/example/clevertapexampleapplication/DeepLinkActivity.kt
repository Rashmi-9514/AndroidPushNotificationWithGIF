package com.example.clevertapexampleapplication

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class DeepLinkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deep_link)
        Log.d("CleverTap", "onCreate: Deeplink Activity")

        val action: String? = intent?.action
        val data: Uri? = intent?.data

        Log.d("Clevertap","Deeplink Action : $action")
        Log.d("Clevertap","Deeplink data: ${data.toString()}")
    }
}