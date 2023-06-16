package com.example.firebasechat.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.firebasechat.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference

class LoginActivity : AppCompatActivity() {
    private var auth: FirebaseAuth? = null
    private  var firebaseUser: FirebaseUser? = null

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

            //check if user login then navigate to user screen
        if(firebaseUser != null) {
            val intent = Intent(
                this@LoginActivity,
                UserActivity::class.java
            )
            startActivity(intent)
            finish()
        }
        binding.btnSignup2.setOnClickListener {
            val intent = Intent(this@LoginActivity,
                SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin2.setOnClickListener {
            val emailLogin = binding.edtInName.text.toString()
            val passwordLogin = binding.edtInPw.text.toString()

            if(TextUtils.isEmpty(emailLogin) && TextUtils.isEmpty(passwordLogin)){
                Toast.makeText(applicationContext,"email account and password is required", Toast.LENGTH_SHORT).show()
            }else{
                auth!!.signInWithEmailAndPassword(emailLogin,passwordLogin)
                    .addOnCompleteListener(this) {
                        if(it.isSuccessful){
                            binding.edtInPw.setText("")
                            binding.edtInPw.setText("")
                            val intent = Intent(this@LoginActivity, UserActivity::class.java)
                            startActivity(intent)
                        }else{
                            Toast.makeText(applicationContext,"Please try again",Toast.LENGTH_SHORT).show()
                        }
                    }

            }
        }


    }
}