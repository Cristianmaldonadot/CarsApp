package com.example.cars.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cars.R
import com.example.cars.model.Car

class CarAdapter(private val carList:List<Car>,
                 private val clickListener: (Car) -> Unit
                 ): RecyclerView.Adapter<CarViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val vh = CarViewHolder(layoutInflater.inflate(R.layout.layout_car2, parent,false)){
            clickListener(carList[it])
        }
        return vh
    }

    override fun getItemCount(): Int = carList.size

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val itemCar = carList[position]
        holder.render(itemCar)
    }
}