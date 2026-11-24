package com.bignerdranch.android.vysotskiy_zd3_1

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.app.AlertDialog

lateinit var email:EditText
lateinit var password:EditText

class SignInActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
    }

    fun Login(view: View){
        if(email.text.toString().isNotEmpty() && password.text.toString().isNotEmpty())
        {
            val intent = Intent(this,QuestsActivity::class.java)
            startActivity(intent)
        }
        else{
            val alert = AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("У вас пустые поля")
                .setPositiveButton("Ок",null)
                .create()
                .show()
        }
    }
}