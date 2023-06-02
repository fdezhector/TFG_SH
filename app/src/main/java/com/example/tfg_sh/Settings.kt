package com.example.tfg_sh

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
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
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.tfg_sh.bbdd.BetterYouBBDD
import com.example.tfg_sh.bbdd.dao.BetterYouDao
import com.example.tfg_sh.bbdd.entidades.DatosBBDD
import com.example.tfg_sh.bbdd.entidades.Tarea
import com.example.tfg_sh.databinding.ActivitySettingsBinding
import com.google.gson.Gson
import com.jakewharton.processphoenix.ProcessPhoenix
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class Settings : AppCompatActivity() {

    private lateinit var settings: ActivitySettingsBinding
    private val dao = BetterYouBBDD.getInstance(this).betterYouDao
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(this, "Permisos aceptados", Toast.LENGTH_LONG).show()
            }
        }

    private val sActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val uri: Uri? = data?.data
            //Proceso el archivo
            importarBBDD(dao, uri)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(settings.root)

        initDropDownMenu()

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
            if (!Utils.arePermissionsGranted(
                    this@Settings,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                && !Utils.arePermissionsGranted(
                    this@Settings,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                requestBetterYouFilePermissions()
            } else {
                alertDialogImportar()
            }
        }

        settings.layoutExportar.setOnClickListener {
            if (!Utils.arePermissionsGranted(
                    this@Settings,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                && !Utils.arePermissionsGranted(
                    this@Settings,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                requestBetterYouFilePermissions()
            } else {
                alertDialogExportar()
                Toast.makeText(this@Settings, "BetterYou exportado", Toast.LENGTH_LONG).show()
            }

        }

        settings.bottomNav.buttonHome.setOnClickListener {
            Utils.goToMainScreen(this)
        }
    }

    private fun requestBetterYouFilePermissions() {
        requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        requestPermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    private fun openFileDialog() {
        val data = Intent(Intent.ACTION_OPEN_DOCUMENT)
        data.addCategory(Intent.CATEGORY_OPENABLE)
        data.type = "*/*"
        val chooserIntent = Intent.createChooser(data, "Selecciona un archivo")
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

            // Obtén la ruta del directorio personalizado dentro del almacenamiento externo
            val backupDirectory = File(getExternalFilesDir(null), "Backups")

            // Crea el directorio si no existe
            if (!backupDirectory.exists()) {
                backupDirectory.mkdirs()
            }

            // Creamos el fichero dentro de la ruta de descargas
            val fechaHoraActual = Date()
            val fechaDia = SimpleDateFormat("dd-MM-yyyy(HH:mm)")
            val fileName = "BetterYou:${fechaDia.format(fechaHoraActual)}.json"
            val file = File(backupDirectory, fileName)

            file.writeText(json)

            // Notificamos al sistema que se ha creado el nuevo fichero
            MediaScannerConnection.scanFile(
                this@Settings,
                arrayOf(file.toString()),
                null,
                null
            )

        }
    }

    private fun importarBBDD(dao: BetterYouDao, uri: Uri?) {
        // Verifica si no se seleccionó ningún archivo
        if (uri == null) {
            Toast.makeText(this, "No se seleccionó ningún archivo", Toast.LENGTH_LONG).show()
            return
        }
        try {
            // Abre un flujo de entrada para acceder al archivo a través del proveedor de documentos
            val inputStream = contentResolver.openInputStream(uri)
            // Lee el contenido del archivo como una cadena de texto JSON
            val jsonString = inputStream?.bufferedReader().use { it?.readText() }
            // Verifica si se pudo leer el archivo correctamente
            if (jsonString != null) {
                val gson = Gson()
                // Convierte la cadena de texto JSON
                val datos = gson.fromJson(jsonString, DatosBBDD::class.java)
                //Inserciones en la BBDD en segundo plano
                synchronized(this){
                    lifecycleScope.launch {
                        try {
                            dao.insertAllNotas(datos.notas)
                            dao.insertAllDiarios(datos.diarios)
                            dao.insertAllEventos(datos.eventos)
                            dao.insertAllTareas(datos.tareas)

                        } catch (e: Exception) {
                            Toast.makeText(this@Settings, "Error al importar el archivo", Toast.LENGTH_LONG).show()
                            Log.e("ERROR SQL", e.localizedMessage)
                            Utils.goToSettings(this@Settings)
                        }
                    }
                }
                Toast.makeText(this@Settings, "BetterYou importado", Toast.LENGTH_LONG).show()
                alertDialogReiniciar()
            } else {
                Toast.makeText(this, "No se pudo leer el archivo", Toast.LENGTH_LONG).show()
            }

        } catch (e: IOException) {
            Toast.makeText(this, "Error al acceder al archivo", Toast.LENGTH_LONG).show()
            Log.e("ERROR IO", e.localizedMessage)
        }
    }

    private fun alertDialogImportar() {
        val alertDialog = AlertDialog.Builder(this)
        val customView =
            LayoutInflater.from(this).inflate(R.layout.custom_alert_dialog, null)
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

    private fun alertDialogExportar() {
        val alertDialog = AlertDialog.Builder(this)
        val customView =
            LayoutInflater.from(this).inflate(R.layout.custom_alert_dialog, null)
        alertDialog.setView(customView)
        val dialog = alertDialog.create()
        //Elementos alertDialog
        val titulo = customView.findViewById<TextView>(R.id.Title)
        val mensaje = customView.findViewById<TextView>(R.id.Message)
        val aceptar = customView.findViewById<Button>(R.id.PositiveButton)
        val cancelar = customView.findViewById<Button>(R.id.NegativeButton)
        aceptar.text = "Aceptar"
        cancelar.visibility = View.GONE

        aceptar.setOnClickListener {
            exportarBBDD(dao)
            dialog.dismiss()
        }

        titulo.text = "Exportar BetterYou"
        mensaje.text = "Los datos que tengas guardados se exportarán a un archivo"

        dialog.show()
    }

    private fun alertDialogReiniciar() {
        val alertDialog = AlertDialog.Builder(this)
        val customView =
            LayoutInflater.from(this).inflate(R.layout.custom_alert_dialog, null)
        alertDialog.setView(customView)
        val dialog = alertDialog.create()
        //Elementos alertDialog
        val titulo = customView.findViewById<TextView>(R.id.Title)
        val mensaje = customView.findViewById<TextView>(R.id.Message)
        val aceptar = customView.findViewById<Button>(R.id.PositiveButton)
        val cancelar = customView.findViewById<Button>(R.id.NegativeButton)
        aceptar.text = "Reiniciar"
        cancelar.visibility = View.GONE

        aceptar.setOnClickListener {
            ProcessPhoenix.triggerRebirth(applicationContext);
        }

        titulo.text = "Reiniciar BetterYou"
        mensaje.text = "La aplicación necesita reiniciarse para aplicar los cambios"

        dialog.show()
    }

   /* fun doRestart(c: Context?) {
        try {
            //check if the context is given
            if (c != null) {
                //fetch the packagemanager so we can get the default launch activity
                // (you can replace this intent with any other activity if you want
                val pm = c.packageManager
                //check if we got the PackageManager
                if (pm != null) {
                    //create the intent with the default start activity for your application
                    val mStartActivity = pm.getLaunchIntentForPackage(
                        c.packageName
                    )
                    if (mStartActivity != null) {
                        mStartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        //create a pending intent so the application is restarted after System.exit(0) was called.
                        // We use an AlarmManager to call this intent in 100ms
                        val mPendingIntentId = 223344
                        val mPendingIntent = PendingIntent
                            .getActivity(
                                c, mPendingIntentId, mStartActivity,
                                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
                            )
                        //kill the application
                        System.exit(0)
                    } else {
                        Log.e("Restart", "Was not able to restart application, mStartActivity null")
                    }
                } else {
                    Log.e("Restart", "Was not able to restart application, PM null")
                }
            } else {
                Log.e("Restart", "Was not able to restart application, Context null")
            }
        } catch (ex: java.lang.Exception) {
            Log.e("Restart", "Was not able to restart application")
        }
    }*/

}