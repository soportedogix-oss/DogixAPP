package com.dogix.app

data class VentaRequest(
    val idUsuario: Int,
    val idProducto: Int,
    val cantidad: Int,
    val total: Double
)