package com.gdscandroid.loginproject.Donator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.gdscandroid.loginproject.MainActivity
import com.gdscandroid.loginproject.R
import com.gdscandroid.loginproject.Utility
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class DonatorHome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donator_home)

        Firebase.auth.currentUser?.let { Log.d("kahokaho", it.uid) }

        val homeFragment= DonatorHomeFragment()
        val profileFragment= DonatorProfileFragment()
        val thingsFragment= DonatorItemsFragment()

        setCurrentFragment(homeFragment)


        val feed_frag = findViewById<ImageView>(R.id.feed_frag)
        val item_frag = findViewById<ImageView>(R.id.items_frag)
        val apply_frag = findViewById<Button>(R.id.apply_frag)

        feed_frag.setOnClickListener {
            feed_frag.setImageDrawable(getDrawable(R.drawable.feed_selected))
            item_frag.setImageDrawable(getDrawable(R.drawable.verify))
            setCurrentFragment(homeFragment)
        }

        item_frag.setOnClickListener {
            item_frag.setImageDrawable(getDrawable(R.drawable.verify_selected))
            feed_frag.setImageDrawable(getDrawable(R.drawable.feed))
            setCurrentFragment(thingsFragment)
        }

        apply_frag.setOnClickListener {
            item_frag.setImageDrawable(getDrawable(R.drawable.verify))
            feed_frag.setImageDrawable(getDrawable(R.drawable.feed))
            intent = Intent(this,DonatorApply::class.java)
            startActivity(intent)
        }

    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.home_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.logout ->{
                lateinit var mGoogleSignInClient: GoogleSignInClient
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id1))
                    .requestEmail()
                    .build()
                mGoogleSignInClient= GoogleSignIn.getClient(this,gso)
                mGoogleSignInClient.signOut().addOnCompleteListener {
                    Log.d("Homeactivit","logout");
                    Firebase.auth.signOut()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    Utility.setName(this,"")
                    Utility.setLocation(this,"")
                    Utility.setProfile(this,"")
                    Utility.setMobile(this,"")
                    Utility.setUid(this,"")
                    Utility.setRole(this,"")
                    Utility.setRewardoint(this,0)
                    Utility.setDonationPoint(this,0)
                    Utility.setProfileComplete(this,false)
                    Utility.setMealDetail(this,"")
                    Utility.setMealPhotoContext(this,"")
                    finish()
                }
                return true
            }
            else ->{
                return super.onOptionsItemSelected(item)
            }
        }
    }
}
