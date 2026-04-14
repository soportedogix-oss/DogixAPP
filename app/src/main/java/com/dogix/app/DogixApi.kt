package com.dogix.app

import retrofit2.Call
import retrofit2.http.*

interface DogixApi {

    @POST("usuarios/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("productos")
    fun listarProductos(): Call<List<Producto>>

    @GET("productos/{id}")
    fun obtenerProducto(@Path("id") id: Int): Call<Producto>

    @POST("carrito")
    fun agregarCarrito(@Body request: CarritoRequest): Call<MensajeResponse>

    @GET("carrito/{idUsuario}")
    fun obtenerCarrito(@Path("idUsuario") id: Int): Call<List<Carrito>>

    @PUT("carrito")
    fun actualizarCarrito(@Body request: ActualizarCantidadRequest): Call<MensajeResponse>

    @DELETE("carrito/{idCarrito}")
    fun eliminarCarrito(@Path("idCarrito") id: Int): Call<MensajeResponse>

    @DELETE("carrito/usuario/{idUsuario}")
    fun limpiarCarrito(@Path("idUsuario") id: Int): Call<MensajeResponse>

    @POST("ventas/checkout/{idUsuario}")
    fun checkout(@Path("idUsuario") idUsuario: Int): Call<MensajeResponse>

    @GET("ventas/{idUsuario}")
    fun obtenerVentas(@Path("idUsuario") id: Int): Call<List<Venta>>
}