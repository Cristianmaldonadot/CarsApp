package com.example.cars

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cars.adapter.CarAdapter
import com.example.cars.proxy.interfaces.CarService
import com.example.cars.proxy.retrofit.CarRetrofit
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var maxCarId = 0
    private lateinit var fbtnAgregar:FloatingActionButton
    private lateinit var recycledViewCar: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycledViewCar = findViewById(R.id.recycled_car)
        fbtnAgregar = findViewById(R.id.fbtn_main_agregar)

        configuraciones()
        cargarData()

    }
    fun configuraciones(){
        recycledViewCar.layoutManager = LinearLayoutManager(this)
        fbtnAgregar.setOnClickListener{
            val intent = Intent(this, AddCarActivity::class.java )
            intent.putExtra( "maxCarId", maxCarId )
            startActivity(intent)
        }

    }

    fun cargarData(){
        CoroutineScope(Dispatchers.IO).launch {
            val retrofit = CarRetrofit.getRetrofit().create(CarService::class.java)
                .getCars()
            val data = retrofit.body()

            runOnUiThread{
                if(retrofit.isSuccessful){
                    recycledViewCar.adapter = CarAdapter(data!!){
                        val carId = it.carId
                        val intent = Intent(this@MainActivity, CarDetailsActivity::class.java)
                            .apply {
                            putExtra("carId", it.carId)
                        }
                        startActivity(intent)
                    }
                    maxCarId = data!!.maxOf { it.carId }
                }
            }
        }
    }
}