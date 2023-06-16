package com.example.firebasechat.Activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.firebasechat.Adapter.userAdapter
import com.example.firebasechat.FireBase
import com.example.firebasechat.Model.user
import com.example.firebasechat.R
import com.example.firebasechat.databinding.ActivityUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId

class UserActivity : AppCompatActivity() {
    var userList = ArrayList<user>()
    private lateinit var binding: ActivityUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUserBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        FireBase.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            FireBase.token = it.token
        }


        binding.userRcv.layoutManager = LinearLayoutManager(this)

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        binding.imgProfile.setOnClickListener{
            val intent =Intent(this@UserActivity,
            ProfileActivity::class.java
                )
            startActivity(intent)

        }

        getUserList()

    }
    fun getUserList(){
        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("User")

        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext,error.message,Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                val currentUser = snapshot.getValue(user::class.java)
                if (currentUser!!.profileImage == ""){
                    binding.imgProfile.setImageResource(R.drawable.images)
                }else{
                    Glide.with(this@UserActivity).load(currentUser.profileImage).into(binding.imgProfile)
                }

                for(dataSnapShot: DataSnapshot in snapshot.children){
                    val user = dataSnapShot.getValue(user::class.java)

                    if (!user!!.userId.equals(firebase.uid)){
                        userList.add(user)
                    }
                }
                var userAdapter = userAdapter(this@UserActivity,userList)
                binding.userRcv.adapter = userAdapter
            }
        })
    }
}