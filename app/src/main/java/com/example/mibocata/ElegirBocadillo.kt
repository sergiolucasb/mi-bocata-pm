package com.example.mibocata

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ElegirBocadillo : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.elegir_bocadillo)

        var btn_historial = findViewById<ImageView>(R.id.btn_historial)
        var btn_calendario = findViewById<ImageView>(R.id.btn_calendario)
        var btn_perfil = findViewById<ImageView>(R.id.btn_perfil)

        var bocadillo_frio = findViewById<View>(R.id.bocadillo_frio)
        var bocadillo_caliente = findViewById<View>(R.id.bocadillo_caliente)

        //NOMBRES
        var nombre_frio = findViewById<TextView>(R.id.nombre_frio)
        var nombre_caliente = findViewById<TextView>(R.id.nombre_caliente)

        //INGREDIENTES
        var ingredientes_frio = findViewById<TextView>(R.id.ingredientes_frio)
        var ingredientes_caliente = findViewById<TextView>(R.id.ingredientes_caliente)

        var programacion_bocadillos = loadBocadillosHoy(this)

        programacion_bocadillos.forEach { bocadillo ->
            var fecha_bocata_parseada = LocalDate.parse(bocadillo.fecha, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
            if (fecha_bocata_parseada.dayOfMonth == LocalDate.now().dayOfMonth && fecha_bocata_parseada.month == LocalDate.now().month && fecha_bocata_parseada.year == LocalDate.now().year) {
                if (bocadillo.frio) {
                    nombre_frio.text = bocadillo.bocadillo
                    ingredientes_frio.text = bocadillo.ingredientes
                } else if (!bocadillo.frio) {
                    nombre_caliente.text = bocadillo.bocadillo
                    ingredientes_caliente.text = bocadillo.ingredientes
                }
            }
        }

        //FUNCIONALIDAD BARRA INFERIOR
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

        btn_perfil.setOnClickListener{
            val intent = Intent(this, perfil::class.java)
            startActivity(intent)
            finish()
        }


        bocadillo_frio.setOnClickListener{
            val cold:String = findViewById<TextView>(R.id.nombre_frio).text.toString()
            programacion_bocadillos.forEach{ bocadillo ->
                if (cold == bocadillo.bocadillo) {
                    Toast.makeText(this, "Alérgenos: " + bocadillo.alergenos, Toast.LENGTH_SHORT).show()
                }
            }
            guardarJson(this, cold, "Atún, aceite, tomate, pan", "pescados, gluten, lácteos",true, 1.40);
        }

        bocadillo_caliente.setOnClickListener{
            val hot:String = findViewById<TextView>(R.id.nombre_caliente).text.toString()
            programacion_bocadillos.forEach{ bocadillo ->
                if (hot == bocadillo.bocadillo) {
                    Toast.makeText(this, "Alérgenos: " + bocadillo.alergenos, Toast.LENGTH_SHORT).show()
                }
            }
            guardarJson(this, hot, "Jamón, pan, aceite", "gluten, sulfitos",false, 2.50);
        }

    }



    fun guardarJson(context: Context, nombreBocadillo : String, ingredientes : String, alergenos : String, frio : Boolean, precio : Double) {
        // Crea una instancia de Gson
        val gson = Gson()

        print("------------>>>>>>"+nombreBocadillo)
        //Toast.makeText(this, nombreBocadillo, Toast.LENGTH_LONG).show()
        //Lista de pedidos para añadir el pedido
        var listaPedido : MutableList<Pedido> = loadProducts(context)

        //Recoger último pedido para comprobar si la fecha es de hoy
        var ultimoPedido = listaPedido.last();

        //Obtener la fecha y parsearla para poder compararla
        val fechaPedido = ultimoPedido.fecha;
        val fechaParseada = LocalDate.parse(fechaPedido, DateTimeFormatter.ofPattern("dd-MM-yyyy"))

        //Si el pedido es de hoy, se modificará
        if (fechaParseada.dayOfMonth == LocalDate.now().dayOfMonth && fechaParseada.month == LocalDate.now().month && fechaParseada.year == LocalDate.now().year) {
            ultimoPedido.bocadillo = nombreBocadillo;
        } else {
            listaPedido.add(Pedido(
                LocalDate.now().toString(),
                nombreBocadillo,
                ingredientes,
                alergenos,
                frio,
                precio
            ))
        }

        val archivo_nombre = "pedidos.json"
        val file = File(context.filesDir, archivo_nombre)

        file.writer().use { writer ->
            gson.toJson(listaPedido, writer)
        }
    }

    private fun loadProducts(context : Context): MutableList<Pedido> {

        val archivoNombre = "pedidos.json"
        val archivo = File(context.filesDir, archivoNombre)

        if (!archivo.exists()) {
            context.resources.openRawResource(R.raw.pedidos).use { inputStream ->
                FileOutputStream(archivo).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        }

        // Lee el archivo JSON desde res/raw
        val inputStream = resources.openRawResource(R.raw.pedidos)
        val reader = InputStreamReader(inputStream)

        //DESERIALIZAR
        //TypeToken indica cómo formatear los valores
        // TypeToken define el tipo complejo List<Product>
        val type = object : TypeToken<List<Pedido>>() {}.type

        //Gson convierte un JSON a objetos (en este caso una lista de objetos)
        val pedidos: MutableList<Pedido> = Gson().fromJson(reader, type)

        return pedidos
    }

    private fun loadBocadillosHoy(context : Context): MutableList<Pedido> {

        // Lee el archivo JSON desde res/raw
        val inputStream = resources.openRawResource(R.raw.programacion_bocadillos)
        val reader = InputStreamReader(inputStream)

        //DESERIALIZAR
        //TypeToken indica cómo formatear los valores
        // TypeToken define el tipo complejo List<Product>
        val type = object : TypeToken<List<Pedido>>() {}.type

        //Gson convierte un JSON a objetos (en este caso una lista de objetos)
        val programacion_bocadillos: MutableList<Pedido> = Gson().fromJson(reader, type)

        return programacion_bocadillos
    }
}