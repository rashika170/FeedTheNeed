package com.gdscandroid.loginproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {

    private lateinit var logout: Button
    private lateinit var name: TextView

    lateinit var mGoogleSignInClient: GoogleSignInClient

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        logout = findViewById(R.id.button2)
        name = findViewById(R.id.name_home)

        Firebase.auth.currentUser?.let { Log.d("Status33", it.uid) };

        name.text = "Hi "+Utility.getName(this)

        name.setOnClickListener {
            intent = Intent(this,DonatorApply::class.java)
            startActivity(intent)
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id1))
            .requestEmail()
            .build()
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso)
        Firebase.auth.currentUser?.let { Log.d("Homeactivit", it.uid) };
        logout.setOnClickListener {
            mGoogleSignInClient.signOut().addOnCompleteListener {
                Log.d("Homeactivit","logout");
                Firebase.auth.signOut()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

//            auth.signOut()
//            intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
        }

    }
}