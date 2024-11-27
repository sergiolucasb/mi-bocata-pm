package com.example.mibocata

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        var btn_login = findViewById<Button>(R.id.btn_login)

        btn_login.setOnClickListener {
            var usuario = findViewById<EditText>(R.id.usuario).text.toString()
            var password = findViewById<EditText>(R.id.password).text.toString()

            if (usuario == "admin" && password == "1234") {
                val intent = Intent(this, ElegirBocadillo::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
            }
        }
        
    }
}