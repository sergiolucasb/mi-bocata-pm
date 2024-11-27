package com.example.mibocata

data class Pedido(
    val fecha : String,
    var bocadillo : String,
    var ingredientes : String,
    var alergenos : String,
    val frio : Boolean,
    val precio : Double
)