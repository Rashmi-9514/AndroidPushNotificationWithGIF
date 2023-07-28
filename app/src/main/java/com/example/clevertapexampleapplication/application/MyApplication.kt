package com.example.clevertapexampleapplication.application

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import com.clevertap.android.pushtemplates.PushTemplateNotificationHandler
import com.clevertap.android.sdk.ActivityLifecycleCallback
import com.clevertap.android.sdk.CleverTapAPI
import com.clevertap.android.sdk.interfaces.NotificationHandler

class MyApplication :Application() {


    companion object{
        @SuppressLint("StaticFieldLeak")
        @JvmStatic
        var cleverTapDefaultInstance: CleverTapAPI? = null
    }

    override fun onCreate() {
        CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.VERBOSE)
        ActivityLifecycleCallback.register(this);
        super.onCreate()
        cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(applicationContext)
        cleverTapDefaultInstance?.enablePersonalization()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            CleverTapAPI.createNotificationChannel(baseContext,"promotion","promotion","promotion",
                NotificationManager.IMPORTANCE_MAX,true)
        }
        CleverTapAPI.setNotificationHandler(PushTemplateNotificationHandler() as NotificationHandler);

    }

    fun getCleverTapInstance(): CleverTapAPI? {
        return cleverTapDefaultInstance
    }
}