package com.example.firebasechat.Model

data class Chat(var senderId:String = "",
                var receiverId:String = "",
                var message:String = "")