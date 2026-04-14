package com.dogix.app

data class Venta(
    val id_venta: Int,
    val id_usuario: Int,
    val id_producto: Int,
    val cantidad: Int,
    val total: Double,
    val fecha: String,
    val nombreProducto: String, // 🔥 CLAVE
    val imagen: String          // 🔥 EXTRA (para luego mostrar imagen)
)