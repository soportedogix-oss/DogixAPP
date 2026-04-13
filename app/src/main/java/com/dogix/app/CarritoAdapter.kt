package com.dogix.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CarritoAdapter(
    private val lista: MutableList<Carrito>
) : RecyclerView.Adapter<CarritoAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNombre: TextView = view.findViewById(R.id.txtNombre)
        val txtPrecio: TextView = view.findViewById(R.id.txtPrecio)
        val txtCantidad: TextView = view.findViewById(R.id.txtCantidad)
        val txtStock: TextView = view.findViewById(R.id.txtStock)

        val btnMas: ImageView = view.findViewById(R.id.btnMas)
        val btnMenos: ImageView = view.findViewById(R.id.btnMenos)
        val btnEliminar: ImageView = view.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_carrito, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = lista[position]

        holder.txtNombre.text = item.nombreProducto
        holder.txtPrecio.text = "S/ ${item.precio}"
        holder.txtCantidad.text = item.cantidad.toString()
        holder.txtStock.text = "Stock: ${item.stock}"

        // ➕ SUMAR
        holder.btnMas.setOnClickListener {

            val pos = holder.bindingAdapterPosition
            if (pos == RecyclerView.NO_POSITION) return@setOnClickListener

            val currentItem = lista[pos]

            if (currentItem.cantidad < currentItem.stock) {
                currentItem.cantidad++

                holder.txtCantidad.text = currentItem.cantidad.toString()
                actualizarCantidad(currentItem)

            } else {
                Toast.makeText(holder.itemView.context, "Stock máximo alcanzado", Toast.LENGTH_SHORT).show()
            }
        }

        // ➖ RESTAR
        holder.btnMenos.setOnClickListener {

            val pos = holder.bindingAdapterPosition
            if (pos == RecyclerView.NO_POSITION) return@setOnClickListener

            val currentItem = lista[pos]

            if (currentItem.cantidad > 1) {
                currentItem.cantidad--

                holder.txtCantidad.text = currentItem.cantidad.toString()
                actualizarCantidad(currentItem)
            }
        }

        // ❌ ELIMINAR
        holder.btnEliminar.setOnClickListener {

            val pos = holder.bindingAdapterPosition
            if (pos == RecyclerView.NO_POSITION) return@setOnClickListener

            val currentItem = lista[pos]

            eliminarItem(currentItem, pos, holder)
        }
    }

    // 🔥 ACTUALIZAR CANTIDAD
    private fun actualizarCantidad(item: Carrito) {

        val request = ActualizarCantidadRequest(
            item.idCarrito,
            item.cantidad
        )

        RetrofitClient.instance.actualizarCarrito(request)
            .enqueue(object : Callback<MensajeResponse> {
                override fun onResponse(
                    call: Call<MensajeResponse>,
                    response: Response<MensajeResponse>
                ) {
                }

                override fun onFailure(call: Call<MensajeResponse>, t: Throwable) {
                }
            })
    }

    // 🔥 ELIMINAR ITEM
    private fun eliminarItem(item: Carrito, position: Int, holder: ViewHolder) {

        RetrofitClient.instance.eliminarCarrito(item.idCarrito)
            .enqueue(object : Callback<MensajeResponse> {

                override fun onResponse(
                    call: Call<MensajeResponse>,
                    response: Response<MensajeResponse>
                ) {
                    if (position >= 0 && position < lista.size) {
                        lista.removeAt(position)
                        notifyItemRemoved(position)
                    }

                    Toast.makeText(holder.itemView.context, "Producto eliminado", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<MensajeResponse>, t: Throwable) {
                    Toast.makeText(holder.itemView.context, "Error al eliminar", Toast.LENGTH_SHORT).show()
                }
            })
    }
}