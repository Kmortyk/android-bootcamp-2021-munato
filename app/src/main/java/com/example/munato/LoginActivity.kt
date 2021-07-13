package com.example.munato

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.munato.databinding.ActivityHomeBinding
import com.example.munato.databinding.ActivityLoginBinding

/*
*   Необходимо использовать многопоточность
    Необходимо использовать (БД + любую стороннюю библиотеку) ИЛИ сервисы Firebase
    Приложение выполняет только заявленные функции, безопасно и не содержит вредоносного кода.
*  */

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogIn.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}