package com.example.tfg_sh.toDoList

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tfg_sh.Utils
import com.example.tfg_sh.bbdd.BetterYouBBDD
import com.example.tfg_sh.databinding.ActivityUpdateTareaBinding

class UpdateTareaActivity : AppCompatActivity() {

    private lateinit var updateTarea: ActivityUpdateTareaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTarea = ActivityUpdateTareaBinding.inflate(layoutInflater)
        setContentView(updateTarea.root)

        val dao = BetterYouBBDD.getInstance(this).betterYouDao

        // creamos el dropdown menu con las prioridades
        Utils.initDropDownMenu(updateTarea.prioridad,this)







        // TODO mirar cómo mejorar la navegación hacia atrás de las pantallas
        updateTarea.buttonCerrar.setOnClickListener {
            Utils.goToMainScreen(this)
        }
    }
}