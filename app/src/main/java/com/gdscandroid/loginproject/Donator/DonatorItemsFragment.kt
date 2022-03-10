package com.gdscandroid.loginproject.Donator

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.get
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.gdscandroid.loginproject.R
import com.gdscandroid.loginproject.Utility
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.snapshot.Index
import kotlinx.android.synthetic.main.activity_donator_apply.*
import kotlinx.android.synthetic.main.fragment_donator_home.*
import kotlinx.android.synthetic.main.fragment_donator_items.*
import java.nio.file.Files.delete


class DonatorItemsFragment : Fragment() {


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

    private fun setCurrentFragment(fragment: Fragment) =
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.flFragment,fragment)
            commit()
        }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        profile_nav1.setOnClickListener {
            setCurrentFragment(DonatorProfileFragment())
        }

        Glide.with(requireContext()).load(activity?.let { Utility.getProfile(it).toString() }).into(profile_nav1)

        val dbRef= FirebaseDatabase.getInstance().reference
        val donorData=ArrayList<DonorData>()
        var donorRVAdapter=DonorRVAdapter(donorData)

        rvDonor.layoutManager= LinearLayoutManager(activity)

        rvDonor.adapter=donorRVAdapter

        val postListener= object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val post: DonorData?=snapshot.getValue(DonorData::class.java)
                if(post!=null){
                    donorData.add(0,post)
                }

                donorRVAdapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val post: DonorData?=snapshot.getValue(DonorData::class.java)

                for(i in 0..donorData.size-1){
                    if(donorData[i].bookId.toString().equals(post!!.bookId.toString())){
                        donorData.set(i,post)
                    }
                }

                donorRVAdapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }

        }

        dbRef.child("DonatorPost").addChildEventListener(postListener)
        Log.d("raatmekaam",donorData.size.toString())


//        floatingActionButton.setOnClickListener{
//           val intent = Intent (activity,DonatorApply::class.java)
//            startActivity(intent)
//        }
    }

    private fun performOptionMenu(position: Int) {


    }


}