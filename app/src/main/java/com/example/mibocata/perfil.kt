package com.example.mibocata

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class perfil : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        var btn_historial = findViewById<ImageView>(R.id.btn_historial)
        var btn_calendario = findViewById<ImageView>(R.id.btn_calendario)
        var btn_menu_principal = findViewById<ImageView>(R.id.btn_menu_principal)

        btn_historial.setOnClickListener{
            val intent = Intent(this, HistorialBocadillos::class.java)
            startActivity(intent)
            finish()
        }

        btn_calendario.setOnClickListener{
            val intent = Intent(this, ProgramacionSemanal::class.java)
            startActivity(intent)
            finish()
        }

        btn_menu_principal.setOnClickListener {
            val intent = Intent(this, ElegirBocadillo::class.java)
            startActivity(intent)
            finish()
        }




    }
}