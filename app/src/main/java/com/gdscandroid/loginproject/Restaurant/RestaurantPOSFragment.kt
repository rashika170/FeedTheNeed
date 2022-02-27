package com.gdscandroid.loginproject.Restaurant

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import com.gdscandroid.loginproject.Donator.DonatorHome
import com.gdscandroid.loginproject.R
import com.gdscandroid.loginproject.Utility
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class RestaurantPOSFragment : Fragment() {

    private lateinit var image: ImageView
    private lateinit var descedit: EditText
    private lateinit var phonetext: TextView
    private lateinit var timetxt: TextView
    private lateinit var numberEdit: EditText
    private lateinit var post: Button

    private var isImage: Boolean = false
    private var imageUrl : String=""
    private lateinit var fileUri1: Uri

    var timenans : String = ""
    val GALLERY_REQUEST_CODE = 69

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v =  inflater.inflate(R.layout.fragment_restaurant_p_o_s, container, false)
        image = v.findViewById(R.id.imageView)
        descedit = v.findViewById(R.id.desc_edit)
        phonetext = v.findViewById(R.id.phn_txt)
        timetxt = v.findViewById(R.id.date_txt)
        numberEdit = v.findViewById(R.id.no_edit)
        post = v.findViewById(R.id.post)

        getCurrentTime()

        image.setOnClickListener {
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
                Toast.makeText(context,"Upload Description", Toast.LENGTH_SHORT).show()
            }else if(!isImage){
                Toast.makeText(context,"Upload Image", Toast.LENGTH_SHORT).show()
            }else if(!numberEdit.text.toString().trim().isEmpty()){
                Toast.makeText(context,"Upload No of Meals", Toast.LENGTH_SHORT).show()
            }else{
                uploadDataToFirebase()
            }
        }
        return v
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
        val fileName = "image.jpg"
        val uid:String = activity?.let { Utility.getUid(it).toString() }.toString()
        val database = FirebaseDatabase.getInstance().reference.child("RestaurentPost").child(uid).push()
        var ranid = database.key.toString()
        ranid = ranid.substring(1,ranid.length-1)
        Log.d("bhibhi",ranid)
        val refStorage = FirebaseStorage.getInstance().reference.child("DonatorPost/${Firebase.auth.currentUser?.uid}/${ranid}/$fileName")
        refStorage.putFile(fileUri1)
            .addOnSuccessListener(
                OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                        imageUrl = it.toString()
                        val database2 = FirebaseDatabase.getInstance().reference.child("DonatorPost").child(uid).child(ranid)
                        database2.child("image").setValue(imageUrl)
                        database2.child("desc").setValue(descedit.text.toString())
                        database2.child("uid").setValue(uid)
                        database2.child("time").setValue(timenans)
                        database2.child("status").setValue("Posted")
                        database2.child("PickedBy").setValue("NoOne")

                        database2.child("verificationLink").setValue("NA")
                        Toast.makeText(context,"Post Uploaded Successfully", Toast.LENGTH_SHORT).show()
                    }
                })

            .addOnFailureListener(OnFailureListener { e ->
                Toast.makeText(context,"Something Went Wrong", Toast.LENGTH_SHORT).show()
            })
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentTime() {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formatted = current.format(formatter)
        timetxt.text = formatted
        timenans = formatted
    }
}