package com.gdscandroid.loginproject.Volunteer

data class VolunteerMealPostData(val BookingId:String?=null, val RestaurantPhone:String?=null,
                                 val Status:String?=null, val expectedPickTime:String?=null
                                 , val numOfMeals:String?=null, val restaurantName:String?=null,
                                 val RestaurantUid:String?=null, var VolunteerUid:String?=null,
                                 val RestaurPhoto:String?=null)