package com.example.clevertapexampleapplication

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.Toast
import com.clevertap.android.sdk.CTInboxListener
import com.clevertap.android.sdk.CTInboxStyleConfig
import com.clevertap.android.sdk.CTWebInterface
import com.clevertap.android.sdk.CleverTapAPI
import com.clevertap.android.sdk.inbox.CTInboxMessage
import com.example.clevertapexampleapplication.application.MyApplication
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*
import kotlin.collections.HashMap
import org.json.JSONArray
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), CTInboxListener {

    private val sharedPrefFile = "kotlinsharedpreference"
    private lateinit var btnLogin: Button
    private lateinit var btnPushProfile: Button
    private lateinit var btnRaiseEvent: Button
    private lateinit var btnAppInbox: Button
    private lateinit var clevertapDefaultInstance: CleverTapAPI
    private var count = 0

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i("TAG", "onCreate: 6")
        clevertapDefaultInstance = (application as MyApplication).getCleverTapInstance()!!
        Log.i("TAG", "onCreate: 7")

        clevertapDefaultInstance.apply {

            ctNotificationInboxListener = this@MainActivity

            //Initialize the inbox and wait for callbacks on overridden methods
            initializeInbox()
        }


//        clevertapDefaultInstance?.pushProfile(profileUpdate)

        btnLogin = findViewById<Button>(R.id.btnLogin)
        btnPushProfile = findViewById<Button>(R.id.btnPushProfile)
        btnRaiseEvent = findViewById<Button>(R.id.btnRaiseEvent)
        btnAppInbox = findViewById<Button>(R.id.btnAppInbox)

        findViewById<WebView>(R.id.webview)?.apply {
            settings.javaScriptEnabled = true
            loadUrl("file:///android_asset/sampleHTMLCode.html")
            settings.allowContentAccess = false
            settings.allowFileAccess = false
            settings.allowFileAccessFromFileURLs = false
            addJavascriptInterface(CTWebInterface(CleverTapAPI.getDefaultInstance(this@MainActivity)), "CleverTap")
        }

        btnLogin.setOnClickListener {

            //if (count <=1){
                val rand = (0..1000).random()
                val profileCreate = HashMap<String, Any>()
                profileCreate["name"] = "Vishal More "//$rand" // String
                profileCreate["Identity"] = "$rand"
//                profileCreate["Identity"] = "910"

            profileCreate["Email"] = "test$rand@gmail.com" // Email address of the user
//                profileCreate["Email"] = "test1255@gmail.com" // Email address of the user

                //        profileUpdate["Phone"] = "+14155534" // Phone (with the country code, starting with +)

                profileCreate["Gender"] = "" // Can be either M or F

                profileCreate["DOB"] = Date()
                Log.i("TAG", "onCreate: 8")
            clevertapDefaultInstance.onUserLogin(profileCreate);
              //  count++
            clevertapDefaultInstance.allInboxMessages
                Log.i("TAG", "onCreate: 9")
            //}
        }

        btnPushProfile.setOnClickListener {
            val profileCreate = HashMap<String, Any>()
            profileCreate["Identity"] = "910"
            profileCreate["Email"] = "test5798@gmail.com"
            clevertapDefaultInstance.onUserLogin(profileCreate)

        }

        btnRaiseEvent.setOnClickListener {
            val prodViewedAction = HashMap<String, Any>()
            prodViewedAction["Product Name"] = "Casio Chronograph Watch"
            prodViewedAction["Category"] = "Mens Accessories"
            prodViewedAction["Product Url"] = "https://www.amazon.in"
            prodViewedAction["Price"] = 59.99
            prodViewedAction["Date"] = Date()
            clevertapDefaultInstance.pushEvent("Product viewed", prodViewedAction)
            /*val profileUpdate = HashMap<String, Any>()
            profileUpdate["email"] = "test12345@gmail.com"
            profileUpdate["phone"] = "+14155534"
            profileUpdate["name"] = "Vishal"
            clevertapDefaultInstance?.pushProfile(profileUpdate)
            val email = clevertapDefaultInstance?.getProperty("Email")*/

        }
    }

    override fun inboxDidInitialize() {
        btnAppInbox.setOnClickListener {
            val inboxTabs =
                arrayListOf(
                    "Promotions",
                    "Offers",
                    "Others"
                )//Anything after the first 2 will be ignored
            CTInboxStyleConfig().apply {
                tabs = inboxTabs //Do not use this if you don't want to use tabs
                tabBackgroundColor = "#FF0000"
                selectedTabIndicatorColor = "#0000FF"
                selectedTabColor = "#000000"
                unselectedTabColor = "#FFFFFF"
                backButtonColor = "#FF0000"
                navBarTitleColor = "#FF0000"
                navBarTitle = "MY INBOX"
                navBarColor = "#FFFFFF"
                inboxBackgroundColor = "#00FF00"
                //firstTabTitle = "First Tab"
                clevertapDefaultInstance.showAppInbox(this) //Opens activity With Tabs

            }
            //OR
            //clevertapDefaultInstance?.showAppInbox()//Opens Activity with default style config
        }
    }

    override fun inboxMessagesDidUpdate() {
        Log.i("TAG", "inboxMessagesDidUpdate() called")
    }
}