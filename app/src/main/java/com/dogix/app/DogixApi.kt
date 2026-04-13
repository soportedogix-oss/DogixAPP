package com.dogix.app

import retrofit2.Call
import retrofit2.http.*

interface DogixApi {

    // =========================
    // 🔐 USUARIO
    // =========================
    @POST("usuarios/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>


    // =========================
    // 📦 PRODUCTOS
    // =========================
    @GET("productos")
    fun listarProductos(): Call<List<Producto>>

    @GET("productos/{id}")
    fun obtenerProducto(@Path("id") id: Int): Call<Producto>


    // =========================
    // 🛒 CARRITO
    // =========================

    // 🔥 AGREGAR AL CARRITO
    @POST("carrito")
    fun agregarCarrito(@Body request: CarritoRequest): Call<MensajeResponse>

    // 🔥 OBTENER CARRITO
    @GET("carrito/{idUsuario}")
    fun obtenerCarrito(@Path("idUsuario") id: Int): Call<List<Carrito>>

    // 🔥 ACTUALIZAR CANTIDAD
    @PUT("carrito")
    fun actualizarCarrito(@Body request: ActualizarCantidadRequest): Call<MensajeResponse>

    // 🔥 ELIMINAR ITEM
    @DELETE("carrito/{idCarrito}")
    fun eliminarCarrito(@Path("idCarrito") id: Int): Call<MensajeResponse>

    // 🔥 LIMPIAR TODO EL CARRITO
    @DELETE("carrito/usuario/{idUsuario}")
    fun limpiarCarrito(@Path("idUsuario") id: Int): Call<MensajeResponse>


    // =========================
    // 💰 VENTAS / CHECKOUT
    // =========================
    @POST("ventas")
    fun registrarVenta(@Body request: VentaRequest): Call<MensajeResponse>

    @GET("ventas/{idUsuario}")
    fun obtenerVentas(@Path("idUsuario") id: Int): Call<List<Venta>>
}