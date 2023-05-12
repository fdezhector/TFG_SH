package com.example.tfg_sh.diario

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

        // comprobamos si el día tiene un color, si tiene entonces cargamos el otro layout
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
        }

        diarioEdit.buttonCerrar.setOnClickListener { Utils.goToMainScreen(this) }

    }

    private fun startEditActivity(){
        setContentView(diarioEdit.root)
    }



}