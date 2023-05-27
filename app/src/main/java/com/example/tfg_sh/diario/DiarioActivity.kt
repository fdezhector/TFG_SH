package com.example.tfg_sh.diario

import android.content.res.Resources
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_sh.R
import com.example.tfg_sh.Utils
import com.example.tfg_sh.databinding.ActivityDiarioEditBinding
import com.example.tfg_sh.databinding.ActivityDiarioEmptyBinding

class DiarioActivity : AppCompatActivity() {

    private lateinit var diarioEmpty: ActivityDiarioEmptyBinding
    private lateinit var diarioEdit: ActivityDiarioEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        diarioEdit = ActivityDiarioEditBinding.inflate(layoutInflater)
        diarioEmpty = ActivityDiarioEmptyBinding.inflate(layoutInflater)
        setContentView(diarioEmpty.root)

        // FIXME comprobamos si el día tiene un color, si tiene entonces cargamos el otro layout
        var i = 0
        if(i == 1){
            startEditActivity()
        }

        // diario vacio

        diarioEmpty.buttonEditar.setOnClickListener { startEditActivity() }
        diarioEmpty.buttonCerrar.setOnClickListener { Utils.goToMainScreen(this) }




        // editar diario

        diarioEdit.addEmotion.setOnClickListener {
        // TODO popup window para mostrar la lista de emociones / si no se consigue -> que se abra otra ventana y listo
            val popupView = layoutInflater.inflate(R.layout.popup_emotions_list, null)
            val popupWindow = PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,// FIXME cambiar altura a la que se queda
                true
            )

            mostrarPopupWindow(popupWindow, popupView)
            cargarEmocionesPredeterminadas()
            initRecyclerView(popupView)
        }

        diarioEdit.buttonCerrar.setOnClickListener { Utils.goToMainScreen(this) }

    }

    private fun initRecyclerView(popupView: View) {
        val recyclerCompletoEmociones = popupView.findViewById<RecyclerView>(R.id.recyclerCompletoEmociones)
        recyclerCompletoEmociones.layoutManager = LinearLayoutManager(this)
        recyclerCompletoEmociones.adapter = EmocionesAdapter(ObjectEmocion.getAll())
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

    private fun mostrarPopupWindow(popupWindow: PopupWindow, popupView: View) {
        // Obtener la altura de la pantalla
        val screenHeight = Resources.getSystem().displayMetrics.heightPixels

        // Calcular la posición vertical para mostrar el PopupWindow
        val popupHeight = popupWindow.contentView.height
        val desiredY = screenHeight / 2 - popupHeight

        // Mostrar el PopupWindow en la posición deseada
        popupWindow.showAtLocation(diarioEdit.addEmotion, Gravity.BOTTOM, 0, desiredY)

        val animation = AnimationUtils.loadAnimation(this, R.anim.anim_evento)
        popupView.startAnimation(animation)
    }

    private fun startEditActivity(){
        setContentView(diarioEdit.root)
    }



}