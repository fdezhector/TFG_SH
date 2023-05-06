package com.example.tfg_sh.toDoList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tfg_sh.MainActivity
import com.example.tfg_sh.R
import kotlinx.android.synthetic.main.activity_crear_card_view.create_priority
import kotlinx.android.synthetic.main.activity_crear_card_view.create_title
import kotlinx.android.synthetic.main.activity_crear_card_view.save_button

class CrearCardView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_card_view)
        save_button.setOnClickListener {
            if (create_title.text.toString().trim { it <= ' ' }.isNotEmpty()
                && create_priority.text.toString().trim { it <= ' ' }.isNotEmpty()
            ) {
                var titulo = create_title.getText().toString()
                var prioridad = create_priority.getText().toString()
                DataObject.setDatos(titulo, prioridad)
                val intent = Intent(this, ToDoListMain::class.java)
                startActivity(intent)

            }
        }
    }
}