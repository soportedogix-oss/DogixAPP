package com.dogix.app

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ProductoAdapter(private val lista: List<Producto>) :
    RecyclerView.Adapter<ProductoAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProducto: ImageView = itemView.findViewById(R.id.imgProducto)
        val txtNombre: TextView = itemView.findViewById(R.id.txtNombre)
        val txtPrecio: TextView = itemView.findViewById(R.id.txtPrecio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = lista[position]

        holder.txtNombre.text = item.nombre
        holder.txtPrecio.text = "S/ ${item.precio}"

        Glide.with(holder.itemView.context)
            .load(item.imagen)
            .into(holder.imgProducto)

        holder.itemView.setOnClickListener {

            val context = holder.itemView.context

            val intent = Intent(context, DetalleProductoActivity::class.java)

            // 🔥 FIX AQUÍ
            intent.putExtra("idProducto", item.id)
            intent.putExtra("nombre", item.nombre)
            intent.putExtra("descripcion", item.descripcion)
            intent.putExtra("imagen", item.imagen)
            intent.putExtra("stock", item.stock)

            context.startActivity(intent)
        }
    }
}