package com.dogix.app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.dogix.app.databinding.ActivityCarritoBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CarritoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCarritoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.statusBars())
        controller.hide(WindowInsetsCompat.Type.navigationBars())

        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        binding = ActivityCarritoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerCarrito.layoutManager = LinearLayoutManager(this)

        verificarSesionYCargar()

        binding.btnComprar.setOnClickListener {
            comprar()
        }
    }

    override fun onResume() {
        super.onResume()
        cargarCarrito()
    }

    private fun verificarSesionYCargar() {

        val prefs = getSharedPreferences("session", MODE_PRIVATE)
        val idUsuario = prefs.getInt("id_usuario", 0)

        if (idUsuario == 0) {
            Toast.makeText(this, "Debes iniciar sesión", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            cargarCarrito()
        }
    }

    private fun cargarCarrito() {

        val prefs = getSharedPreferences("session", MODE_PRIVATE)
        val idUsuario = prefs.getInt("id_usuario", 0)

        RetrofitClient.instance.obtenerCarrito(idUsuario)
            .enqueue(object : Callback<List<Carrito>> {

                override fun onResponse(
                    call: Call<List<Carrito>>,
                    response: Response<List<Carrito>>
                ) {

                    if (response.isSuccessful && response.body() != null) {

                        val lista = response.body()!!.toMutableList()

                        binding.recyclerCarrito.adapter = CarritoAdapter(lista)

                    } else {
                        Toast.makeText(
                            this@CarritoActivity,
                            "Error ${response.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<Carrito>>, t: Throwable) {
                    Toast.makeText(
                        this@CarritoActivity,
                        "Error conexión: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }

    private fun comprar() {

        val prefs = getSharedPreferences("session", MODE_PRIVATE)
        val idUsuario = prefs.getInt("id_usuario", 0)

        RetrofitClient.instance.checkout(idUsuario)
            .enqueue(object : Callback<MensajeResponse> {

                override fun onResponse(
                    call: Call<MensajeResponse>,
                    response: Response<MensajeResponse>
                ) {
                    if (response.isSuccessful) {

                        Toast.makeText(
                            this@CarritoActivity,
                            "Compra realizada 🧾",
                            Toast.LENGTH_SHORT
                        ).show()

                        startActivity(Intent(this@CarritoActivity, VentasActivity::class.java))

                    } else {
                        Toast.makeText(
                            this@CarritoActivity,
                            "Error en compra",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<MensajeResponse>, t: Throwable) {
                    Toast.makeText(
                        this@CarritoActivity,
                        "Error conexión",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}