package com.gdscandroid.loginproject.Donator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.gdscandroid.loginproject.MainActivity
import com.gdscandroid.loginproject.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_donator_home.*

class DonatorHome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donator_home)

        val homeFragment= DonatorHomeFragment()
        val profileFragment= DonatorProfileFragment()
        val thingsFragment= DonatorItemsFragment()

        setCurrentFragment(homeFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home->setCurrentFragment(homeFragment)
                R.id.profile->setCurrentFragment(profileFragment)
                R.id.things->setCurrentFragment(thingsFragment)

            }
            true
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
