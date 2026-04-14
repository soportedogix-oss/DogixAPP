package com.dogix.app

data class Carrito(
    val idCarrito: Int,
    val idProducto: Int,
    var cantidad: Int,
    val nombreProducto: String,
    val imagen: String,
    val precio: Double,
    val stock: Int
)