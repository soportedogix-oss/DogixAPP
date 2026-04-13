package com.dogix.app

data class CarritoRequest(
    val idUsuario: Int,
    val idProducto: Int,
    val cantidad: Int = 1
)