package com.gdscandroid.loginproject.Restaurant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.gdscandroid.loginproject.Donator.DonatorHomeFragment
import com.gdscandroid.loginproject.R
import kotlinx.android.synthetic.main.activity_restaurent.*

class RestaurantActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurent)


        val homeFragment = DonatorHomeFragment()
        val mealsFragment = RestaurantMealsFragment()
        val profileFragment = RestaurantProfileFragment()
        val posFragment = RestaurantPOSFragment()

        setCurrentFragment(homeFragment)
        feed_restaur.setImageDrawable(getDrawable(R.drawable.feed_selected))

        feed_restaur.setOnClickListener {
            feed_restaur.setImageDrawable(getDrawable(R.drawable.feed_selected))
            meal_restaur.setImageDrawable(getDrawable(R.drawable.restaurant))
            pos_restaur.setImageDrawable(getDrawable(R.drawable.bookings))
            setCurrentFragment(homeFragment)
        }

        meal_restaur.setOnClickListener {
            meal_restaur.setImageDrawable(getDrawable(R.drawable.restaurant_selected))
            feed_restaur.setImageDrawable(getDrawable(R.drawable.feed))
            pos_restaur.setImageDrawable(getDrawable(R.drawable.bookings))
            setCurrentFragment(mealsFragment)
        }

        pos_restaur.setOnClickListener {
            pos_restaur.setImageDrawable(getDrawable(R.drawable.bookings_selcted))
            meal_restaur.setImageDrawable(getDrawable(R.drawable.restaurant))
            feed_restaur.setImageDrawable(getDrawable(R.drawable.feed))
            setCurrentFragment(posFragment)
        }

//        bottomNavigationViewRestaurant.setOnNavigationItemSelectedListener {
//            when(it.itemId){
//                R.id.home ->setCurrentFragment(homeFragment)
//                R.id.meals ->setCurrentFragment(mealsFragment)
//                R.id.profile->setCurrentFragment(profileFragment)
//                R.id.pos -> setCurrentFragment(posFragment)
//            }
//            true
//        }


    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragmentRestaurant,fragment)
            commit()
        }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        var inflater : MenuInflater = menuInflater
//        inflater.inflate(R.menu.home_menu,menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId){
//            R.id.logout ->{
//                lateinit var mGoogleSignInClient: GoogleSignInClient
//                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                    .requestIdToken(getString(R.string.default_web_client_id1))
//                    .requestEmail()
//                    .build()
//                mGoogleSignInClient= GoogleSignIn.getClient(this,gso)
//                mGoogleSignInClient.signOut().addOnCompleteListener {
//                    Log.d("Homeactivit","logout");
//                    Firebase.auth.signOut()
//                    val intent = Intent(this, MainActivity::class.java)
//                    startActivity(intent)
//                    Utility.setName(this,"")
//                    Utility.setLocation(this,"")
//                    Utility.setProfile(this,"")
//                    Utility.setMobile(this,"")
//                    Utility.setUid(this,"")
//                    Utility.setRole(this,"")
//                    Utility.setRewardoint(this,0)
//                    Utility.setDonationPoint(this,0)
//                    Utility.setProfileComplete(this,false)
//                    Utility.setMealDetail(this,"")
//                    Utility.setMealPhotoContext(this,"")
//                    finish()
//                }
//                return true
//            }
//            else ->{
//                return super.onOptionsItemSelected(item)
//            }
//        }
//    }
}
