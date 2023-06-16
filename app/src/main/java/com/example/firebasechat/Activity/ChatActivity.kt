package com.example.firebasechat.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.firebasechat.Adapter.chatAdapter
import com.example.firebasechat.Model.Chat
import com.example.firebasechat.Model.NotificationData
import com.example.firebasechat.Model.PushNotification
import com.example.firebasechat.Model.user
import com.example.firebasechat.R
import com.example.firebasechat.RetrofitInstance
import com.example.firebasechat.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    var firebaseUser: FirebaseUser? = null
    var reference: DatabaseReference? = null
    var chatList = ArrayList<Chat>()
    var topic = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityChatBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.imgBack3.setOnClickListener {
            onBackPressed()
        }

        binding.chatRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayout.VERTICAL, false)


        var intent = getIntent()
        var userId = intent.getStringExtra("userId")
        var userName = intent.getStringExtra("userName")

        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().getReference("User").child(userId!!)

        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(user::class.java)
                binding.tvUserName.text = user!!.userName
                if (user.profileImage == "") {
                    binding.imgProfile2.setImageResource(R.drawable.images)
                } else {
                    Glide.with(this@ChatActivity).load(user.profileImage).into(binding.imgProfile2)
                }
            }
        })

        binding.btnSendMessage.setOnClickListener {
            var message: String = binding.etMessage.text.toString()

            if (message.isEmpty()) {
                Toast.makeText(applicationContext, "message is empty", Toast.LENGTH_SHORT).show()
                binding.etMessage.setText("")
            } else {
                sendMessage(firebaseUser!!.uid, userId, message)
                binding.etMessage.setText("")
                topic = "/topics/$userId"
                PushNotification(
                    NotificationData(userName!!, message),
                    topic
                ).also {
                    sendNotification(it)
                }

            }
            readMessage(firebaseUser!!.uid, userId)
        }
    }

        private fun sendMessage(senderId: String, receiverId: String, message: String) {
            var reference: DatabaseReference? = FirebaseDatabase.getInstance().getReference()

            var hashMap: HashMap<String, String> = HashMap()
            hashMap.put("senderId", senderId)
            hashMap.put("receiverId", receiverId)
            hashMap.put("message", message)

            reference!!.child("Chat").push().setValue(hashMap)

        }

        fun readMessage(senderId: String, receiverId: String) {
            val databaseReference: DatabaseReference =
                FirebaseDatabase.getInstance().getReference("Chat")

            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    chatList.clear()
                    for (dataSnapShot: DataSnapshot in snapshot.children) {
                        val chat = dataSnapShot.getValue(Chat::class.java)

                        if (chat!!.senderId.equals(senderId) && chat!!.receiverId.equals(receiverId) ||
                            chat!!.senderId.equals(receiverId) && chat!!.receiverId.equals(senderId)
                        ) {
                            chatList.add(chat)
                        }
                    }

                    val chatAdapter = chatAdapter(this@ChatActivity, chatList)

                    binding.chatRecyclerView.adapter = chatAdapter
                }
            })
        }
    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful) {
                Log.d("TAG", "Response: ${Gson().toJson(response)}")
            } else {
                Log.e("TAG", response.errorBody()!!.string())
            }
        } catch(e: Exception) {
            Log.e("TAG", e.toString())
        }
    }



    }