package com.gdscandroid.loginproject.Volunteer

import android.animation.ValueAnimator
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.gdscandroid.loginproject.Donator.DonatorHome
import com.gdscandroid.loginproject.Donator.DonorData
import com.gdscandroid.loginproject.R
import com.gdscandroid.loginproject.Utility
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.doantor_item.view.*
import kotlinx.android.synthetic.main.volunteer_restaur_item.view.*
import java.util.*
import kotlin.collections.ArrayList


class VolunteerAPllyAdapter(val restaurData:ArrayList<AvailableRestauData>): RecyclerView.Adapter<VolunteerAPllyAdapter.RVViewHolder>(),
    DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener{
    class RVViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

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

    var time:String=""
    var isPickedTime=false

    lateinit var dialog:Dialog


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(
            R.layout.volunteer_restaur_item,parent,false
        )
        return RVViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RVViewHolder, position: Int) {
        holder.itemView.restau_name_vri.text=restaurData[position].name
        holder.itemView.nomeals_vri.text=restaurData[position].LeftDonation
        Glide.with(holder.itemView.context).load(restaurData[position].RestaurPhoto).into(holder.itemView.restaurantPhoto)
        holder.itemView.apply_vri.setOnClickListener {
            dialog = Dialog(holder.itemView.context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.book_meal_dialog)
            val numMeals = dialog.findViewById(R.id.et_numMeals) as EditText
            val pickBtn = dialog.findViewById(R.id.btnPickTime) as Button
            pickBtn.setOnClickListener {
                val calendar: Calendar = Calendar.getInstance()
                day = calendar.get(Calendar.DAY_OF_MONTH)
                month = calendar.get(Calendar.MONTH)
                year = calendar.get(Calendar.YEAR)
                val datePickerDialog =
                    DatePickerDialog(holder.itemView.context, this, year, month,day)
                datePickerDialog.show()
            }

            val bookBtn=dialog.findViewById(R.id.btnBook) as Button
            bookBtn.setOnClickListener {
                if(numMeals.text.toString().trim().toInt()<=restaurData[position].LeftDonation!!.toInt()){
                    if (isPickedTime==false){
                        Toast.makeText(holder.itemView.context,"Please pick time for pick on restaurant",Toast.LENGTH_SHORT).show()
                    }else{
                        val dbRef=FirebaseDatabase.getInstance().reference.child("RestaurantMealsData").child(restaurData[position].uid.toString())
                        val lMeals=restaurData[position].LeftDonation.toString().toInt()
                        val noMeals=numMeals.text.toString().trim().toInt()
                        dbRef.child("LeftDonation").setValue((lMeals-noMeals).toString())
                        val uid = restaurData[position].volUid
                        val database = FirebaseDatabase.getInstance().reference.child("VolunteerPost").child(
                            uid.toString()
                        ).push()
                        var ranid = database.key.toString()
                        ranid = ranid.substring(1,ranid.length-1)
                        val dbRef1=FirebaseDatabase.getInstance().reference.child("VolunteerMealPost").child(uid.toString()).child(ranid)
                        dbRef1.child("numOfMeals").setValue(noMeals.toString())
                        dbRef1.child("restaurantName").setValue(restaurData[position].name.toString())
                        dbRef1.child("expectedPickTime").setValue(time)
                        dbRef1.child("Status").setValue("Booked")
                        dbRef1.child("RestaurantUid").setValue(restaurData[position].uid)
                        dbRef1.child("BookingId").setValue(ranid)
                        dbRef1.child("RestaurantPhone").setValue(restaurData[position].RestaurPhone)
                        dbRef1.child("Verification").setValue("NA")
                        dbRef1.child("VolunteerUid").setValue(restaurData[position].volUid)
                        dbRef1.child("RestaurPhoto").setValue(restaurData[position].RestaurPhoto)

                        val dbRef2=FirebaseDatabase.getInstance().reference.child("RestaurantMealPost").child(restaurData[position].uid.toString()).child(ranid)
                        dbRef2.child("numOfMeals").setValue(noMeals.toString())
                        dbRef2.child("VolunteerName").setValue(restaurData[position].VolName.toString())
                        dbRef2.child("expectedPickTime").setValue(time)
                        dbRef2.child("Status").setValue("Booked")
                        dbRef2.child("VolunteerUid").setValue(restaurData[position].volUid)
                        dbRef2.child("BookingId").setValue(ranid)
                        dbRef2.child("VolunteerNumber").setValue(restaurData[position].volunteerphone)
                        dbRef2.child("Verification").setValue("NA")
                        dbRef2.child("RestaurantUid").setValue(restaurData[position].uid)
                        dbRef2.child("VolunteerPhoto").setValue(Utility.getProfileContext(holder.itemView.context).toString())

                        val dialog2: Dialog = Dialog(holder.itemView.context)
                        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE)
                        dialog2.setContentView(R.layout.dialog_meal_booked)

                        val animationView: LottieAnimationView = dialog2.findViewById(R.id.animation_view)
                        animationView
                            .addAnimatorUpdateListener { animation: ValueAnimator? -> }
                        animationView
                            .playAnimation()

                        if (animationView.isAnimating) {
                            // Do something.

                        }
                        val pickBtn = dialog2.findViewById(R.id.done) as Button
                        pickBtn.setOnClickListener {
                            dialog2.cancel()
                            dialog.dismiss()
                        }
                        dialog2.show()


                    }
                }else{
                    Toast.makeText(holder.itemView.context,"Select meals less than or equal to available meals",Toast.LENGTH_SHORT).show()
                }
            }

            dialog.show()
        }
    }

    override fun getItemCount(): Int = restaurData.size

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        myDay = dayOfMonth
        myYear = year
        myMonth = month
        val calendar: Calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(view!!.context, this, hour, minute,
            DateFormat.is24HourFormat(view.context)
        )
        timePickerDialog.show()
    }
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        myHour = hourOfDay
        myMinute = minute
        time = ""+myYear+"/"+myMonth+"/"+myDay+","+myHour+":"+myMinute
        dialog.findViewById<TextView>(R.id.pick_time_tv).text = myDay.toString() +":"+ myMonth.toString() + ":"+ myYear.toString() + " " + myHour.toString() + ":"  + myMinute.toString()
        isPickedTime = true
        Toast.makeText(view!!.context,"time picked"+time,Toast.LENGTH_SHORT).show()
    }

}