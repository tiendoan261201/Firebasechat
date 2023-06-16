package com.example.firebasechat.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebasechat.Activity.ChatActivity
import com.example.firebasechat.Model.user
import com.example.firebasechat.R
import de.hdodenhof.circleimageview.CircleImageView

class userAdapter (private val context: Context, private val userList: ArrayList<user>):
RecyclerView.Adapter<userAdapter.ViewHolder>(){


    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val userNames:TextView = view.findViewById(R.id.txtUserName)
        val temp: TextView = view.findViewById(R.id.txtTemp)
        val imgUser :CircleImageView = view.findViewById(R.id.userImage)
        val layoutUser: LinearLayout = view.findViewById(R.id.layoutUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user,parent,false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val user = userList[position]
        holder.userNames.text = user.userName
        Glide.with(context).load(user.profileImage).placeholder(R.drawable.images).into(holder.imgUser)

        holder.layoutUser.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("userId",user.userId)
            intent.putExtra("userName",user.userName)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
         return userList.size
    }
}