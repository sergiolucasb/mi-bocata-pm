package com.example.mibocata

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class PedidoAdapter(context: Context, pedido: List<Pedido>) : ArrayAdapter<Pedido>(context, 0, pedido) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.pedido_item, parent, false)

        val pedido = getItem(position)

        val pedido_fecha = view.findViewById<TextView>(R.id.pedido_fecha)
        val pedido_bocadillo = view.findViewById<TextView>(R.id.pedido_bocadillo)

        pedido_fecha.text = pedido?.fecha.toString()
        pedido_bocadillo.text = pedido?.bocadillo

        if (pedido?.frio == true) {
            pedido_fecha.setTextColor(Color.parseColor("#00ff00"))
        } else if (pedido?.frio == false) {
            pedido_fecha.setTextColor(Color.parseColor("#ff0000"))
        }

        return view
    }
}