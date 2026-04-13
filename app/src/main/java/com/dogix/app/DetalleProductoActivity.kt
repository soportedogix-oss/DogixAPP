package com.dogix.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dogix.app.databinding.ActivityDetalleProductoBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetalleProductoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleProductoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetalleProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idProducto = intent.getIntExtra("idProducto", 0)
        val nombre = intent.getStringExtra("nombre") ?: ""
        val descripcion = intent.getStringExtra("descripcion") ?: ""
        val imagen = intent.getStringExtra("imagen") ?: ""
        val stock = intent.getIntExtra("stock", 0)

        Log.d("DEBUG", "ID PRODUCTO: $idProducto")

        binding.txtNombre.text = nombre
        binding.txtDescripcion.text = descripcion
        binding.txtStock.text = "Stock: $stock"

        Glide.with(this)
            .load(imagen)
            .into(binding.imgProducto)

        binding.btnAgregar.setOnClickListener {

            val prefs = getSharedPreferences("session", MODE_PRIVATE)
            val idUsuario = prefs.getInt("id_usuario", 0)

            if (idUsuario == 0) {
                Toast.makeText(this, "Debes iniciar sesión", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                return@setOnClickListener
            }

            if (idProducto == 0) {
                Toast.makeText(this, "Producto inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (stock <= 0) {
                Toast.makeText(this, "Sin stock", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = CarritoRequest(
                idUsuario = idUsuario,
                idProducto = idProducto,
                cantidad = 1
            )

            Log.d("DEBUG", "REQUEST: $request")

            RetrofitClient.instance.agregarCarrito(request)
                .enqueue(object : Callback<MensajeResponse> {

                    override fun onResponse(
                        call: Call<MensajeResponse>,
                        response: Response<MensajeResponse>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@DetalleProductoActivity,
                                "Agregado al carrito 🛒",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this@DetalleProductoActivity,
                                "Error ${response.code()}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<MensajeResponse>, t: Throwable) {
                        Toast.makeText(
                            this@DetalleProductoActivity,
                            "Error conexión",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }
}