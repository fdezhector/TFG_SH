package com.example.tfg_sh.toDoList

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tfg_sh.R
import com.example.tfg_sh.Utils
import com.example.tfg_sh.bbdd.BetterYouBBDD
import com.example.tfg_sh.bbdd.dao.BetterYouDao
import com.example.tfg_sh.databinding.ActivityToDoListMainBinding

class ToDoListMain : AppCompatActivity() {

    private lateinit var toDoList: ActivityToDoListMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toDoList = ActivityToDoListMainBinding.inflate(layoutInflater)
        setContentView(toDoList.root)

        //val dao = BetterYouBBDD.getInstance(this).betterYouDao
        initRecyclerView()

        toDoList.fab.setOnClickListener { view ->
            val popupMenu = PopupMenu(this, view)
            popupMenu.inflate(R.menu.actions_menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.agregarTarea -> {
                        //TODO agregarTarea()
                        agregarTarea()
                        true
                    }
                    R.id.eliminarTareas -> {
                        //TODO eliminarTareas()
                        true
                    }
                    R.id.filtrar -> {
                        //TODO filtrarPorPrioridad()
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }

        toDoList.buttonCerrar.setOnClickListener{ Utils.goToMainScreen(this) }

    }//onCreate

    private fun agregarTarea() {
        val intent = Intent(this, InsertTareaActivity::class.java)
        startActivity(intent)
    }

    private fun initRecyclerView(){
        val recyclerView = toDoList.tareaRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TareaAdapter(ObjectTarea.listaTareas)
    }

}
