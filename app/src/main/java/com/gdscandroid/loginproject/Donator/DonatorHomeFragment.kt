package com.gdscandroid.loginproject.Donator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.gdscandroid.loginproject.R
import com.gdscandroid.loginproject.feeds.FeedData
import com.gdscandroid.loginproject.feeds.FeedRVAdapter
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_donator_home.*

class DonatorHomeFragment : Fragment() {

    val dbRef= FirebaseDatabase.getInstance().reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_donator_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val feedData=ArrayList<FeedData>()
        val feedRVAdapter= FeedRVAdapter(feedData)
        rvFeed.layoutManager= LinearLayoutManager(activity)
        rvFeed.adapter=feedRVAdapter

        val postListener= object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val post:FeedData?=snapshot.getValue(FeedData::class.java)
                if(post!=null){
                    feedData.add(post)
                }
                feedRVAdapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }

        }
        dbRef.child("feeds").addChildEventListener(postListener)

    }
}