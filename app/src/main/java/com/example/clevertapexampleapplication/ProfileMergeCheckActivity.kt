package com.example.clevertapexampleapplication

import android.app.NotificationManager
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.clevertap.android.sdk.*
import com.clevertap.android.sdk.displayunits.DisplayUnitListener
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit
import com.clevertap.android.sdk.inbox.CTInboxMessage
import com.clevertap.android.sdk.pushnotification.CTPushNotificationListener
import com.example.clevertapexampleapplication.application.MyApplication
import java.text.SimpleDateFormat
import java.util.*


class ProfileMergeCheckActivity : AppCompatActivity(), DisplayUnitListener,
    InAppNotificationButtonListener,
    CTPushNotificationListener,
    InboxMessageListener//,PushPermissionResponseListener//,CTInboxListener
{
    private lateinit var btnLogin: Button
    private lateinit var btnPushProfile: Button
    private lateinit var btnPushEvent: Button
    private lateinit var btnPushEventProperty: Button
    private lateinit var btnPushCharged: Button
    private lateinit var btnAppInbox: Button
    private lateinit var btnAppInboxRead: Button
    private lateinit var btnOptOut: Button
    private lateinit var btnOffline: Button
    private lateinit var btnNativeDisplay: Button
    private lateinit var btnGetLocation: Button
    private lateinit var btnPromtNotificationPermission: Button
    private lateinit var btnRaiseCustomEvent: Button
    private lateinit var btnPushEventForPushNotification: Button
    private lateinit var btnPushEventForInApp: Button
    private lateinit var btnPushEventForAppInbox: Button
    private lateinit var btnPushEventForPushTemplates: Button
    private lateinit var etIdentity: EditText
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etMobile: EditText

    private lateinit var clevertapDefaultInstance: CleverTapAPI
    private var count: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_merge_check)

        //Added for Multi-Instance SDK
        /*var clevertapAdditionalInstanceConfig: CleverTapInstanceConfig
        val MY_PREFS_NAME = "MyPrefsFile"
        val prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE)

        var counter = prefs.getInt("count", 0)
        if (counter == 0) {
            Log.d("CleverTap", "Counter : $counter")
            clevertapAdditionalInstanceConfig = CleverTapInstanceConfig.createInstance(
                applicationContext,
                "449-WZ4-KK6Z",
                "410-c44"
            )
            counter++
            val editor = prefs.edit()
            editor.putInt("count", counter)
            editor.commit()
        } else {
            Log.d("CleverTap", "Counter : $counter")
            clevertapAdditionalInstanceConfig = CleverTapInstanceConfig.createInstance(
                applicationContext,
                "TEST-677-956-946Z",
                "TEST-65c-aa6"
            )
            counter++
            val editor = prefs.edit()
            editor.putInt("count", counter)
            editor.commit()
        }


        clevertapAdditionalInstanceConfig.setDebugLevel(CleverTapAPI.LogLevel.DEBUG)

        clevertapDefaultInstance =
            CleverTapAPI.instanceWithConfig(applicationContext, clevertapAdditionalInstanceConfig)*/


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            CleverTapAPI.createNotificationChannel(
                baseContext, "promotion", "promotion", "promotion",
                NotificationManager.IMPORTANCE_MAX, true
            )

        }

        clevertapDefaultInstance = (application as MyApplication).getCleverTapInstance()!!
        clevertapDefaultInstance.enableDeviceNetworkInfoReporting(true)
        clevertapDefaultInstance.apply {
            ctPushNotificationListener = this@ProfileMergeCheckActivity
            //registerPushPermissionNotificationResponseListener(this@ProfileMergeCheckActivity)
        }

        btnLogin = findViewById(R.id.btnLogin)
        btnPushProfile = findViewById(R.id.btnPushProfile)
        btnPushEvent = findViewById(R.id.btnPushEvent)
        btnPushEventProperty = findViewById(R.id.btnPushEventProperty)
        btnPushCharged = findViewById(R.id.btnPushCharged)
        btnAppInbox = findViewById(R.id.btnAppInbox)
        btnAppInboxRead = findViewById(R.id.btnAppInboxRead)
        btnOptOut = findViewById(R.id.btnOptOut)
        btnOffline = findViewById(R.id.btnOffline)
        btnNativeDisplay = findViewById(R.id.btnNativeDisplay)
        btnGetLocation = findViewById(R.id.btnGetLocation)
        btnPromtNotificationPermission = findViewById(R.id.btnPromtNotificationPermission)
        btnPushEventForAppInbox = findViewById(R.id.btnPushEventForAppInbox)
        btnRaiseCustomEvent = findViewById(R.id.btnRaiseCustomEvent)
        btnPushEventForPushNotification = findViewById(R.id.btnPushEventForPushNotification)
        btnPushEventForInApp = findViewById(R.id.btnPushEventForInApp)
        btnPushEventForPushTemplates = findViewById(R.id.btnPushEventForPushTemplates)

        etIdentity = findViewById(R.id.etIdentity)
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etMobile = findViewById(R.id.etMobile)

        btnLogin.setOnClickListener {
            val profileCreate = HashMap<String, Any>()
            profileCreate["Identity"] = etIdentity.text.toString()
            profileCreate["Name"] = etName.text.toString()
            profileCreate["Email"] = etEmail.text.toString()
            profileCreate["Phone"] = etMobile.text.toString()
            profileCreate["Photo"] =
                "https://eu1.dashboard.clevertap.com/images/DemoStoreFemales/image-18.png"
            val otherStuff = arrayOf("Jeans", "Perfume")
            profileCreate["MyStuff"] = otherStuff
            clevertapDefaultInstance.onUserLogin(profileCreate)
        }

        btnPushProfile.setOnClickListener {
            val profileCreate = HashMap<String, Any>()
            profileCreate["Identity"] = etIdentity.text.toString()
            profileCreate["Name"] = etName.text.toString()
            profileCreate["Email"] = etEmail.text.toString()
            profileCreate["Phone"] = etMobile.text.toString()
            clevertapDefaultInstance.pushProfile(profileCreate)
        }

        btnPushEvent.setOnClickListener {
            clevertapDefaultInstance.pushEvent("Product viewed")
            //clevertapDefaultInstance.pushEvent("Event with String Property")
        }

        btnPushEventProperty.setOnClickListener {
            val date = Date()
            val sdf = SimpleDateFormat("HH:mm:ss")
            System.out.println(sdf.format(date))
            Log.d("CleverTap", "Time : " + sdf.format(date))
            val date2 = sdf.parse(sdf.format(date))
            val prodViewedAction = mapOf(
                "Product Name" to "Casio Chronograph Watch",
                "Category" to "Mens Accessories",
                "JsonData" to "{500:1, 1000:1, 2000:0}",
                "Price" to 59.99,
                "Date" to date2
            )
            clevertapDefaultInstance.pushEvent("Product Viewed Event", prodViewedAction)
        }

        btnPushEventForPushNotification.setOnClickListener {
            val prodViewedAction = mapOf(
                "Channel" to "Push Notification"
            )
            clevertapDefaultInstance.pushEvent("Engagement Event", prodViewedAction)
        }

        btnPushEventForPushTemplates.setOnClickListener {
            val prodViewedAction = mapOf(
                "Channel" to "Push Templates"
            )
            clevertapDefaultInstance.pushEvent("Engagement Event", prodViewedAction)
        }

        btnPushEventForInApp.setOnClickListener {
            val prodViewedAction = mapOf(
                "Channel" to "In-App"
            )
            clevertapDefaultInstance.pushEvent("Engagement Event", prodViewedAction)
        }

        btnPushEventForAppInbox.setOnClickListener {
            val prodViewedAction = mapOf(
                "Channel" to "App Inbox"
            )
            clevertapDefaultInstance.pushEvent("Engagement Event", prodViewedAction)
        }

        btnPushCharged.setOnClickListener {
            val chargeDetails = HashMap<String, Any>()
            chargeDetails["Amount"] = 300
            chargeDetails["Payment Mode"] = "Credit card"
            chargeDetails["Charged ID"] = 24052013

            val item1 = HashMap<String, Any>()
            item1["Product category"] = "books"
            item1["Book name"] = "The Millionaire next door"
            item1["Quantity"] = 1

            val item2 = HashMap<String, Any>()
            item2["Product category"] = "books"
            item2["Book name"] = "Achieving inner zen"
            item2["Quantity"] = 1

            val item3 = HashMap<String, Any>()
            item3["Product category"] = "books"
            item3["Book name"] = "Chuck it, let's do it"
            item3["Quantity"] = 5

            val items = ArrayList<HashMap<String, Any>>()
            items.add(item1)
            items.add(item2)
            items.add(item3)

            try {
                clevertapDefaultInstance.pushChargedEvent(chargeDetails, items)
            } catch (e: Exception) {
                // You have to specify the first parameter to push()
                // as CleverTapAPI.CHARGED_EVENT
            }
        }


        //Set the Notification Inbox Listener
        //clevertapDefaultInstance.ctNotificationInboxListener = this
        //Initialize the inbox and wait for callbacks on overridden methods
        clevertapDefaultInstance.initializeInbox()

        clevertapDefaultInstance.setCTInboxMessageListener(this)
        clevertapDefaultInstance.setInboxMessageButtonListener(InboxMessageButtonListener {
            Log.d("CleverTap", "onCreate: ")
        })


        btnAppInbox.setOnClickListener {
            val inboxTabs =
                arrayListOf(
                    "Promotions",
                    "Offers",
                    "Others"
                )//Anything after the first 2 will be ignored

            val a = clevertapDefaultInstance.inboxMessageCount
            val b = clevertapDefaultInstance.inboxMessageUnreadCount
            val c = clevertapDefaultInstance.allInboxMessages
            val d = clevertapDefaultInstance.unreadInboxMessages

            Log.i("CleverTap", "getInboxMessageCount :  $a")
            Log.i("CleverTap", "getInboxMessageUnreadCount :  $b")
            Log.i("CleverTap", "getAllInboxMessages :  $c")
            Log.i("CleverTap", "getUnreadInboxMessages :  $d")
            Log.i("CleverTap", "inboxDidInitialize: ")
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
                firstTabTitle = "First Tab"
                clevertapDefaultInstance.showAppInbox(this) //Opens activity With Tabs
            }
            //OR
            //cleverTapDefaultInstance.showAppInbox()//Opens Activity with default style config
        }

        btnAppInboxRead.setOnClickListener {
            clevertapDefaultInstance.allInboxMessages.forEach {
                clevertapDefaultInstance.markReadInboxMessage(it.messageId)
            }
        }

        btnOptOut.setOnClickListener {
            clevertapDefaultInstance.setOptOut(false)
        }
        btnOffline.setOnClickListener {
            if (count <= 0) {
                clevertapDefaultInstance.setOffline(true)
                count++
            } else {
                clevertapDefaultInstance.setOffline(false)
            }

        }

        btnNativeDisplay.setOnClickListener {
            var displayUnits = clevertapDefaultInstance.allDisplayUnits
            Log.d("CleverTap", "onCreate: $displayUnits")
        }

        btnGetLocation.setOnClickListener {
            val location: Location = clevertapDefaultInstance.location
            Toast.makeText(
                applicationContext,
                "Location: ${location.latitude},${location.longitude}",
                Toast.LENGTH_LONG
            ).show()
        }

        btnPromtNotificationPermission.setOnClickListener {
            //clevertapDefaultInstance.promptForPushPermission(true)
        }

        btnRaiseCustomEvent.setOnClickListener {
            val intent = Intent(this@ProfileMergeCheckActivity, CustomEventActivity::class.java)
            startActivity(intent)

        }

        clevertapDefaultInstance.setInAppNotificationButtonListener(this)
        clevertapDefaultInstance.setDisplayUnitListener(this)


    }

    override fun onDisplayUnitsLoaded(units: ArrayList<CleverTapDisplayUnit>?) {
        Log.d("CleverTap", "onDisplayUnitsLoaded: ")
        for (i in 0 until units!!.size) {
            val unit = units[i]
            //prepareDisplayView(unit)
            Log.d("CleverTap", "onDisplayUnitsLoaded: $unit")
        }
    }

    override fun onInAppButtonClick(payload: HashMap<String, String>?) {

        if (payload != null) {
            //Read the values
        }
    }

    override fun onNotificationClickedPayloadReceived(payload: HashMap<String, Any>?) {
        Log.d("Clevertap", payload.toString())
    }

    override fun onInboxItemClicked(message: CTInboxMessage?) {
        Log.d("CleverTap", "onInboxItemClicked: ")
    }

    /*override fun onPushPermissionResponse(accepted: Boolean) {
        Log.i("CleverTap",
            "onPushPermissionResponse :  InApp---> response() called accepted=$accepted"
        );
        if(accepted){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                CleverTapAPI.createNotificationChannel(applicationContext, "promotion", "Promotion",
                    "Promotion", NotificationManager.IMPORTANCE_HIGH, true)
            };
        }
    }*/

    /*override fun inboxDidInitialize() {
        btnAppInbox.setOnClickListener {
            val inboxTabs =
                arrayListOf(
                    "Promotions",
                    "Offers",
                    "Others"
                )//Anything after the first 2 will be ignored
            Log.i("CleverTap", "getInboxMessageCount :  "+clevertapDefaultInstance.inboxMessageCount)
            Log.i("CleverTap", "getInboxMessageUnreadCount :  "+clevertapDefaultInstance.inboxMessageUnreadCount)
            Log.i("CleverTap", "getAllInboxMessages :  "+clevertapDefaultInstance.allInboxMessages)
            Log.i("CleverTap", "getUnreadInboxMessages :  "+clevertapDefaultInstance.unreadInboxMessages)
            Log.i("CleverTap", "inboxDidInitialize: ")
            *//*CTInboxStyleConfig().apply {
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
                firstTabTitle = "First Tab"
                clevertapDefaultInstance.showAppInbox(this) //Opens activity With Tabs
            }*//*
            //OR
            //cleverTapDefaultInstance.showAppInbox()//Opens Activity with default style config
        }
    }*/

    /* override fun inboxMessagesDidUpdate() {
         TODO("Not yet implemented")
     }*/


    var inboxButtonClickListener: InboxMessageButtonListener = InboxMessageButtonListener {
        Log.d("CleverTap", ": ")
    }


}