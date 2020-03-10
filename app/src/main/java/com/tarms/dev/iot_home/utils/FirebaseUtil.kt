package com.tarms.dev.iot_home.utils

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tarms.dev.iot_home.data.Firm

object FirebaseUtil {
    private val databaseReference =
        FirebaseDatabase.getInstance().reference.child(Utils.firmRef("mazharul_sabbir"))

    fun getAllData(onDataChange: (List<Firm>) -> Unit) {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                println(p0.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val firmList = mutableListOf<Firm>()

                snapshot.children.forEach {
                    it.getValue<Firm>(Firm::class.java)?.let { it1 -> firmList.add(it1) }
                }

                onDataChange(firmList)
            }
        })
    }

    fun getLatestData(onDataChange: (Firm, String) -> Unit) {
        val query = databaseReference.orderByKey().limitToLast(1)
        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                println(p0.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.map { dataSnapshot ->
                    dataSnapshot.getValue<Firm>(Firm::class.java)?.let {
                        onDataChange(
                            it, dataSnapshot.key.toString()
                        )
                    }
                }
            }
        })
    }
}