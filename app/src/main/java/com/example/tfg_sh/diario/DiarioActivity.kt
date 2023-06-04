package com.example.tfg_sh.diario

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tfg_sh.R
import com.example.tfg_sh.Utils
import com.example.tfg_sh.bbdd.BetterYouBBDD
import com.example.tfg_sh.bbdd.dao.BetterYouDao
import com.example.tfg_sh.bbdd.entidades.Diario
import com.example.tfg_sh.databinding.ActivityDiarioEditBinding
import com.example.tfg_sh.databinding.ActivityDiarioEmptyBinding
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.launch


class DiarioActivity : AppCompatActivity() {

    private lateinit var diarioEmpty: ActivityDiarioEmptyBinding
    private lateinit var diarioEdit: ActivityDiarioEditBinding
    private var listaEmocionesStringMarcadas = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        diarioEdit = ActivityDiarioEditBinding.inflate(layoutInflater)
        diarioEmpty = ActivityDiarioEmptyBinding.inflate(layoutInflater)
        setContentView(diarioEmpty.root)
        val notaId = intent.getIntExtra("notaId", -1)
        //Instancia BBDD
        val dao = BetterYouBBDD.getInstance(this).betterYouDao
        var diario: Diario? = null

        lifecycleScope.launch {
            diario = dao.getDiarioNotaId(notaId)
            cargarDiario(diario)
        }

        // diario vacio

        diarioEmpty.buttonEditar.setOnClickListener { startEditActivity() }
        diarioEmpty.buttonCerrar.setOnClickListener { Utils.goToMainScreen(this) }

        // editar diario

        val imageButtons = getListImageButtons()
        var color: String? = null

        diarioEdit.color1.setOnClickListener {
            diarioEdit.isColorSelected.visibility = View.VISIBLE
            setBackgroundModeImageButton(diarioEdit.color1, imageButtons, R.color.purple_depressed)
            color = Integer.toHexString(ContextCompat.getColor(this, R.color.purple_depressed))
        }

        diarioEdit.color2.setOnClickListener {
            diarioEdit.isColorSelected.visibility = View.VISIBLE
            setBackgroundModeImageButton(diarioEdit.color2, imageButtons, R.color.purple_sad)
            color = Integer.toHexString(ContextCompat.getColor(this, R.color.purple_sad))
        }

        diarioEdit.color3.setOnClickListener {
            diarioEdit.isColorSelected.visibility = View.VISIBLE
            setBackgroundModeImageButton(diarioEdit.color3, imageButtons, R.color.blue_neutral)
            color = Integer.toHexString(ContextCompat.getColor(this, R.color.blue_neutral))
        }

        diarioEdit.color4.setOnClickListener {
            diarioEdit.isColorSelected.visibility = View.VISIBLE
            setBackgroundModeImageButton(diarioEdit.color4, imageButtons, R.color.green_joy)
            color = Integer.toHexString(ContextCompat.getColor(this, R.color.green_joy))
        }

        diarioEdit.color5.setOnClickListener {
            diarioEdit.isColorSelected.visibility = View.VISIBLE
            setBackgroundModeImageButton(diarioEdit.color5, imageButtons, R.color.green_super)
            color = Integer.toHexString(ContextCompat.getColor(this, R.color.green_super))
        }

        diarioEdit.buttonGuardar.setOnClickListener {
            if (diarioEdit.isColorSelected.visibility != View.VISIBLE) {
                StyleableToast.makeText(this, "Tienes que seleccionar un color", Toast.LENGTH_LONG, R.style.toast_by).show()
                return@setOnClickListener
            }
            val descripcion: String = diarioEdit.descripcionDiario.text.toString()
            //val emociones = getListaEmocionesMarcadasAsStringList(ObjectEmocion.listaEmocionesMarcadas)

            actualizarDiario(dao, notaId, diario, color, descripcion)
            Utils.goToMainScreen(this)
            StyleableToast.makeText(this, "Diario guardado", Toast.LENGTH_LONG, R.style.toast_by).show()
        }

        diarioEdit.buttonBorrar.setOnClickListener {
            borrarDiario(dao, diario, notaId)
        }

        diarioEdit.buttonCerrar.setOnClickListener { Utils.goToMainScreen(this) }

    }

    private fun cargarDiario(diario: Diario?) {
        if (diario != null) {
            setColorSeleccionado(diario)
            diarioEdit.descripcionDiario.setText(diario.descripcion)
            if (!diario.color.isNullOrEmpty()) {
                startEditActivity()
            }
        }
    }

    private fun setColorSeleccionado(diario: Diario) {
        if (diario.color.equals(
                Integer.toHexString(
                    ContextCompat.getColor(
                        this@DiarioActivity,
                        R.color.purple_depressed
                    )
                ), ignoreCase = true
            )
        ) {
            diarioEdit.color1.setColorFilter(ContextCompat.getColor(this, R.color.purple_depressed))
        } else if (diario.color.equals(
                Integer.toHexString(
                    ContextCompat.getColor(
                        this@DiarioActivity,
                        R.color.purple_sad
                    )
                ), ignoreCase = true
            )
        ) {
            diarioEdit.color2.setColorFilter(ContextCompat.getColor(this, R.color.purple_sad))
        } else if (diario.color.equals(
                Integer.toHexString(
                    ContextCompat.getColor(
                        this@DiarioActivity,
                        R.color.blue_neutral
                    )
                ), ignoreCase = true
            )
        ) {
            diarioEdit.color3.setColorFilter(ContextCompat.getColor(this, R.color.blue_neutral))
        } else if (diario.color.equals(
                Integer.toHexString(
                    ContextCompat.getColor(
                        this@DiarioActivity,
                        R.color.green_joy
                    )
                ), ignoreCase = true
            )
        ) {
            diarioEdit.color4.setColorFilter(ContextCompat.getColor(this, R.color.green_joy))
        } else if (diario.color.equals(
                Integer.toHexString(
                    ContextCompat.getColor(
                        this@DiarioActivity,
                        R.color.green_super
                    )
                ), ignoreCase = true
            )
        ) {
            diarioEdit.color5.setColorFilter(ContextCompat.getColor(this, R.color.green_super))
        }
    }

    private fun actualizarDiario(
        dao: BetterYouDao,
        notaId: Int,
        diario: Diario?,
        color: String?,
        descripcion: String?
    ) {
        lifecycleScope.launch {
            if (diario != null) {
                dao.updateDiario(
                    Diario(
                        id = diario.id,
                        descripcion = descripcion,
                        color = color,
                        notaId = notaId
                    )
                )
            }
        }
    }

    private fun borrarDiario(dao: BetterYouDao, diario: Diario?, notaId: Int) {
        val alertDialog = AlertDialog.Builder(this)
        val customView = LayoutInflater.from(this).inflate(R.layout.custom_alert_dialog, null)
        alertDialog.setView(customView)
        val dialog = alertDialog.create()
        //Elementos alertDialog
        val titulo = customView.findViewById<TextView>(R.id.Title)
        val mensaje = customView.findViewById<TextView>(R.id.Message)
        val aceptar = customView.findViewById<Button>(R.id.PositiveButton)
        val cancelar = customView.findViewById<Button>(R.id.NegativeButton)

        aceptar.setOnClickListener {

            lifecycleScope.launch {
                dao.updateDiario(
                    Diario(
                        id = diario!!.id,
                        descripcion = null,
                        color = null,
                        notaId = notaId
                    )
                )
            }
            Utils.goToMainScreen(this)
            dialog.dismiss()
            StyleableToast.makeText(this, "Se ha borrado el diario correctamente", Toast.LENGTH_LONG, R.style.toast_by).show()
        }

        cancelar.setOnClickListener {
            dialog.dismiss()
        }
        titulo.text = "Eliminar diario "
        mensaje.text = "¿Quieres borrar el diario actual?"

        dialog.show()
    }


    private fun startEditActivity() {
        setContentView(diarioEdit.root)
    }

    // Métodos para el color del día
    private fun getListImageButtons(): MutableList<ImageButton> {
        val imageButtons = mutableListOf<ImageButton>()
        imageButtons.add(diarioEdit.color1)
        imageButtons.add(diarioEdit.color2)
        imageButtons.add(diarioEdit.color3)
        imageButtons.add(diarioEdit.color4)
        imageButtons.add(diarioEdit.color5)
        return imageButtons
    }

    private fun setBackgroundModeImageButton(imageButtonSelected: ImageButton, imageButtons: MutableList<ImageButton>, color: Int) {
        for (imageButton in imageButtons) {
            if (imageButton == imageButtonSelected) {
                // Seleccionamos el botón actual y establecemos su color
                imageButton.isSelected = true
                imageButton.setColorFilter(ContextCompat.getColor(this, color))
            } else {
                // Deseleccionamos los otros botones y establecemos su color a uno predeterminado
                imageButton.isSelected = false
                imageButton.setColorFilter(ContextCompat.getColor(this, R.color.hintColor))
            }
        }
    }
}