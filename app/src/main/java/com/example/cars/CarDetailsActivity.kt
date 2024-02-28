package com.example.cars

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.cars.adapter.CarAdapter
import com.example.cars.model.Car
import com.example.cars.proxy.interfaces.CarService
import com.example.cars.proxy.retrofit.CarRetrofit
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CarDetailsActivity : AppCompatActivity() {

    private lateinit var fbtnEditar: FloatingActionButton
    private lateinit var fbtnEliminar: FloatingActionButton
    private lateinit var etId:TextInputEditText
    private lateinit var etMarca:TextInputEditText
    private lateinit var etModelo:TextInputEditText
    private lateinit var etAnio:TextInputEditText
    private lateinit var etAsientos:TextInputEditText
    private lateinit var etColor:TextInputEditText
    private lateinit var btnGuardar:Button
    private lateinit var btnCancelar:Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_details)

        fbtnEditar = findViewById(R.id.fbtn_details_edit)
        fbtnEliminar = findViewById(R.id.fbtn_details_delete)
        etId = findViewById(R.id.et_details_id)
        etMarca = findViewById(R.id.et_details_marca)
        etModelo = findViewById(R.id.et_details_modelo)
        etAnio = findViewById(R.id.et_details_anio)
        etAsientos = findViewById(R.id.et_details_asientos)
        etColor = findViewById(R.id.et_details_color)
        btnGuardar = findViewById(R.id.btn_details_guardar)
        btnCancelar = findViewById(R.id.btn_details_cancelar)

        fbtnEditar.setOnClickListener{
            etMarca.isEnabled = true
            etModelo.isEnabled = true
            etAnio.isEnabled = true
            etAsientos.isEnabled = true
            etColor.isEnabled = true
            btnGuardar.visibility = View.VISIBLE
            btnCancelar.visibility = View.VISIBLE

        }
        btnCancelar.setOnClickListener{
            etMarca.isEnabled = false
            etModelo.isEnabled = false
            etAnio.isEnabled = false
            etAsientos.isEnabled = false
            etColor.isEnabled = false
            btnGuardar.visibility = View.INVISIBLE
            btnCancelar.visibility = View.INVISIBLE
            obtenerData()
        }

        obtenerData()
        eliminarCar()
        actualizarCar()
    }

    private fun actualizarCar() {
        btnGuardar.setOnClickListener{
            if(validateForm()){
                var idCar = etId.text.toString().trim()
                val marcaCar = etMarca.text.toString().trim()
                val modeloCar = etModelo.text.toString().trim()
                val anioCar = etAnio.text.toString().trim()
                val asientosCar = etAsientos.text.toString().trim()
                val colorCar = etColor.text.toString().trim()
                val anios:Int = anioCar.toInt()
                val id:Int = idCar.toInt()

                val car = Car(
                    carId = id,
                    marca = marcaCar,
                    modelo = modeloCar,
                    anioCreacion = anios,
                    asientos = asientosCar,
                    color = colorCar
                )

                val alertDialog = AlertDialog.Builder(this)

                alertDialog.setMessage("¿Estas seguro que quieres actualizar el Carro con ID ${id}?")
                    .setCancelable(false)
                    .setPositiveButton(
                        "Si",
                        DialogInterface.OnClickListener{ dialogInterface, i ->

                            CoroutineScope(Dispatchers.IO).launch {
                                val retrofit = CarRetrofit.getRetrofit().create(CarService::class.java)
                                    .updateCar(car)
                                val data = retrofit.body()
                                runOnUiThread{
                                    Toast.makeText(this@CarDetailsActivity,"Car actualizado correctamente", Toast.LENGTH_LONG).show()
                                    val irLista = Intent(this@CarDetailsActivity, MainActivity::class.java)
                                    startActivity(irLista)
                                }

                            }
                        }
                    )
                    .setNegativeButton("No",
                        DialogInterface.OnClickListener{dialogInterface, i ->
                            dialogInterface.cancel()
                        })
                alertDialog.create().show()

            }
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true

        if(etMarca.text.toString().isBlank() || etModelo.text.toString().isBlank() || etAnio.text.toString().isBlank() ||
            etAsientos.text.toString().isBlank() || etColor.text.toString().isBlank()){
            isValid = false
            Toast.makeText(this@CarDetailsActivity, "LLene todos los campos", Toast.LENGTH_LONG).show()
        }

        return isValid
    }

    private fun eliminarCar() {
        fbtnEliminar.setOnClickListener{
            val extras = intent.extras
            if(extras!=null){

                val carId = extras.getInt("carId")
                val alertDialog = AlertDialog.Builder(this)

                alertDialog.setMessage("¿Estas seguro que quieres eliminar el Carro con ID ${carId}?")
                    .setCancelable(false)
                    .setPositiveButton(
                        "Si",
                        DialogInterface.OnClickListener{ dialogInterface, i ->

                            CoroutineScope(Dispatchers.IO).launch {
                                val retrofit = CarRetrofit.getRetrofit().create(CarService::class.java)
                                    .deleteCar(carId)
                                val data = retrofit.body()

                                runOnUiThread{
                                    val intent = Intent(this@CarDetailsActivity, MainActivity::class.java)
                                    Toast.makeText(this@CarDetailsActivity, "Carro eliminado correcamente", Toast.LENGTH_SHORT).show()

                                    startActivity(intent)
                                }
                            }
                        }
                    )
                    .setNegativeButton("No",
                        DialogInterface.OnClickListener{dialogInterface, i ->
                            dialogInterface.cancel()
                        })
                alertDialog.create().show()


            }
        }
    }

    private fun obtenerData() {

        val extras = intent.extras
        if(extras!=null){

            val carId = extras.getInt("carId")
            CoroutineScope(Dispatchers.IO).launch {
                val retrofit = CarRetrofit.getRetrofit().create(CarService::class.java)
                    .getCar(carId)
                val data = retrofit.body()

                runOnUiThread{
                    if(retrofit.isSuccessful){
                        etId.setText(data!!.carId.toString())
                        etMarca.setText(data!!.marca)
                        etModelo.setText(data!!.modelo)
                        etAnio.setText(data!!.anioCreacion.toString())
                        etAsientos.setText(data!!.asientos)
                        etColor.setText(data!!.color)

                        }

                    }
                }
            }

        }

}