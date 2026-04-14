package com.dogix.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.dogix.app.databinding.ActivityMainBinding
import retrofit2.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.statusBars())
        controller.hide(WindowInsetsCompat.Type.navigationBars())

        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerProductos.layoutManager = LinearLayoutManager(this)

        cargarProductos()

        binding.btnIrLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnCarrito.setOnClickListener {
            startActivity(Intent(this, CarritoActivity::class.java))
        }

        verificarSesion() // 🔥 CLAVE
    }

    override fun onResume() {
        super.onResume()
        verificarSesion() // 🔥 se actualiza al volver
    }

    private fun verificarSesion() {

        val prefs = getSharedPreferences("session", MODE_PRIVATE)
        val nombre = prefs.getString("nombre", null)

        if (nombre != null) {

            val inicial = nombre.substring(0, 1).uppercase()

            binding.txtUser.text = inicial
            binding.txtUser.visibility = View.VISIBLE
            binding.btnIrLogin.visibility = View.GONE

            binding.txtUser.setOnClickListener {
                mostrarMenuUsuario()
            }

        } else {
            binding.txtUser.visibility = View.GONE
            binding.btnIrLogin.visibility = View.VISIBLE
        }
    }

    private fun mostrarMenuUsuario() {

        val popup = PopupMenu(this, binding.txtUser)

        popup.menu.add("Cerrar sesión")

        popup.setOnMenuItemClickListener {

            if (it.title == "Cerrar sesión") {

                val prefs = getSharedPreferences("session", MODE_PRIVATE)
                prefs.edit().clear().apply()

                Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()

                verificarSesion()
            }

            true
        }

        popup.show()
    }

    private fun cargarProductos() {

        RetrofitClient.instance.listarProductos()
            .enqueue(object : Callback<List<Producto>> {

                override fun onResponse(
                    call: Call<List<Producto>>,
                    response: Response<List<Producto>>
                ) {
                    if (response.isSuccessful) {
                        val lista = response.body() ?: emptyList()
                        binding.recyclerProductos.adapter = ProductoAdapter(lista)
                    }
                }

                override fun onFailure(call: Call<List<Producto>>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Error conexión", Toast.LENGTH_SHORT).show()
                }
            })
    }
}