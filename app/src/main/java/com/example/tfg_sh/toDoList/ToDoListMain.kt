package com.example.tfg_sh.toDoList

import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_sh.R
import com.example.tfg_sh.Utils
import com.example.tfg_sh.bbdd.BetterYouBBDD
import com.example.tfg_sh.bbdd.dao.BetterYouDao
import com.example.tfg_sh.bbdd.entidades.Tarea
import com.example.tfg_sh.databinding.ActivityToDoListMainBinding
import com.example.tfg_sh.databinding.BottomNavBinding
import com.example.tfg_sh.databinding.ItemTareaBinding
import kotlinx.coroutines.launch

class ToDoListMain : AppCompatActivity() {

    private lateinit var toDoList: ActivityToDoListMainBinding
    private lateinit var bottomNav: BottomNavBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toDoList = ActivityToDoListMainBinding.inflate(layoutInflater)
        setContentView(toDoList.root)
        bottomNav = BottomNavBinding.bind(toDoList.bottomNav.root)

        val dao = BetterYouBBDD.getInstance(this).betterYouDao

        initRecyclerView()

        actualizarVistaVacia()

        toDoList.fab.setOnClickListener { view ->
            val popupMenu = PopupMenu(this, view)
            popupMenu.inflate(R.menu.actions_menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.agregarTarea -> {
                        agregarTarea()
                        true
                    }

                    R.id.eliminarTareas -> {
                        eliminarTareas(dao)
                        true
                    }

                    R.id.filtrar -> {
                        filtrarPorPrioridad()
                        true
                    }

                    else -> false
                }
            }
            popupMenu.show()
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                // this method is called when the item is moved.
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // this method is called when we swipe our item to the right direction.
                // on below line we are getting the item at a particular position.

            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                val itemTareaBinding = ItemTareaBinding.bind(viewHolder.itemView)
                // FIXME
                /*val position = viewHolder.bindingAdapterPosition
                val tarea = ObjectTarea.listaTareas[position]*/

                val halfScreenWidth = recyclerView.width / 2

                // Limita el desplazamiento horizontal hasta la mitad de la pantalla
                val clampedDx = dX.coerceIn(-halfScreenWidth.toFloat(), halfScreenWidth.toFloat())

                // Aplica el desplazamiento restringido
                super.onChildDraw(c, recyclerView, viewHolder, clampedDx, dY, actionState, isCurrentlyActive)

                // Obtiene la vista del ViewHolder
                val itemView = viewHolder.itemView

                // Calcula el porcentaje del desplazamiento en relación a la mitad de la pantalla
                val displacementRatio = clampedDx / halfScreenWidth

                // Calcula el desplazamiento máximo para volver a la posición inicial
                val maxSwipeDistance = halfScreenWidth / 5

                // Calcula el desplazamiento actual para volver a la posición inicial
                val swipeBackDistance = (maxSwipeDistance * displacementRatio).toInt()

                // Aplica la animación para volver a la posición inicial
                itemView.translationX = swipeBackDistance.toFloat()

                val editButton = itemTareaBinding.swipeEditButton
                val deleteButton = itemTareaBinding.swipeDeleteButton

                editButton.visibility = View.VISIBLE
                deleteButton.visibility = View.VISIBLE

                // FIXME añadir funcionalidad correcta a los swipebuttons
                /*editButton.setOnClickListener {
                    val intent = Intent(this@ToDoListMain, UpdateTareaActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    intent.putExtra("position", position)
                    startActivity(intent)
                }

                deleteButton.setOnClickListener {
                    ObjectTarea.deleteTarea(position)
                    lifecycleScope.launch {
                        dao.deleteTarea(Tarea(tarea.id, tarea.titulo, tarea.prioridad))
                    }
                }*/

                if (swipeBackDistance == 0) {
                    editButton.visibility = View.GONE
                    deleteButton.visibility = View.GONE
                }

            }
        }).attachToRecyclerView(toDoList.tareaRecyclerView)


        toDoList.resetFilter.setOnClickListener {
            ObjectTarea.listaTareas = ObjectTarea.listaAuxiliar.toMutableList()
            ObjectTarea.listaAuxiliar.clear()
            actualizarVisibilidadBotonFiltro()
            initRecyclerView()
        }

        //
        toDoList.addButton.setOnClickListener { agregarTarea() }
        toDoList.buttonCerrar.setOnClickListener { Utils.goToMainScreen(this) }
        bottomNav.buttonHome.setOnClickListener { Utils.goToMainScreen(this) }
        bottomNav.buttonSettings.setOnClickListener { Utils.goToSettings(this) }

    }//onCreate

    private fun initRecyclerView() {
        val recyclerView = toDoList.tareaRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TareaAdapter(ObjectTarea.getAll(), this)
        actualizarVistaVacia()
    }

    private fun actualizarVistaVacia() {
        if (toDoList.tareaRecyclerView.adapter!!.itemCount == 0) {
            toDoList.messageSinTareas.visibility = View.VISIBLE
            toDoList.addButton.visibility = View.VISIBLE
            toDoList.tareaRecyclerView.visibility = View.GONE
        } else {
            toDoList.messageSinTareas.visibility = View.GONE
            toDoList.addButton.visibility = View.GONE
            toDoList.tareaRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun filtrarPorPrioridad() {
        val view = toDoList.fab
        val popupMenuFiltrar = PopupMenu(this, view)
        popupMenuFiltrar.inflate(R.menu.actions_filtrar)
        var listaFiltrada = ObjectTarea.listaTareas
        popupMenuFiltrar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.prioridad_alta -> {
                    listaFiltrada = listaFiltrada.filter { itemTarea ->
                        itemTarea.prioridad.equals(
                            "Alta",
                            ignoreCase = true
                        )
                    } as MutableList<ItemTarea>
                    actualizarRecyclerFiltrado(listaFiltrada)
                    initRecyclerView()
                    true
                }

                R.id.prioridad_media -> {
                    listaFiltrada = listaFiltrada.filter { itemTarea ->
                        itemTarea.prioridad.equals(
                            "Media",
                            ignoreCase = true
                        )
                    } as MutableList<ItemTarea>
                    actualizarRecyclerFiltrado(listaFiltrada)
                    initRecyclerView()
                    true
                }

                R.id.prioridad_baja -> {
                    listaFiltrada = listaFiltrada.filter { itemTarea ->
                        itemTarea.prioridad.equals(
                            "Baja",
                            ignoreCase = true
                        )
                    } as MutableList<ItemTarea>
                    actualizarRecyclerFiltrado(listaFiltrada)
                    initRecyclerView()
                    true
                }

                else -> false
            }
        }
        popupMenuFiltrar.show()
    }

    //Nos creamos una lista auxiliar como copia, borramos la del recycler la borrarmos y establecemos la filtrada
    private fun actualizarRecyclerFiltrado(listaFiltrada: MutableList<ItemTarea>) {
        ObjectTarea.listaAuxiliar = ObjectTarea.listaTareas.toMutableList()
        ObjectTarea.listaTareas.clear()
        ObjectTarea.listaTareas = listaFiltrada
        actualizarVisibilidadBotonFiltro()
    }

    private fun actualizarVisibilidadBotonFiltro() {
        if (toDoList.resetFilter.visibility == View.GONE) {
            toDoList.resetFilter.visibility = View.VISIBLE
        } else {
            toDoList.resetFilter.visibility = View.GONE
        }
    }

    private fun agregarTarea() {
        val intent = Intent(this, InsertTareaActivity::class.java)
        startActivity(intent)
    }

    private fun eliminarTareas(dao: BetterYouDao) {
        val alertDialog = AlertDialog.Builder(this)
        val customView = LayoutInflater.from(this).inflate(R.layout.custom_alert_dialog, null)
        alertDialog.setView(customView)
        val dialog = alertDialog.create()

        val titulo = customView.findViewById<TextView>(R.id.Title)
        val mensaje = customView.findViewById<TextView>(R.id.Message)
        val aceptar = customView.findViewById<Button>(R.id.PositiveButton)
        val cancelar = customView.findViewById<Button>(R.id.NegativeButton)

        aceptar.setOnClickListener {
            ObjectTarea.deleteAll()
            lifecycleScope.launch {
                dao.deleteAllTareas()
            }
            initRecyclerView()
            Toast.makeText(this, "Se han borrado las tareas correctamente", Toast.LENGTH_LONG)
                .show()
            dialog.dismiss()
        }

        cancelar.setOnClickListener {
            dialog.dismiss()
        }

        titulo.text = "Eliminar tareas"
        mensaje.text = "¿Quieres borrar todas las tareas?"

        dialog.show()
    }

}
