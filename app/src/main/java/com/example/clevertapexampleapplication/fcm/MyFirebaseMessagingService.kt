package com.example.clevertapexampleapplication.fcm

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.clevertap.android.sdk.CleverTapAPI
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.clevertap.pushtemplates.TemplateRenderer

import com.clevertap.android.sdk.pushnotification.NotificationInfo
import com.clevertap.pushtemplates.Utils
import com.clevertap.pushtemplates.Utils.isForPushTemplates


class MyFirebaseMessagingService: FirebaseMessagingService() {
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
                    if (Utils.isForPushTemplates(extras)) {
                        TemplateRenderer.createNotification(context, extras)
                        //TemplateRenderer.createNotification(context, extras, config);
                    } else {
                        CleverTapAPI.createNotification(context, extras)
                    }
                }else{

                }
            }
        } catch (throwable: Throwable) {
            Log.d("TAG", "onMessageReceived: Error parsing FCM payload ${throwable.message}")
        }
    }
    override fun onNewToken(token:String) {
        CleverTapAPI.getDefaultInstance(this)?.pushFcmRegistrationId(token, true)
    }
}