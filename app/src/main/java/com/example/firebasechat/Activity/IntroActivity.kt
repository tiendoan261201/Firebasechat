package com.example.firebasechat.Activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasechat.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {

   private lateinit var binding: ActivityIntroBinding
   override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       binding = ActivityIntroBinding.inflate(layoutInflater)
       setContentView(binding.root)


       binding.btnGo.setOnClickListener {
           val i = Intent(this@IntroActivity, LoginActivity::class.java)
           startActivity(i)
           finish()
       }
   }
}