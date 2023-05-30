package com.example.tfg_sh

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckedTextView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.tfg_sh.bbdd.BetterYouBBDD
import com.example.tfg_sh.bbdd.dao.BetterYouDao
import com.example.tfg_sh.bbdd.entidades.DatosBBDD
import com.example.tfg_sh.bbdd.entidades.Tarea
import com.example.tfg_sh.databinding.ActivitySettingsBinding
import com.example.tfg_sh.notificacion.NotificacionAlarma.Companion.NOTIFICATION_ID
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.io.File

class Settings : AppCompatActivity() {

    private lateinit var settings: ActivitySettingsBinding
    val dao = BetterYouBBDD.getInstance(this).betterYouDao
    private val sActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val uri: Uri? = data?.data
            //Proceso el archivo y veo que hago con el pasarle IMPORTAR-BBDD
            importarBBDD(dao, uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(settings.root)

        initDropDownMenu()


        // FIXME
        /*settings.horaNotificacionDiario.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position) as String

                cancelNotification(this@Settings)

                val horaNotificacion = Utils.obtenerHora(selectedItem.toString())
                // Notificacion Diario
                Utils.scheduleNotificacion(
                    this@Settings,
                    "Diario BetterYou",
                    "Escribe en tu diario...",
                    "¿Cómo te ha ido el día?",
                    (horaNotificacion.time - Calendar.getInstance().timeInMillis).toInt()
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // NO APLICA | Maneja el caso en el que no se haya seleccionado ninguna opción
            }
        }*/


        // TODO
        //  Para importar: se le pedirá que escoja un archivo JSON de su sistema (escalable a que se le pida un archivo JSON de Google Drive)
        //  Para exportar: se le avisará con qué significa exportar BetterYou

        settings.layoutAbout.setOnClickListener {
            val url = "https://github.com/fdezhector/TFG_SH"
            // Creamos un Intent con la acción ACTION_VIEW y la URL
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

            // Verificamos si hay aplicaciones disponibles para abrir la URL
            if (intent.resolveActivity(packageManager) != null) {
                // Mostrar las opciones de "Abrir con"
                startActivity(Intent.createChooser(intent, "Abrir con"))
            }

        }






        settings.layoutImportar.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this)
            val customView = LayoutInflater.from(this).inflate(R.layout.custom_alert_dialog, null)
            alertDialog.setView(customView)
            val dialog = alertDialog.create()
            //Elementos alertDialog
            val titulo = customView.findViewById<TextView>(R.id.Title)
            val mensaje = customView.findViewById<TextView>(R.id.Message)
            val aceptar = customView.findViewById<Button>(R.id.PositiveButton)
            val cancelar = customView.findViewById<Button>(R.id.NegativeButton)
            aceptar.text = "Importar"
            cancelar.visibility = View.GONE

            aceptar.setOnClickListener {
                openFileDialog()

                dialog.dismiss()
            }

            titulo.text = "Importar BetterYou"
            mensaje.text = "Selecciona el archivo JSON que deseas importar"

            dialog.show()
        }



        settings.layoutExportar.setOnClickListener {
            exportarBBDD(dao)
        }

        settings.bottomNav.buttonHome.setOnClickListener {
            Utils.goToMainScreen(this)
        }
    }

    private fun openFileDialog() {
        val data = Intent(Intent.ACTION_OPEN_DOCUMENT)
        data.addCategory(Intent.CATEGORY_OPENABLE)
        data.type = "*/*"
        val chooserIntent = Intent.createChooser(data, "Choose a file")
        sActivityResultLauncher.launch(chooserIntent)
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
        settings.horaNotificacionDiario.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedItem = parent.getItemAtPosition(position) as String

                    val dropDownShownItem =
                        this@Settings.findViewById<CheckedTextView>(R.id.dropdown_menu_layout)
                    dropDownShownItem.setTextColor(
                        ContextCompat.getColor(
                            this@Settings,
                            R.color.gris
                        )
                    )
                    dropDownShownItem.setBackgroundColor(
                        ContextCompat.getColor(
                            this@Settings,
                            R.color.negro
                        )
                    )
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // NO APLICA | Maneja el caso en el que no se haya seleccionado ninguna opción
                }
            }
    }

    private fun exportarBBDD(dao: BetterYouDao) {
        // Creamos las listas de todas las entidades de la BBDD
        lifecycleScope.launch {

            val diarios = dao.getAllDiarios()
            val eventos = dao.getAllEventos()
            val tareas = dao.getAllTareas() as List<Tarea>
            val notas = dao.getAllNotas()

            val datos = DatosBBDD(diarios, eventos, notas, tareas)
            // Parseo JSON
            val gson = Gson()
            val json = gson.toJson(datos)

            // Obtenemos la ruta de descargas
            val downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            // Creamos el directorio si no existe
            if (!downloadsFolder.exists()) {
                downloadsFolder.mkdirs()
            }
            // Creamos el fichero dentro de la ruta de descargas
            val file = File(downloadsFolder, "data.json")
            file.writeText(json)

            // Notificamos al sistema que se ha creado el nuevo fichero
            MediaScannerConnection.scanFile(
                this@Settings,
                arrayOf(file.toString()),
                null,
                null
            )
            Toast.makeText(this@Settings, "BetterYou ha sido exportado a descargas", Toast.LENGTH_LONG).show()
        }
    }

    //FIXME Al importar la BBDD el recycler view de las tareas no se sincroniza

    private fun importarBBDD(dao: BetterYouDao, uri: Uri?) {
        if (uri == null) {
            Toast.makeText(this, "No se seleccionó ningún archivo", Toast.LENGTH_LONG).show()
            return
        }

        var filePath: String? = uri.path
        if (filePath == null) {
            Toast.makeText(this, "No se puede obtener la ruta del archivo", Toast.LENGTH_LONG)
                .show()
            return
        }

        //Solucion para probar en emulador
        //filePath = filePath.replace("/document/raw:", "")

        // Obtenemos la referencia al archivo JSON en el almacenamiento externo.
        val file = File(filePath)

        // Leemos el archivo
        val jsonString = file.readText()
        // Convertimos el archivo en objetos de tipo DatosBBDD
        val gson = Gson()
        val datos = gson.fromJson(jsonString, DatosBBDD::class.java)
        // Insertamos todos los datos en la BBDD
        lifecycleScope.launch {
            try {
                dao.insertAllNotas(datos.notas)
                dao.insertAllDiarios(datos.diarios)
                dao.insertAllEventos(datos.eventos)
                dao.insertAllTareas(datos.tareas)
            } catch (e: Exception) {
                Toast.makeText(this@Settings, "Error al importar el archivo", Toast.LENGTH_LONG).show()
                Log.e("ERROR SQL",e.localizedMessage)
                Utils.goToSettings(this@Settings)
            }
        }

    }

    // Borrar la notificación
    private fun cancelNotification(context: Context) {
        // Obtener el NotificationManager
        val notificationManager = NotificationManagerCompat.from(context)

        // Cancelar la notificación
        notificationManager.cancel(NOTIFICATION_ID)
    }

}