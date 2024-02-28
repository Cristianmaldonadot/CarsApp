package com.example.cars

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.cars.model.Car
import com.example.cars.proxy.interfaces.CarService
import com.example.cars.proxy.retrofit.CarRetrofit
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddCarActivity : AppCompatActivity() {

    private lateinit var btnGuardar: Button
    private lateinit var btnCancelar: Button
    private lateinit var etMarca: TextInputEditText
    private lateinit var etModelo: TextInputEditText
    private lateinit var etAnio: TextInputEditText
    private lateinit var etAsientos: TextInputEditText
    private lateinit var etColor: TextInputEditText
    private var maxCarId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_car)

        val extras = intent.extras

        if(extras != null){
            maxCarId = extras.getInt("maxCarId") +1
        }

        btnGuardar = findViewById(R.id.btn_add_guardar)
        btnCancelar = findViewById(R.id.btn_add_cancelar)

        configuraciones()
        guardarData()
        cancelar()

    }

    fun configuraciones(){
        etMarca = findViewById(R.id.et_registrarse_marca)
        etModelo = findViewById(R.id.et_agregar_modelo)
        etAnio = findViewById(R.id.et_agregar_anio)
        etAsientos = findViewById(R.id.et_agregar_asientos)
        etColor = findViewById(R.id.et_agregar_color)

    }

    private fun validateForm(): Boolean {
        var isValid = true

        if(etMarca.text.toString().isBlank() || etModelo.text.toString().isBlank() || etAnio.text.toString().isBlank() ||
            etAsientos.text.toString().isBlank() || etColor.text.toString().isBlank()){
            isValid = false
            Toast.makeText(this@AddCarActivity, "LLene todos los campos", Toast.LENGTH_LONG).show()
        }

        return isValid
    }

    fun guardarData(){
        btnGuardar.setOnClickListener{
            if(validateForm()){
                val marcaCar = etMarca.text.toString().trim()
                val modeloCar = etModelo.text.toString().trim()
                val anioCar = etAnio.text.toString().trim()
                val asientosCar = etAsientos.text.toString().trim()
                val colorCar = etColor.text.toString().trim()
                val anios:Int = anioCar.toInt()

                val car = Car(
                    carId = maxCarId,
                    marca = marcaCar,
                    modelo = modeloCar,
                    anioCreacion = anios,
                    asientos = asientosCar,
                    color = colorCar
                )

                val alertDialog = AlertDialog.Builder(this)

                alertDialog.setMessage("¿Estas seguro que quieres agregar el nuevo carro?")
                    .setCancelable(false)
                    .setPositiveButton(
                        "Si",
                        DialogInterface.OnClickListener{ dialogInterface, i ->

                            CoroutineScope(Dispatchers.IO).launch {
                                val retrofit = CarRetrofit.getRetrofit().create(CarService::class.java)
                                    .saveCar(car)
                                val data = retrofit.body()
                                runOnUiThread{
                                    Toast.makeText(this@AddCarActivity,"Carro agregado correctamente", Toast.LENGTH_LONG).show()
                                    val irLista = Intent(this@AddCarActivity, MainActivity::class.java)
                                    startActivity(irLista)
                                }

                            }
                        }
                    )
                    .setNegativeButton("No",
                        DialogInterface.OnClickListener{ dialogInterface, i ->
                            dialogInterface.cancel()
                        })
                alertDialog.create().show()

            }
        }
    }
    fun cancelar(){
        btnCancelar.setOnClickListener{
            val cancelar = Intent(this@AddCarActivity, MainActivity::class.java)
            startActivity(cancelar)
        }
    }
}