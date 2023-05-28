package com.example.tfg_sh.diario

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
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
import kotlinx.coroutines.launch


class DiarioActivity : AppCompatActivity() {

    private lateinit var diarioEmpty: ActivityDiarioEmptyBinding
    private lateinit var diarioEdit: ActivityDiarioEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        diarioEdit = ActivityDiarioEditBinding.inflate(layoutInflater)
        diarioEmpty = ActivityDiarioEmptyBinding.inflate(layoutInflater)
        setContentView(diarioEmpty.root)
        val notaId = intent.getIntExtra("notaId", -1)
        //Instancia BBDD
        val dao = BetterYouBBDD.getInstance(this).betterYouDao
        var diario: Diario? = null

        cargarEmocionesPredeterminadas()
        initRecyclerEmociones()

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
            setBackgroundModeImageButton(diarioEdit.color1, imageButtons)
            color = Integer.toHexString(ContextCompat.getColor(this, R.color.purple_depressed))
        }

        diarioEdit.color2.setOnClickListener {
            diarioEdit.isColorSelected.visibility = View.VISIBLE
            setBackgroundModeImageButton(diarioEdit.color2, imageButtons)
            color = Integer.toHexString(ContextCompat.getColor(this, R.color.purple_sad))
        }

        diarioEdit.color3.setOnClickListener {
            diarioEdit.isColorSelected.visibility = View.VISIBLE
            setBackgroundModeImageButton(diarioEdit.color3, imageButtons)
            color = Integer.toHexString(ContextCompat.getColor(this, R.color.blue_neutral))
        }

        diarioEdit.color4.setOnClickListener {
            diarioEdit.isColorSelected.visibility = View.VISIBLE
            setBackgroundModeImageButton(diarioEdit.color4, imageButtons)
            color = Integer.toHexString(ContextCompat.getColor(this, R.color.green_joy))
        }

        diarioEdit.color5.setOnClickListener {
            diarioEdit.isColorSelected.visibility = View.VISIBLE
            setBackgroundModeImageButton(diarioEdit.color5, imageButtons)
            color = Integer.toHexString(ContextCompat.getColor(this, R.color.green_super))
        }

        val listaEmocionesMarcadas = getListaEmocionesMarcadas()
        var listaEmocionesStringMarcadas =
            getListaEmocionesMarcadasAsStringList(listaEmocionesMarcadas)

        diarioEdit.editEmotionsButton.setOnClickListener {
            actualizarLayout()
        }

        diarioEdit.doneEditEmotionsButton.setOnClickListener {
            actualizarLayout()
            listaEmocionesStringMarcadas =
                getListaEmocionesMarcadasAsStringList(getListaEmocionesMarcadas())
            diarioEdit.textViewEmociones.text = listaEmocionesStringMarcadas.toString()
        }

        diarioEdit.deselectEmotionsButton.setOnClickListener {
            deselectAllCheckBoxes()
        }

        diarioEdit.buttonGuardar.setOnClickListener {
            if (diarioEdit.isColorSelected.visibility != View.VISIBLE) {
                Toast.makeText(this, "Tienes que seleccionar un color ", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val descripcion: String = diarioEdit.descripcionDiario.text.toString()
            actualizarDiario(dao, notaId, diario, color, listaEmocionesStringMarcadas, descripcion)
            Utils.goToMainScreen(this)
            Toast.makeText(this, "Diario guardado", Toast.LENGTH_LONG).show()
        }

        diarioEdit.buttonBorrar.setOnClickListener {
            borrarDiario(dao,diario,notaId)
        }

        diarioEdit.buttonCerrar.setOnClickListener { Utils.goToMainScreen(this) }

    }

    private fun cargarDiario(diario: Diario?) {
        if (diario != null) {
            setColorSeleccionado(diario)
            setEmociones(diario)
            diarioEdit.descripcionDiario.setText(diario.descripcion)
            if (!diario.color.isNullOrEmpty()) {
                startEditActivity()
            }
        }
    }

    private fun setEmociones(diario: Diario) {
        diarioEdit.textViewEmociones.text = diario.emociones.toString()
        if (diarioEdit.textViewEmociones.text.equals("null") || diarioEdit.textViewEmociones.text.equals("[]"))
            diarioEdit.textViewEmociones.text = "Selecciona las emociones que hayas sentido en el día de hoy"
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
            diarioEdit.color1.imageTintMode = PorterDuff.Mode.ADD
        } else if (diario.color.equals(
                Integer.toHexString(
                    ContextCompat.getColor(
                        this@DiarioActivity,
                        R.color.purple_sad
                    )
                ), ignoreCase = true
            )
        ) {
            diarioEdit.color2.imageTintMode = PorterDuff.Mode.ADD
        } else if (diario.color.equals(
                Integer.toHexString(
                    ContextCompat.getColor(
                        this@DiarioActivity,
                        R.color.blue_neutral
                    )
                ), ignoreCase = true
            )
        ) {
            diarioEdit.color3.imageTintMode = PorterDuff.Mode.ADD
        } else if (diario.color.equals(
                Integer.toHexString(
                    ContextCompat.getColor(
                        this@DiarioActivity,
                        R.color.green_joy
                    )
                ), ignoreCase = true
            )
        ) {
            diarioEdit.color4.imageTintMode = PorterDuff.Mode.ADD
        } else if (diario.color.equals(
                Integer.toHexString(
                    ContextCompat.getColor(
                        this@DiarioActivity,
                        R.color.green_super
                    )
                ), ignoreCase = true
            )
        ) {
            diarioEdit.color5.imageTintMode = PorterDuff.Mode.ADD
        }
    }

    private fun actualizarDiario(dao: BetterYouDao, notaId: Int, diario: Diario?, color: String?, listaEmociones: List<String>, descripcion: String?) {
        lifecycleScope.launch {
            if (diario != null) {
                dao.updateDiario(
                    Diario(
                        id = diario.id,
                        emociones = listaEmociones,
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
                        emociones = null,
                        descripcion = null,
                        color = null,
                        notaId = notaId
                    )
                )
            }
            Utils.goToMainScreen(this)
            dialog.dismiss()
            Toast.makeText(
                this,
                "Se ha borrado el diario correctamente",
                Toast.LENGTH_LONG
            ).show()
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

    private fun actualizarLayout() {
        if (diarioEdit.doneEditEmotionsButton.visibility == View.GONE) {
            diarioEdit.editEmotionsButton.visibility = View.GONE
            diarioEdit.textViewEmociones.visibility = View.GONE

            diarioEdit.doneEditEmotionsButton.visibility = View.VISIBLE
            diarioEdit.deselectEmotionsButton.visibility = View.VISIBLE
            diarioEdit.emotionList.visibility = View.VISIBLE
        } else {
            diarioEdit.editEmotionsButton.visibility = View.VISIBLE
            diarioEdit.textViewEmociones.visibility = View.VISIBLE

            diarioEdit.doneEditEmotionsButton.visibility = View.GONE
            diarioEdit.deselectEmotionsButton.visibility = View.GONE
            diarioEdit.emotionList.visibility = View.GONE
        }
    }

    private fun cargarEmocionesPredeterminadas() {
        val listaEmocionesPredeterminadas = listOf(
            ItemEmocion("Felicidad"),
            ItemEmocion("Tristeza"),
            ItemEmocion("Ira"),
            ItemEmocion("Miedo"),
            ItemEmocion("Sorpresa"),
            ItemEmocion("Asco"),
            ItemEmocion("Amor"),
            ItemEmocion("Enojo"),
            ItemEmocion("Alegría"),
            ItemEmocion("Dolor"),
            ItemEmocion("Emoción"),
            ItemEmocion("Ansiedad"),
            ItemEmocion("Esperanza"),
            ItemEmocion("Culpa"),
            ItemEmocion("Vergüenza")
        )
        ObjectEmocion.listaEmociones = listaEmocionesPredeterminadas.toMutableList()
    }

    private fun initRecyclerEmociones() {
        diarioEdit.emotionList.layoutManager = GridLayoutManager(this, 2, LinearLayoutManager.HORIZONTAL, false)
        diarioEdit.emotionList.adapter = EmocionesAdapter(ObjectEmocion.getAll())
    }

    // Métodos para las emociones
    private fun deselectAllCheckBoxes() {
        val recylerEmociones = diarioEdit.emotionList
        // Recorre todos los elementos del Recyler
        for (i in 0 until recylerEmociones.childCount) {
            val checkboxEmocion = recylerEmociones.findViewHolderForAdapterPosition(i)
            if (checkboxEmocion is EmocionesViewHolder) {
                val checkBox = checkboxEmocion.itemEmocion.itemRecyclerEmotion
                checkBox.isChecked = false
            }
        }
    }

    private fun getListaEmocionesMarcadas(): MutableList<ItemEmocion> {
        // Reiniciamos la lista
        ObjectEmocion.listaEmocionesMarcadas.clear()

        val recylerEmociones = diarioEdit.emotionList

        // Recorremos todos los elementos del recyler
        for (i in 0 until recylerEmociones.childCount) {
            val checkboxEmocion = recylerEmociones.findViewHolderForAdapterPosition(i)
            if (checkboxEmocion is EmocionesViewHolder) {
                val checkBox = checkboxEmocion.itemEmocion.itemRecyclerEmotion
                val emocion = checkboxEmocion.itemEmocion.itemRecyclerEmotion.text.toString()
                // Si la emocion está marcada, la añadimos a la lista auxiliar de emociones marcadas
                if (checkBox.isChecked) {
                    ObjectEmocion.listaEmocionesMarcadas.add(ItemEmocion(emocion))
                }
            }
        }
        return ObjectEmocion.listaEmocionesMarcadas
    }

    private fun getListaEmocionesMarcadasAsStringList(listaEmocionesMarcadas: MutableList<ItemEmocion>): MutableList<String> {
        val listaEmocionesMarcadasAsString = mutableListOf<String>()
        val recyclerEmociones = diarioEdit.emotionList

        for (i in 0 until listaEmocionesMarcadas.size) {
            val viewHolder = recyclerEmociones.getChildViewHolder(recyclerEmociones.getChildAt(i))
            if (viewHolder is EmocionesViewHolder) {
                listaEmocionesMarcadasAsString.add((viewHolder.itemEmocion.itemRecyclerEmotion.text.toString()))
            }
        }
        return listaEmocionesMarcadasAsString
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

    private fun setBackgroundModeImageButton(
        imageButtonSelected: ImageButton,
        imageButtons: MutableList<ImageButton>
    ) {
        for (imageButton in imageButtons) {
            if (imageButton == imageButtonSelected) {
                // Seleccionamos el botón actual y establecemos su tint mode como "add"
                imageButton.isSelected = true
                imageButton.imageTintMode = PorterDuff.Mode.ADD
            } else {
                // Deseleccionamos los otros botones y establecemos su tint mode como "multiply"
                imageButton.isSelected = false
                imageButton.imageTintMode = PorterDuff.Mode.MULTIPLY
            }
        }
    }
}