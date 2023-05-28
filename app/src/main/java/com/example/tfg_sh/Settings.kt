package com.example.tfg_sh

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tfg_sh.bbdd.BetterYouBBDD
import com.example.tfg_sh.bbdd.dao.BetterYouDao
import com.example.tfg_sh.bbdd.entidades.DatosBBDD
import com.example.tfg_sh.bbdd.entidades.Tarea
import com.example.tfg_sh.databinding.ActivitySettingsBinding
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.io.File

class Settings : AppCompatActivity() {

    private lateinit var settings: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(settings.root)

        val dao = BetterYouBBDD.getInstance(this).betterYouDao

        initDropDownMenu()








        //TODO aplicar a los dos onClickListener de importar y exportar un alertDialog.
        // Para importar: se le pedirá que escoja un archivo JSON de su sistema (escalable a que se le pida un archivo JSON de Google Drive)
        // Para exportar: se le avisará con qué significa exportar BetterYou

        settings.layoutImportar.setOnClickListener {
            importarBBDD(dao)
        }

        settings.layoutExportar.setOnClickListener {
            exportarBBDD(dao)
        }

        settings.bottomNav.buttonHome.setOnClickListener {
            Utils.goToMainScreen(this)
        }
    }

    private fun exportarBBDD(dao: BetterYouDao){
        //Log.e("FILEDIR", getExternalFilesDir(null).toString())
        //Creamos las listas de todas las entidades de la BBDD
        lifecycleScope.launch{
            val diarios = dao.getAllDiarios()
            val eventos = dao.getAllEventos()
            val tareas = dao.getAllTareas() as List<Tarea>
            val notas = dao.getAllNotas()

            val datos = DatosBBDD(diarios,eventos,notas,tareas)
            // Parseo JSON
            val gson = Gson()
            val json = gson.toJson(datos)
            // Creamos el fichero
            val file = File(this@Settings.getExternalFilesDir(null), "data.json")
            file.writeText(json)
        }
    }

    //FIXME Al importar la BBDD el recycler view de las tareas no se sincroniza

    private fun importarBBDD(dao: BetterYouDao) {
        try {
            // Obtenemos la referencia al archivo JSON en el almacenamiento externo.
            val file = File(this.getExternalFilesDir(null), "data.json")
            // Leemos el archivo
            val jsonString = file.readText()
            // Convertimos el archivo en objetos de tipo DatosBBDD
            val gson = Gson()
            val datos = gson.fromJson(jsonString, DatosBBDD::class.java)
            // Insertamos todos los datos en la BBDD
            lifecycleScope.launch {
                dao.insertAllDiarios(datos.diarios)
                dao.insertAllEventos(datos.eventos)
                dao.insertAllTareas(datos.tareas)
                dao.insertAllNotas(datos.notas)
            }
        } catch (e: Exception) {
            Log.e("ImportarBBDD", "Error al importar los datos desde el archivo JSON", e)
        }
    }

    fun initDropDownMenu() {
        val horas = listOf("20:00", "21:00", "22:00", "23:00")
        // creamos el adaptador para el spinner
        val adaptador = ArrayAdapter(this, R.layout.dropdown_menu, horas)
        // definimos el estilo de los desplegables
        adaptador.setDropDownViewResource(R.layout.dropdown_menu_item)
        // relacionamos el adaptador con el spinner del xml
        settings.horaNotificacionDiario.adapter = adaptador

        // listener que se ejectutará cuando se ha seleccionado una prioridad
        settings.horaNotificacionDiario.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position) as String
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // NO APLICA | Maneja el caso en el que no se haya seleccionado ninguna opción
            }
        }
    }

}