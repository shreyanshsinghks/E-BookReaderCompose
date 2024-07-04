package com.hello.ebookreader.data.network

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import javax.inject.Inject

//class GetAllCategories @Inject constructor(private val firebaseDatabase: FirebaseDatabase) {
//
//    fun getAllCategories() {
//        val categoryListener = object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                val category = dataSnapshot.getValue<CategoryDTO>()
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//
//            }
//        }
//        firebaseDatabase.reference.child("BookCategory").addValueEventListener(categoryListener)
//    }
//
//
//}