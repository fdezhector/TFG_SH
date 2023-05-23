package com.example.tfg_sh

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.CalendarView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tfg_sh.bbdd.BetterYouBBDD
import com.example.tfg_sh.bbdd.dao.BetterYouDao
import com.example.tfg_sh.bbdd.entidades.Diario
import com.example.tfg_sh.bbdd.entidades.Evento
import com.example.tfg_sh.bbdd.entidades.Nota
import com.example.tfg_sh.databinding.ActivityMainBinding
import com.example.tfg_sh.databinding.BottomNavBinding
import com.example.tfg_sh.diario.DiarioActivity
import com.example.tfg_sh.evento.EventoActivity
import com.example.tfg_sh.toDoList.ToDoListMain
import kotlinx.coroutines.launch
import java.util.Date

class MainActivity : AppCompatActivity() {

    private lateinit var main: ActivityMainBinding
    private lateinit var bottomNav: BottomNavBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        main = ActivityMainBinding.inflate(layoutInflater)
        //Instancia BBDD
        val dao = BetterYouBBDD.getInstance(this).betterYouDao
        var id = 0
        setContentView(main.root)
        bottomNav = BottomNavBinding.bind(main.bottomNav.root)

        cargarVistaVacia()

        main.calendarView.setOnDateChangeListener(object : CalendarView.OnDateChangeListener {
            override fun onSelectedDayChange(calendario: CalendarView, year: Int, month: Int, dayOfMonth: Int) {
                // cargamos el layout
                cargarVistaElementos()
                // comprobamos si existe o no una nota
                val fechaNota = Utils.obtenerFechaNota(year, month, dayOfMonth)
                id = Utils.setId(fechaNota)
                lifecycleScope.launch {
                    val nota: Nota?
                    nota = existeNota(dao, id)
                    //Si no existe, creamos todas las entidades
                    if (nota == null) {
                        insertarTodasEntidades(nota, id, fechaNota, dao)
                    }
                }
            }
        })

        main.vistaEvento.setOnClickListener {
            val vistaEventoActivty = Intent(this, EventoActivity::class.java)
            vistaEventoActivty.putExtra("notaId",id)
            startActivity(vistaEventoActivty)
        }

        main.vistaDiario.setOnClickListener {
            val vistaDiarioActivity = Intent(this, DiarioActivity::class.java)
            vistaDiarioActivity.putExtra("notaId",id)
            startActivity(vistaDiarioActivity)
        }

        main.vistaToDoList.setOnClickListener {
            val titulo = "Notificacion de prueba"
            val textoCorto = "Tienes que ingresar a la app para realizar tus tareas"
            val textoDetallado = "Tienes pendientes muchas tareas, deberias ir realizandolas o se te van a acumular"
            val vistaToDoListActivity = Intent(this, ToDoListMain::class.java)
            startActivity(vistaToDoListActivity)
        }

        bottomNav.buttonSettings.setOnClickListener {
            Utils.goToSettings(this)
        }

    }

    private suspend fun insertarTodasEntidades(
        nota: Nota?, id: Int, fechaNota: Date, dao: BetterYouDao
    ) {
        var nota1 = nota
        nota1 = Nota(id, fechaNota.toString())
        dao.insertNota(nota1)
        dao.insertDiario(
            Diario(
                emociones = null, descripcion = null, notaId = nota1.id
            )
        )
        dao.insertEvento(
            Evento(
                titulo = null,
                descripcion = null,
                ubicacion = null,
                horaInicio = null,
                horaFin = null,
                notaId = nota1.id
            )
        )
    }

    // FIXME mirar si se puede hacer estos dos métodos en uno solo
    private fun cargarVistaVacia() {
        main.layoutElementos.visibility = View.GONE
        main.layoutLogo.visibility = View.VISIBLE
    }

    private fun cargarVistaElementos(){
        if(main.layoutLogo.visibility == View.VISIBLE){
            main.layoutLogo.visibility = View.GONE
            main.layoutElementos.visibility = View.VISIBLE
            val animation = AnimationUtils.loadAnimation(this, R.anim.anim_elementos)
            main.layoutElementos.startAnimation(animation)
        }
    }

    private suspend fun existeNota(dao: BetterYouDao, id: Int): Nota? {
        return dao.getNota(id)
    }


}


