package com.gdscandroid.feedtheneed.Donator

data class DonorData(val image:String?=null, val desc:String?=null,
                     val location:String?=null, val uid:String?=null,
                     val time:String?=null,
                     val status:String?=null, val pickedBy:String?=null,
                     val pickedTime:String?=null, val verificationLink:String?=null,
                     val latitude:String?=null, val longitude:String?=null,
                     var bookId:String?=null, var name:String?=null,
                     var volPic:String?=null, var volPhoneNumber:String?=null,
                     var donatorPic:String?=null, var donatorPhone:String?=null,
                     val volUid:String?=null,val donatorName:String?=null)
