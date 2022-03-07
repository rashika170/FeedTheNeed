package com.gdscandroid.loginproject.Volunteer

data class EduAdvertiseData(val Leadname:String?=null, val Phone:String?=null,
                            val Info:String?=null, val LeftVolunteers:String?=null,
                            val time:String?=null, val Location:String?=null,
                            val Status:String?=null,val Latitude:String?=null,
                            val Longitude:String?=null,val VolunteerUid:String?=null,
                            val QuestId:String?=null,val Comment:HashMap<String,HashMap<String,String>>?=null,
                            val LeadPic:String?=null
)
