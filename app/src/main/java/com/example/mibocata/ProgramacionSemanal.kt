package com.example.mibocata

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.GridView
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.io.InputStreamReader
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ProgramacionSemanal : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_programacion_semanal)

        var btn_historial = findViewById<ImageView>(R.id.btn_historial)
        var btn_perfil = findViewById<ImageView>(R.id.btn_perfil)
        var btn_menu_principal = findViewById<ImageView>(R.id.btn_menu_principal)

        var nombre_frio = findViewById<TextView>(R.id.nombre_frio)
        var nombre_caliente = findViewById<TextView>(R.id.nombre_caliente)

        btn_historial.setOnClickListener{
            val intent = Intent(this, HistorialBocadillos::class.java)
            startActivity(intent)
            finish()
        }

        btn_perfil.setOnClickListener{
            val intent = Intent(this, perfil::class.java)
            startActivity(intent)
            finish()
        }

        btn_menu_principal.setOnClickListener {
            val intent = Intent(this, ElegirBocadillo::class.java)
            startActivity(intent)
            finish()
        }

        val bocadillos = loadBocadillos(this)


        bocadillos.forEach { bocadillo ->
            var fecha_bocata_parseada = LocalDate.parse(bocadillo.fecha, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
            val fecha_manana = LocalDate.now().plusDays(1)
            if (fecha_bocata_parseada.dayOfMonth == fecha_manana.dayOfMonth && fecha_bocata_parseada.month == fecha_manana.month && fecha_bocata_parseada.year == fecha_manana.year) {
                if (bocadillo.frio) {
                    nombre_frio.text = bocadillo.bocadillo
                } else if (!bocadillo.frio) {
                    nombre_caliente.text = bocadillo.bocadillo
                }
            }
        }


        val gridViewBocadillos = findViewById<GridView>(R.id.gridViewBocadillos)
        val adapter = PedidoAdapter(this, bocadillos)
        gridViewBocadillos.adapter = adapter

    }

    fun loadBocadillos(context : Context) : List<Pedido>{
        // Lee el archivo JSON desde res/raw
        val inputStream = resources.openRawResource(R.raw.programacion_bocadillos)
        val reader = InputStreamReader(inputStream)

        //DESERIALIZAR

        //TypeToken indica cómo formatear los valores
        // TypeToken define el tipo complejo List<Product>
        val type = object : TypeToken<List<Pedido>>() {}.type

        //Gson convierte un JSON a objetos (en este caso una lista de objetos)
        val pedidos: List<Pedido> = Gson().fromJson(reader, type)

        return pedidos
    }
}