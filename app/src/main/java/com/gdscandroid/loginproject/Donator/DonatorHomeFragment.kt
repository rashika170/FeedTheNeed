package com.gdscandroid.loginproject.Donator

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.util.Util
import com.gdscandroid.loginproject.R
import com.gdscandroid.loginproject.Utility
import com.gdscandroid.loginproject.feeds.FeedData
import com.gdscandroid.loginproject.feeds.FeedRVAdapter
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_donator_home.*
import java.net.URI
import java.util.*
import kotlin.collections.ArrayList

class DonatorHomeFragment : Fragment() {

    val GALLERY_REQUEST_CODE = 69
    lateinit var dialog:Dialog
    lateinit var uri:Uri

    val dbRef= FirebaseDatabase.getInstance().reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_donator_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val feedData=ArrayList<FeedData>()
        val feedRVAdapter= FeedRVAdapter(feedData)
        rvFeed.layoutManager= LinearLayoutManager(activity)
        rvFeed.adapter=feedRVAdapter

        val postListener= object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val post:FeedData?=snapshot.getValue(FeedData::class.java)
                if(post!=null){
                    feedData.add(post)
                }
                feedRVAdapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }

        }
        dbRef.child("feeds").addChildEventListener(postListener)

    }

    override fun onStart() {
        super.onStart()
        if (Utility.getMealDetail(requireContext()).toString().equals("") && Utility.getrole(requireActivity()).toString().equals("Organization")){
            dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.meal_info_dialog)
            dialog.setCancelable(false)
            val mealInfo = dialog.findViewById(R.id.et_mealinfo) as EditText
            val mealImage = dialog.findViewById(R.id.mealImage) as ImageView
            val pickBtn = dialog.findViewById(R.id.btnSet) as Button

            mealImage.setOnClickListener {
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

            pickBtn.setOnClickListener {
                val info = mealInfo.text
                val fileName = "mealPhoto.jpg"

                val database = FirebaseDatabase.getInstance().reference
                val refStorage = FirebaseStorage.getInstance().reference.child("UserProfile/${Firebase.auth.currentUser?.uid}/$fileName")

                refStorage.putFile(uri)
                    .addOnSuccessListener(
                        OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                            taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                                val meal_imguri  = it.toString()
                                database.child("Users").child(Utility.getUid(requireActivity()).toString())
                                    .child("MealsImage").setValue(meal_imguri)
                                database.child("Users").child(Utility.getUid(requireActivity()).toString())
                                    .child("MealsInfo").setValue(info.toString())

                                database.child("RestaurantMealsData").child(Utility.getUid(requireActivity()).toString())
                                    .child("MealsImage").setValue(meal_imguri)
                                database.child("RestaurantMealsData").child(Utility.getUid(requireActivity()).toString())
                                    .child("MealsInfo").setValue(info.toString())

                                Utility.setMealPhotoContext(requireActivity(),meal_imguri)
                                Utility.setMealDetail(requireActivity(),info.toString())
                                dialog.dismiss()
                            }
                        })

                    ?.addOnFailureListener(OnFailureListener { e ->
                        print(e.message)
                        dialog.dismiss()
                    })
            }
            dialog.show()
        }
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
                dialog.findViewById<ImageView>(R.id.mealImage).setImageURI(file_uri)
                uri = file_uri
            }
        }
    }
}