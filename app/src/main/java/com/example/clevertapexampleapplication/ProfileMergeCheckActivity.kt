package com.example.clevertapexampleapplication

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.clevertap.android.sdk.CTInboxStyleConfig
import com.clevertap.android.sdk.CleverTapAPI
import com.clevertap.android.sdk.InAppNotificationButtonListener
import com.clevertap.android.sdk.displayunits.DisplayUnitListener
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit
import com.clevertap.android.sdk.pushnotification.CTPushNotificationListener
import com.example.clevertapexampleapplication.application.MyApplication
import java.util.*


class ProfileMergeCheckActivity : AppCompatActivity(), DisplayUnitListener,InAppNotificationButtonListener,
    CTPushNotificationListener//,CTInboxListener
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
    private lateinit var etIdentity: EditText
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etMobile: EditText

    private lateinit var clevertapDefaultInstance: CleverTapAPI
    private var count:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_merge_check)
        clevertapDefaultInstance = (application as MyApplication).getCleverTapInstance()!!
        clevertapDefaultInstance.apply {
            ctPushNotificationListener = this@ProfileMergeCheckActivity
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
        }

        btnPushEventProperty.setOnClickListener {
            val prodViewedAction = mapOf(
                "Product Name" to "Casio Chronograph Watch",
                "Category" to "Mens Accessories",
                "Price" to 59.99,
                "Date" to Date())
            clevertapDefaultInstance.pushEvent("Product Viewed Event", prodViewedAction)
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
            if (count<=0) {
                clevertapDefaultInstance.setOffline(true)
                count++
            }else{
                clevertapDefaultInstance.setOffline(false)
            }

        }

        btnNativeDisplay.setOnClickListener {
            var displayUnits = clevertapDefaultInstance.allDisplayUnits
            Log.d("CleverTap", "onCreate: ")
        }

        btnGetLocation.setOnClickListener {
            val location:Location = clevertapDefaultInstance.location
            Toast.makeText(applicationContext,"Location: ${location.latitude},${location.longitude}",Toast.LENGTH_LONG).show()
        }

        clevertapDefaultInstance.setInAppNotificationButtonListener(this)
        clevertapDefaultInstance.setDisplayUnitListener(this)

    }

    override fun onDisplayUnitsLoaded(units: ArrayList<CleverTapDisplayUnit>?) {
        Log.d("CleverTap", "onDisplayUnitsLoaded: ")
        for (i in 0 until units!!.size)
        {
            val unit = units[i]
            //prepareDisplayView(unit)
            Log.d("CleverTap", "onDisplayUnitsLoaded: $unit")
        }
    }

    override fun onInAppButtonClick(payload: HashMap<String, String>?) {

        if(payload != null){
            //Read the values
        }
    }

    override fun onNotificationClickedPayloadReceived(payload: HashMap<String, Any>?) {
        Log.d("Clevertap", payload.toString())
    }

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


}