package com.example.tfg_sh.toDoList

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.tfg_sh.Utils
import com.example.tfg_sh.databinding.ActivityToDoListMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ToDoListMain : AppCompatActivity() {
    private lateinit var toDoList: ActivityToDoListMainBinding
    private lateinit var bbdd: BBDDToDoList
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toDoList = ActivityToDoListMainBinding.inflate(layoutInflater)
        setContentView(toDoList.root)
        bbdd = Room.databaseBuilder(
            applicationContext, BBDDToDoList::class.java, "To_Do"
        ).build()
        toDoList.add.setOnClickListener {
            val intent = Intent(this,CrearCardView::class.java)
            startActivity(intent)
        }
        toDoList.deleteAll.setOnClickListener {
            DataObject.borrarTodos()
            GlobalScope.launch {
                bbdd.dao().borrarTodasTareas()
            }
            setRecycler()
        }
        setRecycler()

        toDoList.buttonCerrar.setOnClickListener{ Utils.goToPreviousScreen(this) }

    }
    private fun setRecycler(){
        toDoList.recyclerView.adapter = Adaptador(DataObject.getTodosLosDatos())
        toDoList.recyclerView.layoutManager = LinearLayoutManager(this)
    }

}
