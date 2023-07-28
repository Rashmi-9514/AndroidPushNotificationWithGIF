package com.example.clevertapexampleapplication

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.clevertap.android.geofence.CTGeofenceAPI
import com.clevertap.android.geofence.CTGeofenceSettings
import com.clevertap.android.geofence.interfaces.CTGeofenceEventsListener
import com.clevertap.android.sdk.*
import com.clevertap.android.sdk.displayunits.DisplayUnitListener
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit
import com.clevertap.android.sdk.inbox.CTInboxMessage
import com.clevertap.android.sdk.pushnotification.CTPushNotificationListener
import com.example.clevertapexampleapplication.application.MyApplication
import com.example.clevertapexampleapplication.location.LocationTracker
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class ProfileMergeCheckActivity : AppCompatActivity(), DisplayUnitListener,
    InAppNotificationButtonListener,
    CTPushNotificationListener,InboxMessageListener
    //,PushPermissionResponseListener//,CTInboxListener
{

    //Buttons
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
    private lateinit var btnPushEventForCustomInApp: Button
    private lateinit var btnPushEventForAppInbox: Button
    private lateinit var btnPushEventForPushTemplates: Button
    private lateinit var btnPushEventForPushNotificationAppInbox: Button
    private lateinit var btnStopLocation: Button
    private lateinit var btnWebView: Button
    private lateinit var btnInitGeofence: Button
    private lateinit var btnTriggerLocation: Button
    private lateinit var btnDeactivateGeofence: Button
    private lateinit var btnPushEventForUserRating: Button
    private lateinit var btnPushEventForNPSRating: Button
    private lateinit var btnPushEventForLeadGenerationText: Button
    private lateinit var btnPushEventForLeadGenerationImage: Button


    //EditText
    private lateinit var etIdentity: EditText
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etMobile: EditText
    private lateinit var etUserProperty: EditText

    private lateinit var clevertapDefaultInstance: CleverTapAPI
    private var count: Int = 0
    private var setOffline: Boolean = false

    private lateinit var locationTracker: LocationTracker
    private val permissionId = 2
    private var count2:Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_merge_check)

        locationTracker = LocationTracker(this)

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
        btnPushEventForPushNotificationAppInbox = findViewById(R.id.btnPushEventForPushNotificationAppInbox)
        btnPushEventForInApp = findViewById(R.id.btnPushEventForInApp)
        btnPushEventForCustomInApp = findViewById(R.id.btnPushEventForCustomInApp)
        btnPushEventForPushTemplates = findViewById(R.id.btnPushEventForPushTemplates)
        btnWebView = findViewById(R.id.btnWebView)
        btnStopLocation = findViewById(R.id.btnStopLocation)
        btnInitGeofence = findViewById(R.id.btnInitGeofence)
        btnTriggerLocation = findViewById(R.id.btnTriggerLocation)
        btnDeactivateGeofence = findViewById(R.id.btnDeactivateGeofence)
        btnPushEventForUserRating = findViewById(R.id.btnPushEventForUserRating)
        btnPushEventForNPSRating = findViewById(R.id.btnPushEventForNPSRating)
        btnPushEventForLeadGenerationText = findViewById(R.id.btnPushEventForLeadGenerationText)
        btnPushEventForLeadGenerationImage = findViewById(R.id.btnPushEventForLeadGenerationImage)

        etIdentity = findViewById(R.id.etIdentity)
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etMobile = findViewById(R.id.etMobile)
        etUserProperty = findViewById(R.id.etUserProperty)

        btnLogin.setOnClickListener {
            val profileCreate = HashMap<String, Any>()
            profileCreate["Identity"] = etIdentity.text.toString()
            profileCreate["Name"] = etName.text.toString()
            profileCreate["Email"] = etEmail.text.toString()
            profileCreate["Phone"] = etMobile.text.toString()
            profileCreate["Photo"] =
                "https://eu1.dashboard.clevertap.com/images/DemoStoreFemales/image-18.png"
            val otherStuff = arrayOf("Jeans", "Perfume")
            //profileCreate["MyStuff"] = otherStuff
            clevertapDefaultInstance.onUserLogin(profileCreate)
        }

        btnPushProfile.setOnClickListener {
            val profileCreate = HashMap<String, Any>()
            if (etIdentity.text.toString().isNotEmpty()){
                profileCreate["Identity"] = etIdentity.text.toString()
            }

            if (etName.text.toString().isNotEmpty()){
                profileCreate["Name"] = etName.text.toString()
            }

            if (etEmail.text.toString().isNotEmpty()){
                profileCreate["Email"] = etEmail.text.toString()
            }

            if (etMobile.text.toString().isNotEmpty()){
                profileCreate["Phone"] = etMobile.text.toString()
            }

            if (etUserProperty.text.toString().isNotEmpty()){
                profileCreate["User Type"] = etUserProperty.text.toString()
            }

            val otherStuff = arrayOf("Bags", "Shoes")
            profileCreate["MyStuff"] = otherStuff
            clevertapDefaultInstance.pushProfile(profileCreate)
        }

        btnPushEvent.setOnClickListener {
            clevertapDefaultInstance.pushEvent("Product Viewed")
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

        btnPushEventForPushNotificationAppInbox.setOnClickListener {
            val prodViewedAction = mapOf(
                "Channel" to "Push + App Inbox"

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
        btnPushEventForCustomInApp.setOnClickListener {
            val prodViewedAction = mapOf(
                "Channel" to "Custom In-App"
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
            chargeDetails["Amount"] =   300
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

        clevertapDefaultInstance.setCTInboxMessageListener(this);
        clevertapDefaultInstance.setInboxMessageButtonListener(InboxMessageButtonListener {
            Log.d("CleverTap", "Inbox Message Button Listener: ")
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
                firstTabTitle = "All"
                clevertapDefaultInstance.showAppInbox(this) //Opens activity With Tabs
            }
            //OR
            //cleverTapDefaultInstance.showAppInbox()//Opens Activity with default style config
        }

        btnAppInboxRead.setOnClickListener {
            clevertapDefaultInstance.allInboxMessages
            clevertapDefaultInstance.allInboxMessages.forEach {
                clevertapDefaultInstance.markReadInboxMessage(it.messageId)
            }
        }

        btnOptOut.setOnClickListener {
            clevertapDefaultInstance.setOptOut(true)
        }
        btnOffline.setOnClickListener {
            if (!setOffline) {
                clevertapDefaultInstance.setOffline(true)
                setOffline = true
            } else {
                clevertapDefaultInstance.setOffline(false)
                setOffline = false
            }
        }

        btnNativeDisplay.setOnClickListener {
           // var displayUnits = clevertapDefaultInstance.allDisplayUnits
            //Log.d("CleverTap", "onCreate: $displayUnits")

            val prodViewedAction = mapOf(
                "Channel" to "Native Display"
            )
            clevertapDefaultInstance.pushEvent("Engagement Event", prodViewedAction)
        }


        btnGetLocation.setOnClickListener {

            if (checkPermissions()) {
                if (isLocationEnabled()) {
                    locationTracker.getLocationUpdates(object :LocationTracker.LocationTrackingCallback{
                        override fun receiveLocation(location: Location) {
                            clevertapDefaultInstance.location = location
                            //locationTracker.stopLocationUpdates()
                            Toast.makeText(applicationContext,"Latitude: ${location.latitude}  Longitude: ${location.longitude} ",Toast.LENGTH_SHORT).show()
                        }
                    })
                    locationTracker.startLocationUpdates()
                } else {
                    Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                }
            } else {
                requestPermissions()
            }
        }

        btnStopLocation.setOnClickListener{
            locationTracker.stopLocationUpdates()
        }

        btnPromtNotificationPermission.setOnClickListener {
            //clevertapDefaultInstance.promptForPushPermission(true)
            //clevertapDefaultInstance.pushEvent("Flutter Event")
            clevertapDefaultInstance.pushEvent("Raise PN with App Inbox")
        }

        btnRaiseCustomEvent.setOnClickListener {
            val intent = Intent(this@ProfileMergeCheckActivity, CustomEventActivity::class.java)
            startActivity(intent)

        }

        btnWebView.setOnClickListener {
            val intent = Intent(this@ProfileMergeCheckActivity, WebViewActivity::class.java)
            startActivity(intent)
        }

        btnInitGeofence.setOnClickListener {
            initCTGeofenceApi(clevertapDefaultInstance)
        }

        btnTriggerLocation.setOnClickListener {
            try
            {
                CTGeofenceAPI.getInstance(applicationContext).triggerLocation()
            }
            catch (e:IllegalStateException) {
                // thrown when this method is called before geofence SDK initialization
            }
        }

        btnDeactivateGeofence.setOnClickListener {
            CTGeofenceAPI.getInstance(applicationContext).deactivate()
        }

        btnPushEventForUserRating.setOnClickListener {
            val prodViewedAction = mapOf(
                "Channel" to "User Rating"
            )
            clevertapDefaultInstance.pushEvent("Engagement Event", prodViewedAction)
        }

        btnPushEventForNPSRating.setOnClickListener {
            val prodViewedAction = mapOf(
                "Channel" to "NPS Rating"
            )
            clevertapDefaultInstance.pushEvent("Engagement Event", prodViewedAction)
        }

        btnPushEventForLeadGenerationText.setOnClickListener {
            val prodViewedAction = mapOf(
                "Channel" to "Lead Generation Text"
            )
            clevertapDefaultInstance.pushEvent("Engagement Event", prodViewedAction)
        }

        btnPushEventForLeadGenerationImage.setOnClickListener {
            val prodViewedAction = mapOf(
                "Channel" to "Lead Generation Image"
            )
            clevertapDefaultInstance.pushEvent("Engagement Event", prodViewedAction)
        }

        clevertapDefaultInstance.setInAppNotificationButtonListener(this)
        clevertapDefaultInstance.setDisplayUnitListener(this)
    }

    private fun initCTGeofenceApi(cleverTapInstance: CleverTapAPI) {
        val context = applicationContext!!

        CTGeofenceAPI.getInstance(context).apply {
            init(
                CTGeofenceSettings.Builder()
                    .enableBackgroundLocationUpdates(true)
                    .setLogLevel(com.clevertap.android.geofence.Logger.DEBUG)
                    .setLocationAccuracy(CTGeofenceSettings.ACCURACY_HIGH)
                    .setLocationFetchMode(CTGeofenceSettings.FETCH_CURRENT_LOCATION_PERIODIC)
                    .setGeofenceMonitoringCount(50)
                    .setInterval(1800000) // 1 hour
                    .setFastestInterval(1800000) // 30 minutes
                    .setSmallestDisplacement(200f) // 1 km
                    .setGeofenceNotificationResponsiveness(60000) // 5 minute
                    .build(), cleverTapInstance
            )
            setOnGeofenceApiInitializedListener {
                Toast.makeText(context, "Geofence API initialized", Toast.LENGTH_SHORT).show()
            }
            setCtGeofenceEventsListener(object : CTGeofenceEventsListener {
                override fun onGeofenceEnteredEvent(jsonObject: JSONObject) {
                    Toast.makeText(context, "Geofence Entered", Toast.LENGTH_SHORT).show()
                }

                override fun onGeofenceExitedEvent(jsonObject: JSONObject) {
                    Toast.makeText(context, "Geofence Exited", Toast.LENGTH_SHORT).show()
                }
            })
            setCtLocationUpdatesListener { Toast.makeText(context, "Location updated", Toast.LENGTH_SHORT).show() }
        }
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

     /*fun onInboxItemClicked(message: CTInboxMessage?) {
        Log.d("CleverTap", "onInboxItemClicked: ")
    }*/

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

    @SuppressLint("RestrictedApi")
    override fun onInboxItemClicked(
        message: CTInboxMessage?,
        contentPageIndex: Int,
        buttonIndex: Int
    ) {
        Log.i("CleverTap", "InboxItemClicked at page-index $contentPageIndex with button-index $buttonIndex")

        //The contentPageIndex corresponds to the page index of the content, which ranges from 0 to the total number of pages for carousel templates. For non-carousel templates, the value is always 0, as they only have one page of content.
        val messageContentObject = message?.inboxMessageContents?.get(contentPageIndex)

        //The buttonIndex corresponds to the CTA button clicked (0, 1, or 2). A value of -1 indicates the app inbox body/message clicked.
        if (buttonIndex != -1) {
            //button is clicked
            try {
                val buttonObject: JSONObject? = messageContentObject?.links?.get(buttonIndex) as JSONObject?
                val buttonType = buttonObject?.optString("type")
                Log.i("CleverTap", "type of button clicked: " + buttonType);
            } catch (t: Throwable) {
                t.printStackTrace();
            }
        } else {
            //item is clicked
            Log.i("CleverTap", "type/template of App Inbox item:" + message?.type);
        }
    }


    override fun onPause() {
        super.onPause()
        //locationTracker.stopLocationUpdates()
    }

    override fun onResume() {
        super.onResume()
        //locationTracker.startLocationUpdates()
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        Toast.makeText(this,"Location Permission not granted!!!",Toast.LENGTH_SHORT).show()
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this,"Location Permission Granted",Toast.LENGTH_SHORT).show()
                if (!isLocationEnabled()){
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                }
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: android.location.LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
        return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (data?.action.equals(Settings.ACTION_LOCATION_SOURCE_SETTINGS)){
            Toast.makeText(this,"Settings",Toast.LENGTH_SHORT).show()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}