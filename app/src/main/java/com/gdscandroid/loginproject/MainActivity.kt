package com.gdscandroid.loginproject

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var login: Button
    private lateinit var google: TextView

    //Google sign in
    lateinit var mGoogleSignInClient: GoogleSignInClient
    val Req_Code: Int = 123
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
        email = findViewById(R.id.editTextTextPersonName);
        password = findViewById(R.id.editTextTextPersonName2)
        login = findViewById(R.id.button)
        google = findViewById(R.id.textView)

        initializeGoogle();

        login.setOnClickListener(){
            CustomLogin()
        }

        google.setOnClickListener {
            Toast.makeText(this, "Logging In", Toast.LENGTH_SHORT).show()
            signInGoogle()
        }

    }

    private fun initializeGoogle() {
        FirebaseApp.initializeApp(this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id1))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        firebaseAuth = FirebaseAuth.getInstance()
    }

    private fun signInGoogle() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, Req_Code)
    }

    // onActivityResult() function : this is where
    // we provide the task and data for the Google Account
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Req_Code) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                UpdateUI(account)
            }
        } catch (e: ApiException) {
            Log.d("Status123",e.toString())
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    // this is where we update the UI after Google signin takes place
    private fun UpdateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->

            if (task.isSuccessful) {
                val ref = Firebase.database.getReference("Users")
                var k=0
                ref.addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for(nextSnapshot:DataSnapshot in snapshot.children){
                            if(nextSnapshot.key.toString().equals(firebaseAuth.currentUser?.uid)){
                                Log.d("bhibhi","cfc")
                                k=1
                                Utility.setName(this@MainActivity,nextSnapshot.child("Name").value.toString())
                                Utility.setLocation(this@MainActivity,nextSnapshot.child("Location").value.toString())
                                Utility.setProfile(this@MainActivity,nextSnapshot.child("Profile").value.toString())
                                Utility.setMobile(this@MainActivity,nextSnapshot.child("Phone").value.toString())
                                Utility.setUid(this@MainActivity,nextSnapshot.child("Uid").value.toString())

                                intent = Intent(this@MainActivity, HomeActivity::class.java)
                                startActivity(intent);
                                finish()
                            }
                        }
                        if(k==0){
                            val gname : String = account.givenName.toString()
                            val gmail : String = account.email.toString()
                            val uid : String = firebaseAuth.currentUser?.uid ?: ""
                            Toast.makeText(this@MainActivity, gname+"  , "+gmail+"  , "+uid, Toast.LENGTH_LONG).show()
                            val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                            intent.putExtra("name", gname+" "+account.familyName.toString())
                            intent.putExtra("profile",account.photoUrl.toString());
                            intent.putExtra("uid", firebaseAuth.currentUser?.uid.toString())
                            intent.putExtra("source", "Google")
                            intent.putExtra("credential", credential)
                            startActivity(intent)
                            finish()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })

            }
        }
    }

    private fun CustomLogin(){
        val email = email.text.toString().trim()
        val pass = password.text.toString().trim()
        if(email.isBlank() || pass.isBlank()){
            Toast.makeText(baseContext,"Please Fill all Details",Toast.LENGTH_SHORT).show()
        }else{
            auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        auth.signInWithEmailAndPassword(email,pass)
                            .addOnCompleteListener(this){
                                if(it.isSuccessful){
                                    Toast.makeText(baseContext,"Successfully Logged in!!",Toast.LENGTH_SHORT).show()
                                    intent = Intent(this, ProfileActivity::class.java)
                                    intent.putExtra("name", "")
                                    intent.putExtra("profile","");
                                    intent.putExtra("uid", auth.currentUser?.uid.toString())
                                    intent.putExtra("source", "Custom")
                                    intent.putExtra("email", email)
                                    intent.putExtra("pass", pass)
                                    startActivity(intent);
                                    finish()
                                }else{
                                    Toast.makeText(baseContext,task.exception.toString(),Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        try{
                            throw task.exception!!
                        }catch (e: FirebaseAuthUserCollisionException){
                            auth.signInWithEmailAndPassword(email,pass)
                                .addOnCompleteListener {
                                    if(it.isSuccessful){
                                        var k=0
                                        val ref = Firebase.database.getReference("Users");
                                        ref.addListenerForSingleValueEvent(object : ValueEventListener{
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                for(nextSnapshot:DataSnapshot in snapshot.children){
                                                    Log.d("Status33",snapshot.key.toString())
                                                    if(nextSnapshot.key.toString().equals(auth.currentUser?.uid)){
                                                        k=1
                                                        Utility.setName(this@MainActivity,nextSnapshot.child("Name").value.toString())
                                                        Utility.setLocation(this@MainActivity,nextSnapshot.child("Location").value.toString())
                                                        Utility.setProfile(this@MainActivity,nextSnapshot.child("Profile").value.toString())
                                                        Utility.setMobile(this@MainActivity,nextSnapshot.child("Phone").value.toString())
                                                        Utility.setUid(this@MainActivity,nextSnapshot.child("Uid").value.toString())
                                                        Utility.setRole(this@MainActivity,nextSnapshot.child("Role").value.toString())
                                                        Utility.setRewardoint(this@MainActivity,
                                                            nextSnapshot.child("RewardPoints").value as Int
                                                        )
                                                        Utility.setDonationPoint(this@MainActivity,
                                                            nextSnapshot.child("DonationPoints").value as Int
                                                        )

                                                        intent = Intent(this@MainActivity, HomeActivity::class.java)
                                                        startActivity(intent);
                                                    }
                                                }
                                                if(k==0){
                                                    intent = Intent(this@MainActivity, ProfileActivity::class.java)
                                                    intent.putExtra("name", "")
                                                    intent.putExtra("profile","");
                                                    intent.putExtra("uid", auth.currentUser?.uid.toString())
                                                    intent.putExtra("source", "Custom")
                                                    intent.putExtra("email", email)
                                                    intent.putExtra("pass", pass)
                                                    startActivity(intent);
                                                    finish()
                                                    startActivity(intent);
                                                }
                                            }

                                            override fun onCancelled(error: DatabaseError) {

                                            }

                                        })
                                    }
                                }
                        }catch (e: Exception){
                            Log.w("Status123", task.exception.toString(), task.exception)
                            Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}