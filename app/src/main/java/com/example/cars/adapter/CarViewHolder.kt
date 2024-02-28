package com.example.cars.adapter

import android.view.TextureView
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cars.R
import com.example.cars.model.Car

class CarViewHolder(view:View, position: (Int) -> Unit):RecyclerView.ViewHolder(view) {

    val tvMarca: TextView = view.findViewById(R.id.tv_car_marca)
    val tvModelo: TextView = view.findViewById(R.id.tv_car_modelo)

    init {
        itemView.setOnClickListener{
            position(adapterPosition)
        }
    }

    fun render(car:Car){
        tvMarca.text = car.marca
        tvModelo.text = car.modelo
    }
}