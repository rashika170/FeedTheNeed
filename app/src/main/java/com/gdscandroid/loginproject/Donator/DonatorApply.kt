package com.gdscandroid.loginproject.Donator

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.format.DateFormat.is24HourFormat
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.IOException
import com.gdscandroid.loginproject.HomeActivity
import com.gdscandroid.loginproject.R
import com.gdscandroid.loginproject.Utility
import kotlinx.android.synthetic.main.activity_donator_apply.*
import kotlinx.android.synthetic.main.fragment_donator_home.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class DonatorApply : AppCompatActivity(),DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private var lati:String =""
    private var longi:String =""
    private lateinit var image:ImageView
    private lateinit var descedit: EditText
//    private lateinit var locatext: TextView
//    private lateinit var timetxt: TextView
    private lateinit var pickedtimetxt: TextView
    private lateinit var post: Button
    private lateinit var pick: Button
    private var isLocation: Boolean = false
    private var isImage: Boolean = false
    private var isPickedTime: Boolean = false
    private var imageUrl : String=""
    private lateinit var fileUri1: Uri

    var day = 0
    var month: Int = 0
    var year: Int = 0
    var hour: Int = 0
    var minute: Int = 0
    var myDay = 0
    var myMonth: Int = 0
    var myYear: Int = 0
    var myHour: Int = 0
    var myMinute: Int = 0

    //location
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var lastLocation: Location? = null
    var locatnans : String = ""
    var timenans : String = ""
    val GALLERY_REQUEST_CODE = 69

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donator_apply)



        Glide.with(this).load(Utility.getProfile(this)).into(profile_nav2)

        image = findViewById(R.id.imageView)
        descedit = findViewById(R.id.desc_edit)
        //locatext = findViewById(R.id.locatn_txt)
        pickedtimetxt = findViewById(R.id.pickedtime_txt)
        post = findViewById(R.id.post)
        pick = findViewById(R.id.pick)

        locationGet()
        //getCurrentTime()

        pick.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            day = calendar.get(Calendar.DAY_OF_MONTH)
            month = calendar.get(Calendar.MONTH)
            year = calendar.get(Calendar.YEAR)
            val datePickerDialog =
                DatePickerDialog(this, this, year, month,day)
            datePickerDialog.show()
        }

        addImage.setOnClickListener {
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

        post.setOnClickListener {
            if(descedit.text.toString().trim().isBlank()){
                Toast.makeText(this,"Upload Description", Toast.LENGTH_SHORT).show()
            }else if(!isImage){
                Toast.makeText(this,"Upload Image", Toast.LENGTH_SHORT).show()
            }else if(!isPickedTime){
                Toast.makeText(this,"Upload Picking time", Toast.LENGTH_SHORT).show()
            }else{
                uploadDataToFirebase()
            }
        }
    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun getCurrentTime() {
//        val current = LocalDateTime.now()
//        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")
//        val formatted = current.format(formatter)
////        timetxt.text = formatted
// //       timenans = formatted
//    }

    private fun locationGet() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locatnans = ""
        if (!checkPermissions()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions()
            }
        }
        else {
            getLastLocation()
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
                    lati=lastLocation?.latitude.toString()
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
                        Log.d("raatmekaam",address.toString())
                        Log.d("raatmekaam",address.adminArea.toString())
                        sb.append(address.getAddressLine(0))

                        // StringBuilder sb is converted into a string
                        // and this value is assigned to the
                        // initially declared addressString string.
                        addressString = sb.toString()
                        Log.d("bhibhi",addressString)
//                        locatext.text = addressString
                        locatnans = addressString
                        isLocation = true
                    }
                } catch (e: IOException) {
                    Toast.makeText(applicationContext,"Unable connect to Geocoder", Toast.LENGTH_LONG).show()
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
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
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
                image.setImageURI(file_uri)
                fileUri1 = file_uri
                isImage = true
            }
        }
    }

    private fun uploadDataToFirebase() {
        val pd:ProgressDialog = ProgressDialog(this)
        pd.setMessage("Please Wait...Your data is uploading !!")
        pd.setCancelable(false)
        pd.show()
        val fileName = "image.jpg"
        val uid:String = Utility.getUid(this).toString()
        val database = FirebaseDatabase.getInstance().reference.child("DonatorPost").child(uid).push()
        var ranid = database.key.toString()
        ranid = ranid.substring(1,ranid.length-1)
        Log.d("bhibhi",ranid)
        val refStorage = FirebaseStorage.getInstance().reference.child("DonatorPost/${Firebase.auth.currentUser?.uid}/${ranid}/$fileName")
        refStorage.putFile(fileUri1)
            .addOnSuccessListener(
                OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                        imageUrl = it.toString()
                        val database2 = FirebaseDatabase.getInstance().reference.child("DonatorPost").child(ranid)
                        database2.child("image").setValue(imageUrl)
                        database2.child("desc").setValue(descedit.text.toString())
                        database2.child("location").setValue(locatnans)
                        database2.child("uid").setValue(uid)
                        database2.child("time").setValue(timenans)
                        database2.child("status").setValue("Posted")
                        database2.child("pickedBy").setValue("NoOne")
                        database2.child("pickedTime").setValue(pickedtimetxt.text.toString())
                        database2.child("verificationLink").setValue("NA")
                        database2.child("latitude").setValue(lati)
                        database2.child("longitude").setValue(longi)
                        database2.child("bookId").setValue(ranid)
                        database2.child("donatorPic").setValue(Utility.getProfile(this).toString())
                        database2.child("donatorPhone").setValue(Utility.getMobile(this).toString())
                        database2.child("donatorName").setValue(Utility.getName(this).toString())
                        pd.dismiss()
                        Toast.makeText(this,"Post Uploaded Successfully", Toast.LENGTH_SHORT).show()
                        intent = Intent(this, DonatorHome::class.java)
                        startActivity(intent)
                    }
                })

            .addOnFailureListener(OnFailureListener { e ->
                Toast.makeText(this,"Something Went Wrong", Toast.LENGTH_SHORT).show()
            })
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        myDay = dayOfMonth
        myYear = year
        myMonth = month
        val calendar: Calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(this, this, hour, minute,
            is24HourFormat(this))
        timePickerDialog.show()
    }
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        myHour = hourOfDay
        myMinute = minute
        pickedtimetxt.text = ""+myYear+"/"+myMonth+"/"+myDay+","+myHour+":"+myMinute
        //pickedtimetxt.text = "Year: " + myYear + "\n" + "Month: " + myMonth + "\n" + "Day: " + myDay + "\n" + "Hour: " + myHour + "\n" + "Minute: " + myMinute
        isPickedTime = true
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.flFragment,fragment)
            commit()
        }
}