package com.gdscandroid.feedtheneed.Restaurant

import android.animation.ValueAnimator
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.gdscandroid.feedtheneed.R
import com.gdscandroid.feedtheneed.Utility
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class RestaurantPOSFragment : Fragment() {

//    private lateinit var image: ImageView
//    private lateinit var descedit: EditText
    private lateinit var phonetext: EditText
    private lateinit var numberEdit: EditText
    private lateinit var post: Button
    private lateinit var name:EditText
    private var isImage: Boolean = false
    private var imageUrl : String=""
    private lateinit var fileUri1: Uri

    var timenans : String = ""
    val GALLERY_REQUEST_CODE = 69

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private fun setCurrentFragment(fragment: Fragment) =
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.flFragment,fragment)
            commit()
        }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v =  inflater.inflate(R.layout.fragment_restaurant_p_o_s, container, false)
//        image = v.findViewById(R.id.imageView)
//        descedit = v.findViewById(R.id.desc_edit)
        phonetext = v.findViewById(R.id.phn_txt)
        numberEdit = v.findViewById(R.id.no_edit)
        post = v.findViewById(R.id.post)
        name=v.findViewById(R.id.name_edit)



        //Glide.with(requireContext()).load(activity?.let { Utility.getProfile(it).toString() }).into(profile_nav1)

        v.findViewById<ImageView>(R.id.profile_nav1)?.let {
            Glide.with(requireActivity()).load(Utility.getProfileContext(requireContext())).into(
                it
            )
        }

        getCurrentTime()

        v.findViewById<TextView>(R.id.restaur_name)?.setText(Utility.getName(requireActivity()))
        v.findViewById<TextView>(R.id.meal_detail)?.setText(Utility.getMealDetail(requireActivity()))
        v.findViewById<TextView>(R.id.meal_cost)?.setText("Rs. "+Utility.getMealCostDetail(requireActivity())+" INR")
        v.findViewById<ImageView>(R.id.meal_img)?.let {
            Glide.with(requireActivity()).load(Utility.getMealPhotoContext(requireContext())).into(
                it
            )
        }


        post.setOnClickListener {
//            if(descedit.text.toString().trim().isBlank()){
//                Toast.makeText(context,"Upload Description", Toast.LENGTH_SHORT).show()
//            }else
            if(numberEdit.text.toString().trim().isEmpty()){
                Toast.makeText(context,"Upload No of Meals", Toast.LENGTH_SHORT).show()
            }else if(name.text.toString().trim().isEmpty()){
                Toast.makeText(context,"Please enter name of the donator", Toast.LENGTH_SHORT).show()
            }else if(phonetext.text.toString().trim().isEmpty()){
                Toast.makeText(context,"Please enter Phone number of the donator", Toast.LENGTH_SHORT).show()
            }else{
                uploadDataToFirebase()
            }
        }
        return v
    }


    private fun uploadDataToFirebase() {
        val uid:String = activity?.let { Utility.getUid(it).toString() }.toString()
        val database = FirebaseDatabase.getInstance().reference.child("RestaurentPost").child(uid).push()
        var ranid = database.key.toString()
        ranid = ranid.substring(1,ranid.length-1)
        Log.d("bhibhi",ranid)
        val database2 = FirebaseDatabase.getInstance().reference.child("RestaurantData").child(uid).child(ranid)
        database2.child("desc").setValue("")
        database2.child("uid").setValue(uid)
        database2.child("date").setValue(timenans)
        database2.child("NumMeals").setValue(numberEdit.text.toString().trim())
        database2.child("name").setValue(name.text.toString())
        database2.child("phone").setValue(phonetext.text.toString().trim())

        val database3=FirebaseDatabase.getInstance().reference.child("RestaurantMealsData").child(uid)
        database3.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var tDonation:Int=snapshot.child("TotalDonation").value.toString().toInt()
                var lDonation:Int= snapshot.child("LeftDonation").value.toString().toInt()
                var cDonation:Int=snapshot.child("CompletedDonation").value.toString().toInt()
                var donation:Int = numberEdit.text.toString().toInt()
                tDonation += donation
                lDonation += donation

                database3.child("TotalDonation").setValue(tDonation.toString())
                database3.child("LeftDonation").setValue(lDonation.toString())

                val dialog:Dialog = Dialog(context!!)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setContentView(R.layout.dialog_pos_booked)

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
                    startActivity(Intent(activity,RestaurantActivity::class.java))
                }
                dialog.show()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        Toast.makeText(context,"Post Uploaded Successfully", Toast.LENGTH_SHORT).show()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentTime() {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formatted = current.format(formatter)
        timenans = formatted
    }
}