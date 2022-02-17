package com.gdscandroid.loginproject.Donator

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.gdscandroid.loginproject.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_donator_items.*


class DonatorItemsFragment : Fragment() {

    val dbRef= FirebaseDatabase.getInstance().reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MSG","Here is your on Create")


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d("MSG","Here is your on CreateView")
        return inflater.inflate(R.layout.fragment_donator_items, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val donorData=ArrayList<DonorData>()
        val donorRVAdapter=DonorRVAdapter(donorData)
        rvDonor.layoutManager= LinearLayoutManager(activity)
        rvDonor.adapter=donorRVAdapter

        val postListener= object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val post: DonorData?=snapshot.getValue(DonorData::class.java)
                if(post!=null){
                    donorData.add(post)
                }
                donorRVAdapter.notifyDataSetChanged()
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
        dbRef.child("DonatorPost").addChildEventListener(postListener)

        floatingActionButton.setOnClickListener{
           val intent = Intent (activity,DonatorApply::class.java)
            startActivity(intent)
        }
    }

}