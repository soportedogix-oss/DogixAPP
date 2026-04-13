package com.dogix.app

data class Producto(
    val id: Int,   // 🔥 CAMBIO CLAVE
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val imagen: String,
    val stock: Int
)