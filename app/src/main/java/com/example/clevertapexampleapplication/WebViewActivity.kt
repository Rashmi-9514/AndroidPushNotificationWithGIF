package com.example.clevertapexampleapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import com.clevertap.android.sdk.CTWebInterface
import com.clevertap.android.sdk.CleverTapAPI
import com.example.clevertapexampleapplication.application.MyApplication

class WebViewActivity : AppCompatActivity() {

    private lateinit var wvWebsite: WebView
    private lateinit var clevertapDefaultInstance: CleverTapAPI

    @SuppressLint("JavascriptInterface", "SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        clevertapDefaultInstance = (application as MyApplication).getCleverTapInstance()!!

        wvWebsite = findViewById(R.id.wvWebsite)
        wvWebsite.loadUrl("https://samplewebintegration.000webhostapp.com/")
        //wvWebsite.settings.domStorageEnabled = true
        wvWebsite.addJavascriptInterface(CTWebInterface(clevertapDefaultInstance,),"com_example_clevertapexampleapplication")
        wvWebsite.settings.javaScriptEnabled = true
    }
}