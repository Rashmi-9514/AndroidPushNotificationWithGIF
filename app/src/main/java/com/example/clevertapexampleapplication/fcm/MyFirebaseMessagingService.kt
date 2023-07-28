package com.example.clevertapexampleapplication.fcm

import android.R
import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.clevertap.android.sdk.CleverTapAPI
import com.clevertap.android.sdk.pushnotification.fcm.CTFcmMessageHandler
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {
    var context: Context? = null

    override fun onMessageReceived(message: RemoteMessage) {
        try {
            context = applicationContext
            if (message.data.isNotEmpty()) {
                val extras = Bundle()
                for ((key, value) in message.data.entries) {
                    extras.putString(key, value)
                }
                val info = CleverTapAPI.getNotificationInfo(extras)
                if (info.fromCleverTap) {
                    CTFcmMessageHandler().createNotification(context, message)
                } else {
                    //Handle Notification sent from other platforms.
                }
            }
        } catch (throwable: Throwable) {
            Log.d("TAG", "onMessageReceived: Error parsing FCM payload ${throwable.message}")
        }
    }

    override fun onNewToken(token: String) {
        //CleverTapAPI.getDefaultInstance(this)?.pushFcmRegistrationId(token, true)
        //Added for Multi-Instance SDK
        /*val MY_PREFS_NAME = "MyPrefsFile"
        val prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("token", token)
        editor.commit()*/
        CTFcmMessageHandler().onNewToken(context, token)


    }
}