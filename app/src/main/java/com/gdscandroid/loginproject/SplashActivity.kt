package com.gdscandroid.loginproject

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat

class SplashActivity : AppCompatActivity() {

//    var textView: TyperTextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

//        textView = findViewById<View>(R.id.linetext) as TyperTextView
//        textView.animateText("Feed The Need")
//        textView.setTypeface(ResourcesCompat.getFont(this, R.font.helveticaneue))
//
//
//        val linearLayout = findViewById<LinearLayout>(R.id.linearlayout)
//        val animationDrawable = linearLayout.background as AnimationDrawable
//        animationDrawable.setEnterFadeDuration(1300)
//        animationDrawable.setExitFadeDuration(1300)
//        animationDrawable.start()
//
//        Handler().postDelayed({
//            val intent = Intent(this@MainActivity, camera_permission::class.java)
//            startActivity(intent)
//            finish()
//        }, 2600)

    }
}