package com.example.mibocata

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.io.InputStreamReader
import java.text.DecimalFormat

class HistorialBocadillos : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial_bocadillos)

        var btn_perfil = findViewById<ImageView>(R.id.btn_perfil)
        var btn_calendario = findViewById<ImageView>(R.id.btn_calendario)
        var btn_menu_principal = findViewById<ImageView>(R.id.btn_menu_principal)

        var total_frios = findViewById<TextView>(R.id.total_frios)
        var total_calientes = findViewById<TextView>(R.id.total_calientes)
        var dinero = findViewById<TextView>(R.id.dinero)
        val formato = DecimalFormat("#.##")

        //FUNCIONES DE BOTONES MENÚ
        btn_perfil.setOnClickListener {
            val intent = Intent(this, perfil::class.java)
            startActivity(intent)
            finish()
        }

        btn_calendario.setOnClickListener {
            val intent = Intent(this, ProgramacionSemanal::class.java)
            startActivity(intent)
            finish()
        }

        btn_menu_principal.setOnClickListener{
            val intent = Intent(this, ElegirBocadillo::class.java)
            startActivity(intent)
            finish()
        }

        //RELLENAR DATOS DEL LISTVIEW

        val pedidos = loadProducts(this)
        val listViewPedidos = findViewById<ListView>(R.id.listViewHistorial)
        val adapter = PedidoAdapter(this, pedidos)
        listViewPedidos.adapter = adapter

        //CONTAR BOCADILLOS FRÍOS Y CALIENTES
        var contador_frios = 0
        var contador_calientes = 0
        var contador_dinero = 0.00
        pedidos.forEach{ pedido ->
            if (pedido.frio) {
                contador_frios++
            } else if(!pedido.frio) {
                contador_calientes++
            }
            contador_dinero += pedido.precio
        }

        total_frios.text = contador_frios.toString()
        total_calientes.text = contador_calientes.toString()
        dinero.text = formato.format(contador_dinero).toDouble().toString()

    }

    private fun loadProducts(context : Context): List<Pedido> {
        // Lee el archivo JSON desde res/raw
        val inputStream = resources.openRawResource(R.raw.pedidos)
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
