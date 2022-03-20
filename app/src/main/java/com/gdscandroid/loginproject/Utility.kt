package com.gdscandroid.loginproject

import android.app.Activity
import android.content.Context

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

    public fun setUidContext(activity:Context,name:String){
        val sharedPreferences = activity.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("Uid",name)
        }.apply()
    }

    public fun getUidContext(activity:Context) : String? {
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

    public fun setMobileContext(activity:Context,name:String){
        val sharedPreferences = activity.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("Mobile",name)
        }.apply()
    }

    public fun getMobileContext(activity:Context) : String? {
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
        val ans = sharedPreferences.getString("Profile","")
        return ans
    }

    public fun setProfileContext(activity:Context,name:String){
        val sharedPreferences = activity.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("Profile",name)
        }.apply()
    }

    public fun getProfileContext(activity:Context) : String? {
        val sharedPreferences = activity.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val ans = sharedPreferences.getString("Profile","")
        return ans
    }

    public fun setLongitude(activity:Activity,name:String){
        val sharedPreferences = activity.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("Longitude",name)
        }.apply()
    }

    public fun getLongitude(activity:Activity) : String? {
        val sharedPreferences = activity.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val ans = sharedPreferences.getString("Longitude","")
        return ans
    }

    public fun setLatitude(activity:Activity,name:String){
        val sharedPreferences = activity.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("Latitude",name)
        }.apply()
    }

    public fun getLatitude(activity:Activity) : String? {
        val sharedPreferences = activity.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val ans = sharedPreferences.getString("Latitude","")
        return ans
    }

    public fun setLongitudeContext(activity:Context,name:String){
        val sharedPreferences = activity.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("Longitude",name)
        }.apply()
    }

    public fun getLongitudeContext(activity:Context) : String? {
        val sharedPreferences = activity.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val ans = sharedPreferences.getString("Longitude","")
        return ans
    }

    public fun setLatitudeContext(activity:Context,name:String){
        val sharedPreferences = activity.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("Latitude",name)
        }.apply()
    }

    public fun getLatitudeContext(activity:Context) : String? {
        val sharedPreferences = activity.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val ans = sharedPreferences.getString("Latitude","")
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

    public fun setDonationPointContext(activity:Context,name:Long){
        val sharedPreferences = activity.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putLong("donation",name)
        }.apply()
    }

    public fun getDonationPointContext(activity:Context) : Long? {
        val sharedPreferences = activity.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val ans = sharedPreferences.getLong("donation",0)
        return ans
    }

    public fun setProfileComplete(activity:Activity,name:Boolean){
        val sharedPreferences = activity.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putBoolean("profilecomplete",name)
        }.apply()
    }

    public fun getProfileComplete(activity:Activity) : Boolean {
        val sharedPreferences = activity.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val ans = sharedPreferences.getBoolean("profilecomplete",false)
        return ans
    }

    public fun setNameContext(activity:Context,name:String){
        val sharedPreferences = activity.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("Name",name)
        }.apply()
    }

    public fun getNameContext(activity:Context) : String? {
        val sharedPreferences = activity.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val ans = sharedPreferences.getString("Name",null)
        return ans
    }

    public fun setMealPhotoContext(activity:Context,name:String){
        val sharedPreferences = activity.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("MealPhoto",name)
        }.apply()
    }

    public fun getMealPhotoContext(activity:Context) : String? {
        val sharedPreferences = activity.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val ans = sharedPreferences.getString("MealPhoto",null)
        return ans
    }

    public fun setMealDetail(activity:Context,name:String){
        val sharedPreferences = activity.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("MealDetail",name)
        }.apply()
    }

    public fun getMealDetail(activity:Context) : String? {
        val sharedPreferences = activity.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val ans = sharedPreferences.getString("MealDetail",null)
        return ans
    }

    public fun setMealDetailActivity(activity:Activity,name:String){
        val sharedPreferences = activity.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("MealDetail",name)
        }.apply()
    }

    public fun getMealDetailActivity(activity:Activity) : String? {
        val sharedPreferences = activity.getSharedPreferences("shared",Context.MODE_PRIVATE)
        val ans = sharedPreferences.getString("MealDetail",null)
        return ans
    }

}