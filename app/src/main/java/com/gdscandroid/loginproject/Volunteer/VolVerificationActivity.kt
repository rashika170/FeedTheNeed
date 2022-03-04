package com.gdscandroid.loginproject.Volunteer

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.gdscandroid.loginproject.R
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_donator_apply.*
import kotlinx.android.synthetic.main.activity_vol_verification.*

class VolVerificationActivity : AppCompatActivity() {

    val REQUEST_CODE = 200
    var count:Int=0
    var ptsl:Boolean = false
    lateinit var arrayList: ArrayList<Uri>
    var voluid:String = ""
    var bookid:String = ""
    var resid:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vol_verification)

        count = intent.getIntExtra("meals",0)
        voluid = intent.getStringExtra("VolunteerUid")!!
        bookid = intent.getStringExtra("BookingID")!!
        resid = intent.getStringExtra("RestauUid")!!
        arrayList = ArrayList(count)

        verification_imv.setOnClickListener {
            openGalleryForImages()
        }

        verification_tv.setOnClickListener {
            if (ptsl){
                FirebaseDatabase.getInstance().reference.child("VolunteerMealPost")
                    .child(voluid).child(bookid).child("Status").setValue("Verified")
                FirebaseDatabase.getInstance().reference.child("RestaurantMealPost")
                    .child(resid).child(bookid).child("Status").setValue("Verified")
                Toast.makeText(this,"Verified",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"Please Select as no of images as meals",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openGalleryForImages() {
        var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE);

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){

            var img_count=0;
            var img_uri: Uri? = null;
            // if multiple images are selected
            if (data?.getClipData() != null) {
                var count = data.clipData!!.itemCount
                img_count = count
                for (i in 0..count - 1) {
                    var imageUri: Uri = data.clipData!!.getItemAt(i).uri
                    arrayList.add(imageUri)
                    //     iv_image.setImageURI(imageUri) Here you can assign your Image URI to the ImageViews
                }
                img_uri = data.clipData!!.getItemAt(0).uri
                // if single image is selected
            } else if (data?.getData() != null) {

                    img_count = 1
                var imageUri: Uri = data.data!!
                img_uri = imageUri
                arrayList.add(imageUri)

            }
            if(img_count!=count){
                Toast.makeText(this,"Please Select as no of images as meals",Toast.LENGTH_SHORT).show()
            }else{
                verification_imv.setImageURI(img_uri)
                ptsl = true
            }
        }
    }
}