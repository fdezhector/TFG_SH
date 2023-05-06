package com.example.tfg_sh.diario

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tfg_sh.databinding.ActivityDiarioEditBinding
import com.example.tfg_sh.databinding.ActivityDiarioEmptyBinding

import com.example.tfg_sh.Utils

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

        diarioEmpty.buttonEditar.setOnClickListener { startEditActivity() }

        diarioEmpty.buttonCerrar.setOnClickListener { Utils.goToPreviousScreen(this) }
        diarioEdit.buttonCerrar.setOnClickListener { Utils.goToPreviousScreen(this) }

    }

    private fun startEditActivity(){
        setContentView(diarioEdit.root)
    }

}