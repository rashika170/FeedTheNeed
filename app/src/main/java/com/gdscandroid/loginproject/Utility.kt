package com.gdscandroid.loginproject

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

object Utility {
    public fun setName(activity:Activity,name:String){
        val sharedPreferences = activity.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("Name",name)
        }.apply()
    }

    public fun getName(activity:Activity) : String? {
        val sharedPreferences = activity.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val ans = sharedPreferences.getString("Name",null)
        return ans
    }

    public fun setUid(activity:Activity,name:String){
        val sharedPreferences = activity.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("Uid",name)
        }.apply()
    }

    public fun getUid(activity:Activity) : String? {
        val sharedPreferences = activity.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val ans = sharedPreferences.getString("Uid",null)
        return ans
    }

    public fun setMobile(activity:Activity,name:String){
        val sharedPreferences = activity.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("Mobile",name)
        }.apply()
    }

    public fun getMobile(activity:Activity) : String? {
        val sharedPreferences = activity.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val ans = sharedPreferences.getString("Mobile",null)
        return ans
    }

    public fun setLocation(activity:Activity,name:String){
        val sharedPreferences = activity.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("Location",name)
        }.apply()
    }

    public fun getLocation(activity:Activity) : String? {
        val sharedPreferences = activity.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val ans = sharedPreferences.getString("Location",null)
        return ans
    }

    public fun setProfile(activity:Activity,name:String){
        val sharedPreferences = activity.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("Profile",name)
        }.apply()
    }

    public fun getProfile(activity:Activity) : String? {
        val sharedPreferences = activity.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val ans = sharedPreferences.getString("Profile",null)
        return ans
    }

    public fun setRole(activity:Activity,name:String){
        val sharedPreferences = activity.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("role",name)
        }.apply()
    }

    public fun getrole(activity:Activity) : String? {
        val sharedPreferences = activity.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val ans = sharedPreferences.getString("role",null)
        return ans
    }

    public fun setRewardoint(activity:Activity,name:Long){
        val sharedPreferences = activity.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putLong("reward",name)
        }.apply()
    }

    public fun getRewardoint(activity:Activity) : Long? {
        val sharedPreferences = activity.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val ans = sharedPreferences.getLong("reward",0)
        return ans
    }

    public fun setDonationPoint(activity:Activity,name:Long){
        val sharedPreferences = activity.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putLong("donation",name)
        }.apply()
    }

    public fun getDonationPoint(activity:Activity) : Long? {
        val sharedPreferences = activity.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val ans = sharedPreferences.getLong("donation",0)
        return ans
    }

}