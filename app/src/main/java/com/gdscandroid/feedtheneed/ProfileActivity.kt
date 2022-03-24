package com.gdscandroid.feedtheneed

import android.Manifest
import android.animation.ValueAnimator
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.core.app.ActivityCompat
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.gdscandroid.feedtheneed.Donator.DonatorHome
import com.gdscandroid.feedtheneed.Restaurant.RestaurantActivity
import com.gdscandroid.feedtheneed.Volunteer.VolunteerHomeActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

class ProfileActivity : AppCompatActivity() {

    var lati:String=""
    var longi:String=""

    //Phone verification
    var number : String =""

    // create instance of firebase auth
    lateinit var auth: FirebaseAuth

    // we will use this to match the sent otp from firebase
    lateinit var storedVerificationId:String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    private lateinit var number_otp: EditText
    private lateinit var otp: EditText
    private lateinit var send_otp: Button
    private lateinit var verify: Button
    private lateinit var detect: Button
    private lateinit var nextbtn: Button
    private lateinit var image: ImageView
    private lateinit var nametxt: EditText
    private lateinit var locationtv: TextView
    private var isVerifired: Boolean = false
    private var isLocation: Boolean = false
    private var Profile_bool: Boolean = false
    private lateinit var credentialss : GoogleAuthCredential
    var email:String="";
    var pass:String="";
    var source:String="";
    var profile:String="";

    lateinit var img_uri:Uri

    //location
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var lastLocation: Location? = null
    var ans : String = ""
    val GALLERY_REQUEST_CODE = 69


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        var name:String = intent.getStringExtra("name").toString()
        var uid:String = intent.getStringExtra("uid").toString()
        profile = intent.getStringExtra("profile").toString()
        source = intent.getStringExtra("source").toString()

        if(source.equals("Custom")){
            email = intent.getStringExtra("email").toString()
            pass = intent.getStringExtra("pass").toString()
        }else if(source.equals("Google")){
            credentialss = intent.extras?.get("credential") as GoogleAuthCredential
        }


        number_otp = findViewById(R.id.enterNum)
        send_otp = findViewById(R.id.sendOtp)
        otp = findViewById(R.id.enterotp)
        verify = findViewById(R.id.verifyOtp)
        detect = findViewById(R.id.location_btn)
        nextbtn = findViewById(R.id.next)
        image = findViewById(R.id.profile)
        nametxt = findViewById(R.id.entername)
        locationtv = findViewById(R.id.location)
        auth=FirebaseAuth.getInstance()

        nametxt.setText(name)
        if(!profile.equals("")){
            Glide.with(this).load(profile).into(image)
            img_uri = Uri.parse("")
            Profile_bool=true
        }

        //role spinner
        val roles = resources.getStringArray(R.array.roles)
        var roleStr : String = "";

        val spinner = findViewById<Spinner>(R.id.spinner2)
        if (spinner != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, roles)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    roleStr = roles[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }

        addFromDevice.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(
                    intent,
                    "Please select..."
                ),
                GALLERY_REQUEST_CODE
            )
        }

        detect.setOnClickListener {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            ans = ""
            if (!checkPermissions()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions()
                }
            }
            else {
                getLastLocation()
            }
        }

        send_otp.setOnClickListener {
            login()
        }

        nextbtn.setOnClickListener {
            if(!isVerifired){
                Toast.makeText(this,"Verify Phone Number", Toast.LENGTH_SHORT).show()
            }else if(!isLocation){
                Toast.makeText(this,"Detect Location", Toast.LENGTH_SHORT).show()
            }else if(!Profile_bool){
                Toast.makeText(this,"Upload Profile", Toast.LENGTH_SHORT).show()
            }else if(nametxt.text.toString().trim().isBlank()){
                Toast.makeText(this,"Enter Name", Toast.LENGTH_SHORT).show()
            }else if(roleStr.equals("Select Role")){
                Toast.makeText(this,"Select a role", Toast.LENGTH_SHORT).show()
            }
            else{
                val pd=ProgressDialog(this)
                pd.setMessage("Please wait!!..")
                pd.setCancelable(false)
                pd.show()

                val fileName = "profile.jpg"

                val database = FirebaseDatabase.getInstance()
                val refStorage = FirebaseStorage.getInstance().reference.child("UserProfile/${Firebase.auth.currentUser?.uid}/$fileName")
                Log.d("bhibhhi",it.toString())

                if (!profile.equals("")){
                    val databse = Firebase.database
                    val ref = databse.getReference("Users").child(uid)
                    ref.child("Name").setValue(nametxt.text.toString())
                    ref.child("Location").setValue(ans)
                    ref.child("Profile").setValue(profile)
                    ref.child("Phone").setValue(number_otp.text.toString())
                    ref.child("Uid").setValue(uid)
                    ref.child("Role").setValue(roleStr)
                    ref.child("RewardPoints").setValue(0)
                    ref.child("DonationPoints").setValue(0)
                    ref.child("Latitude").setValue(lati)
                    ref.child("Longitude").setValue(longi)
                    ref.child("MealsImage").setValue("")
                    ref.child("MealsInfo").setValue("")

                    if(roleStr=="Organization"){
                        val ref2 = databse.getReference("RestaurantMealsData").child(uid)
                        ref2.child("CompletedDonation").setValue("0")
                        ref2.child("TotalDonation").setValue("0")
                        ref2.child("LeftDonation").setValue("0")
                        ref2.child("RestaurPhoto").setValue(profile)
                        ref2.child("latitude").setValue(lati)
                        ref2.child("longitude").setValue(longi)
                        ref2.child("name").setValue(nametxt.text.toString())
                        ref2.child("RestaurPhone").setValue(number_otp.text.toString())
                        ref2.child("MealsImage").setValue("")
                        ref2.child("MealsInfo").setValue("")
                    }

                    Utility.setName(this,nametxt.text.toString())
                    Utility.setLocation(this,ans)
                    Utility.setProfile(this,profile)
                    Utility.setMobile(this,number_otp.text.toString())
                    Utility.setUid(this,uid)
                    Utility.setRole(this,roleStr)
                    Utility.setRewardoint(this,0)
                    Utility.setDonationPoint(this,0)
                    Utility.setProfileComplete(this,true)
                    Utility.setLongitude(this,longi)
                    Utility.setLatitude(this,lati)
                    Utility.setMealDetail(this,"")
                    Utility.setMealPhotoContext(this,"")
                    pd.dismiss()

                    val dialog: Dialog = Dialog(this)
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog.setContentView(R.layout.dialog_registration)

                    val animationView: LottieAnimationView = dialog.findViewById(R.id.animation_view)
                    animationView
                        .addAnimatorUpdateListener { animation: ValueAnimator? -> }
                    animationView
                        .playAnimation()

                    if (animationView.isAnimating) {
                        // Do something.

                    }
                    val pickBtn = dialog.findViewById(R.id.done) as Button
                    pickBtn.setOnClickListener {
                        dialog.cancel()
                        if(Utility.getrole(this).equals("Donator")){
                            intent = Intent(this, DonatorHome::class.java)
                            startActivity(intent);
                            finish()
                        }else if(Utility.getrole(this).equals("Organization")){
                            intent = Intent(this, RestaurantActivity::class.java)
                            startActivity(intent);
                            finish()
                        }else{
                            intent = Intent(this, VolunteerHomeActivity::class.java)
                            startActivity(intent);
                            finish()
                        }
                    }
                    dialog.show()
                }
                else{
                    refStorage.putFile(img_uri)
                        .addOnSuccessListener(
                            OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                                taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                                    profile = it.toString()
                                    Log.d("bhibhhi",it.toString())
                                    val databse = Firebase.database
                                    val ref = databse.getReference("Users").child(uid)
                                    ref.child("Name").setValue(nametxt.text.toString())
                                    ref.child("Location").setValue(ans)
                                    ref.child("Profile").setValue(profile)
                                    ref.child("Phone").setValue(number_otp.text.toString())
                                    ref.child("Uid").setValue(uid)
                                    ref.child("Role").setValue(roleStr)
                                    ref.child("RewardPoints").setValue(0)
                                    ref.child("DonationPoints").setValue(0)
                                    ref.child("Latitude").setValue(lati)
                                    ref.child("Longitude").setValue(longi)
                                    ref.child("MealsImage").setValue("")
                                    ref.child("MealsInfo").setValue("")

                                    if(roleStr=="Organization"){
                                        val ref2 = databse.getReference("RestaurantMealsData").child(uid)
                                        ref2.child("CompletedDonation").setValue("0")
                                        ref2.child("TotalDonation").setValue("0")
                                        ref2.child("LeftDonation").setValue("0")
                                        ref2.child("RestaurPhoto").setValue(profile)
                                        ref2.child("latitude").setValue(lati)
                                        ref2.child("longitude").setValue(longi)
                                        ref2.child("name").setValue(nametxt.text.toString())
                                        ref2.child("RestaurPhone").setValue(number_otp.text.toString())
                                        ref2.child("MealsImage").setValue("")
                                        ref2.child("MealsInfo").setValue("")
                                    }

                                    Utility.setName(this,nametxt.text.toString())
                                    Utility.setLocation(this,ans)
                                    Utility.setProfile(this,profile)
                                    Utility.setMobile(this,number_otp.text.toString())
                                    Utility.setUid(this,uid)
                                    Utility.setRole(this,roleStr)
                                    Utility.setRewardoint(this,0)
                                    Utility.setDonationPoint(this,0)
                                    Utility.setProfileComplete(this,true)
                                    Utility.setLongitude(this,longi)
                                    Utility.setLatitude(this,lati)
                                    Utility.setMealDetail(this,"")
                                    Utility.setMealPhotoContext(this,"")
                                    pd.dismiss()

                                    val dialog: Dialog = Dialog(this)
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                    dialog.setContentView(R.layout.dialog_registration)

                                    val animationView: LottieAnimationView = dialog.findViewById(R.id.animation_view)
                                    animationView
                                        .addAnimatorUpdateListener { animation: ValueAnimator? -> }
                                    animationView
                                        .playAnimation()

                                    if (animationView.isAnimating) {
                                        // Do something.

                                    }
                                    val pickBtn = dialog.findViewById(R.id.done) as Button
                                    pickBtn.setOnClickListener {
                                        dialog.cancel()
                                        if(Utility.getrole(this).equals("Donator")){
                                            intent = Intent(this, DonatorHome::class.java)
                                            startActivity(intent);
                                            finish()
                                        }else if(Utility.getrole(this).equals("Organization")){
                                            intent = Intent(this, RestaurantActivity::class.java)
                                            startActivity(intent);
                                            finish()
                                        }else{
                                            intent = Intent(this, VolunteerHomeActivity::class.java)
                                            startActivity(intent);
                                            finish()
                                        }
                                    }
                                    dialog.show()

                                    //DownloadImageFromInternet(image).execute(profile)
                                }
                            })

                        ?.addOnFailureListener(OnFailureListener { e ->
                            pd.dismiss()
                            Log.d("bhibhhi",e.message.toString())
                            print(e.message)
                        })
                }

            }
        }

        verify.setOnClickListener {
            val otp = otp.text.trim().toString()
            if(otp.isNotEmpty()){
                val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(
                    storedVerificationId.toString(), otp)
                signInWithPhoneAuthCredential(credential)
            }else{
                Toast.makeText(this,"Enter OTP", Toast.LENGTH_SHORT).show()
            }
        }

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            // This method is called when the verification is completed
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                //startActivity(Intent(applicationContext, MainActivity::class.java))
                Log.d("GFG" , "onVerificationCompleted Success")
            }

            // Called when verification is failed add log statement to see the exception
            override fun onVerificationFailed(e: FirebaseException) {
                Log.d("GFG" , "onVerificationFailed  $e")
            }

            // On code is sent by the firebase this method is called
            // in here we start a new activity where user can enter the OTP
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d("GFG","onCodeSent: $verificationId")
                storedVerificationId = verificationId
                resendToken = token

                // Start a new activity using intent
                // also send the storedVerificationId using intent
                // we will use this id to send the otp back to firebase
//                val intent = Intent(applicationContext,OtpActivity::class.java)
//                intent.putExtra("storedVerificationId",storedVerificationId)
//                startActivity(intent)
                Toast.makeText(this@ProfileActivity,"Code Sent", Toast.LENGTH_SHORT).show()
                otp.visibility = View.VISIBLE
                verify.visibility = View.VISIBLE
            }
        }
    }

    private fun login() {
        number = number_otp.text.trim().toString()

        // get the phone number from edit text and append the country cde with it
        if (number.isNotEmpty()){
            number = "+91$number"
            sendVerificationCode(number)
        }else{
            Toast.makeText(this,"Enter mobile number", Toast.LENGTH_SHORT).show()
        }
    }

    // this method sends the verification code
    // and starts the callback of verification
    // which is implemented above in onCreate
    private fun sendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        Log.d("GFG" , "Auth started")
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //val intent = Intent(this , MainActivity::class.java)
                    //startActivity(intent)
                    auth.currentUser?.let { Log.d("GFG" , it.uid) }
                    auth.signOut()
                    Toast.makeText(this,"Verified", Toast.LENGTH_SHORT).show()
                    isVerifired = true
                    if(source.equals("Custom")){
                        auth.signInWithEmailAndPassword(email,pass)
                    }else if(source.equals("Google")){
                        auth.signInWithCredential(credentialss)
                    }
                } else {
                    // Sign in failed, display a message and update the UI
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(this,"Invalid OTP", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient?.lastLocation!!.addOnCompleteListener(this) { task ->
            if (task.isSuccessful && task.result != null) {
                lastLocation = task.result

                val mGeocoder = Geocoder(applicationContext, Locale.getDefault())
                var addressString= ""

                // Reverse-Geocoding starts
                try {
                    lati = lastLocation?.latitude.toString()
                    longi=lastLocation?.longitude.toString()
                    val addressList: List<Address> = mGeocoder.getFromLocation(lastLocation!!.latitude, lastLocation!!.longitude, 1)

                    // use your lat, long value here
                    if (addressList != null && addressList.isNotEmpty()) {
                        val address = addressList[0]
                        val sb = StringBuilder()
                        for (i in 0 until address.maxAddressLineIndex) {
                            sb.append(address.getAddressLine(i)).append("\n")
                        }

                        // Various Parameters of an Address are appended
                        // to generate a complete Address
                        sb.append(address.getAddressLine(0))

                        // StringBuilder sb is converted into a string
                        // and this value is assigned to the
                        // initially declared addressString string.
                        addressString = sb.toString()
                        Log.d("bhibhi",addressString)
                        locationtv.text = addressString
                        ans = addressString
                        isLocation = true
                    }
                } catch (e: IOException) {
                    Toast.makeText(applicationContext,"Unable connect to Geocoder"+e.message,Toast.LENGTH_LONG).show()
                }
            }
            else {
                Log.w("ProfileActivity", "getLastLocation:exception", task.exception)
            }
        }
    }

    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }
    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE
        )
    }

    private fun showSnackbar(
        mainTextStringId: String, actionStringId: String,
        listener: View.OnClickListener
    ) {
        Toast.makeText(this, mainTextStringId, Toast.LENGTH_LONG).show()
    }

    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")
            showSnackbar("Location permission is needed for core functionality", "Okay",
                View.OnClickListener {
                    startLocationPermissionRequest()
                })
        }
        else {
            Log.i(TAG, "Requesting permission")
            startLocationPermissionRequest()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            when {
                grantResults.isEmpty() -> {
                    // If user interaction was interrupted, the permission request is cancelled and you
                    // receive empty arrays.
                    Log.i(TAG, "User interaction was cancelled.")
                }
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    // Permission granted.
                    getLastLocation()
                }
                else -> {
                    showSnackbar("Permission was denied", "Settings",
                        View.OnClickListener {
                            // Build intent that displays the App settings screen.
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts(
                                "package",
                                Build.DISPLAY, null
                            )
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }
    companion object {
        private val TAG = "LocationProvider"
        private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {

        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )

        if (requestCode == GALLERY_REQUEST_CODE
            && resultCode == Activity.RESULT_OK
            && data != null
            && data.data != null
        ) {

            // Get the Uri of data
            val file_uri = data.data
            if (file_uri != null) {
                //uploadImageToFirebase(file_uri)
                image.setImageURI(file_uri)
                img_uri = file_uri
                Profile_bool=true
            }
        }
    }

    private fun uploadImageToFirebase(fileUri: Uri) {

    }

    override fun onPause() {
        super.onPause()
        Log.d("kahokaho","Pause is here")
    }

    override fun onDestroy() {
        lateinit var mGoogleSignInClient: GoogleSignInClient
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id1))
            .requestEmail()
            .build()

        mGoogleSignInClient= GoogleSignIn.getClient(this,gso)
        Log.d("kahokaho","destroy is here")
        mGoogleSignInClient.signOut().addOnCompleteListener {
            Log.d("kahokaho","logout");
            Firebase.auth.signOut()
            finish()
        }
        super.onDestroy()
    }

}