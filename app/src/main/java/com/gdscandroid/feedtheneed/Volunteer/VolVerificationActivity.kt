package com.gdscandroid.feedtheneed.Volunteer

import android.animation.ValueAnimator
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.gdscandroid.feedtheneed.R
import com.gdscandroid.feedtheneed.Utility
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_vol_verification.*
import java.text.SimpleDateFormat
import java.util.*

class VolVerificationActivity : AppCompatActivity() {

    val REQUEST_CODE = 200
    var count:Int=0
    var ptsl:Boolean = false
    lateinit var arrayList: ArrayList<Uri>
    var voluid:String = ""
    var bookid:String = ""
    var resid:String = ""
    var img_uri: Uri? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vol_verification)

        count = intent.getIntExtra("meals",0)
        voluid = intent.getStringExtra("VolunteerUid")!!
        bookid = intent.getStringExtra("BookingID")!!
        resid = intent.getStringExtra("RestauUid")!!
        arrayList = ArrayList(count)

        select_imv.setOnClickListener {
            openGalleryForImages()
        }

        count1.text = "Total Images to be uploaded : "+count

        share_tv.setOnClickListener {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.share_dialog)
            val info = dialog.findViewById(R.id.et_info) as EditText
            val share_img = dialog.findViewById(R.id.share_img) as ImageView
            val pickBtn = dialog.findViewById(R.id.btnShare) as Button
            dialog.show()

            share_img.setImageURI(img_uri)

            pickBtn.setOnClickListener {
                val pd: ProgressDialog = ProgressDialog(this)
                pd.setMessage("Please Wait...Your data is uploading !!")
                pd.setCancelable(false)
                pd.show()

                val uid:String = Utility.getUid(this).toString()
                val database = FirebaseDatabase.getInstance().reference.child("DonaPost").child(uid).push()
                var ranid = database.key.toString()
                ranid = ranid.substring(1,ranid.length-1)
                val fileName = "image.jpg"

                val refStorage = FirebaseStorage.getInstance().reference.child("Feeds/${ranid}/$fileName")

                img_uri?.let { it1 ->
                    refStorage.putFile(it1)
                        .addOnSuccessListener(
                            OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                                taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                                    val sdf = SimpleDateFormat("dd/M/yyyy hh:mm")
                                    val currentTime = sdf.format(Date())
                                    val profile = it.toString()
                                    val db=FirebaseDatabase.getInstance().reference.child("Feeds").child(ranid)
                                    db.child("name").setValue(Utility.getName(this).toString())
                                    db.child("role").setValue(Utility.getrole(this).toString())
                                    db.child("photo").setValue(Utility.getProfile(this).toString())
                                    db.child("Points").setValue((Utility.getDonationPoint(this)
                                        ?.plus(Utility.getRewardoint(this)!!)).toString())
                                    db.child("Location").setValue(Utility.getLocation(this).toString())
                                    db.child("Latitude").setValue(Utility.getLatitude(this).toString())
                                    db.child("Longitude").setValue(Utility.getLongitude(this).toString())
                                    db.child("feedImage").setValue(profile)
                                    db.child("description").setValue(info.text.toString())
                                    db.child("uid").setValue(voluid)
                                    db.child("feedId").setValue(ranid)
                                    db.child("currentTime").setValue(currentTime.toString())
                                    pd.dismiss()
                                   dialog.dismiss()
                                }
                            })

                        ?.addOnFailureListener(OnFailureListener { e ->
                            print(e.message)
                            dialog.dismiss()
                        })
                }
            }
        }

        verification_tv.setOnClickListener {
            Handler().postDelayed({
                share_tv.visibility = View.VISIBLE
                FirebaseDatabase.getInstance().reference.child("VolunteerMealPost")
                    .child(voluid).child(bookid).child("Status").setValue("Verified")
                FirebaseDatabase.getInstance().reference.child("RestaurantMealPost")
                    .child(resid).child(bookid).child("Status").setValue("Verified")
                FirebaseDatabase.getInstance().reference.child("Users")
                    .child(voluid).addListenerForSingleValueEvent(object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var dP=snapshot.child("RewardPoints").value.toString().toInt()
                            dP+=count*50
                            FirebaseDatabase.getInstance().reference.child("Users")
                                .child(voluid).child("RewardPoints").setValue(dP)
                            Utility.setRewardoint(this@VolVerificationActivity,dP.toLong())
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // TODO("Not yet implemented")
                        }

                    })
                val dialog: Dialog = Dialog(this)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setContentView(R.layout.dialog_verificationdone)

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
                    Toast.makeText(this,"Verified",Toast.LENGTH_SHORT).show()
                }
                dialog.show()

            }, 5000)
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