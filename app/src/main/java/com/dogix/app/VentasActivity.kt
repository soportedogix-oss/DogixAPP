package com.dogix.app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dogix.app.databinding.ActivityVentasBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VentasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVentasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVentasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerVentas.layoutManager = LinearLayoutManager(this)

        cargarVentas()
    }

    private fun cargarVentas() {

        val prefs = getSharedPreferences("session", MODE_PRIVATE)
        val idUsuario = prefs.getInt("id_usuario", 0)

        RetrofitClient.instance.obtenerVentas(idUsuario)
            .enqueue(object : Callback<List<Venta>> {

                override fun onResponse(
                    call: Call<List<Venta>>,
                    response: Response<List<Venta>>
                ) {
                    if (response.isSuccessful && response.body() != null) {

                        val lista = response.body()!!

                        if (lista.isEmpty()) {
                            Toast.makeText(
                                this@VentasActivity,
                                "No tienes compras aún 📦",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        binding.recyclerVentas.adapter = VentaAdapter(lista)

                    } else {
                        Toast.makeText(
                            this@VentasActivity,
                            "Error ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<Venta>>, t: Throwable) {
                    Toast.makeText(
                        this@VentasActivity,
                        "Error conexión: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}