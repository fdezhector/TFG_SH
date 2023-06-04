package com.example.tfg_sh

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
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
        
        cargarLogo()

        main.calendarView.setOnDateChangeListener { calendario, year, month, dayOfMonth ->
            // Cargamos el layout que contiene los elementos de to-do-list, evento y diario
            cargarElementos()
            // Convertimos la fecha en el ID de la Nota
            // Este ID lo utilizaremos como FK tanto en el evento como en el diario
            val fechaNota = Utils.obtenerFechaNota(year, month, dayOfMonth)
            id = Utils.setId(fechaNota)
            //Lanzamos una Corutina para hacer las consultas con la BBDD
            lifecycleScope.launch {
                // Comprobamos si existe o no una nota
                val nota: Nota? = existeNota(dao, id)
                //Si no existe, creamos todas las entidades
                if (nota == null) {
                    insertarTodasEntidades(nota, id, fechaNota, dao)
                }
            }
        }

        main.vistaEvento.setOnClickListener {
            val vistaEventoActivty = Intent(this, EventoActivity::class.java)
            // Pasamos el ID de la nota para poder realizar la consulta con la BBDD una vez nos encontremos en el Evento
            // de esta manera si ya existe un evento, podremos cargar en su layout los datos de la BBDD
            vistaEventoActivty.putExtra("notaId",id)
            startActivity(vistaEventoActivty)
        }

        main.vistaDiario.setOnClickListener {
            val vistaDiarioActivity = Intent(this, DiarioActivity::class.java)
            // Pasamos el ID de la nota para poder realizar la consulta con la BBDD una vez nos encontremos en el Diario
            // de esta manera si ya existe un diario, podremos cargar en su layout los datos de la BBDD
            vistaDiarioActivity.putExtra("notaId",id)
            startActivity(vistaDiarioActivity)
        }

        main.vistaToDoList.setOnClickListener {
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
        val nota1 = Nota(id, fechaNota.toString())
        dao.insertNota(nota1)
        dao.insertDiario(
            Diario(
                descripcion = null, color = null ,notaId = nota1.id
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

    private fun cargarLogo() {
        main.layoutElementos.visibility = View.GONE
        main.layoutLogo.visibility = View.VISIBLE
    }

    private fun cargarElementos(){
        if(main.layoutLogo.visibility == View.VISIBLE){
            main.layoutLogo.visibility = View.GONE

            val animationLogo = AnimationUtils.loadAnimation(this, R.anim.anim_layout_logo)
            main.layoutLogo.startAnimation(animationLogo)

            animationLogo.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    // No aplica
                }
                // Una vez termine la animacion que ha invocado este listener, se ejecutara este metodo
                override fun onAnimationEnd(animation: Animation?) {
                    main.layoutElementos.visibility = View.VISIBLE

                    val animationToDoList = AnimationUtils.loadAnimation(this@MainActivity, R.anim.anim_todolist)
                    main.vistaToDoList.startAnimation(animationToDoList)

                    val animationEvento = AnimationUtils.loadAnimation(this@MainActivity, R.anim.anim_evento)
                    main.vistaEvento.startAnimation(animationEvento)

                    val animationDiario = AnimationUtils.loadAnimation(this@MainActivity, R.anim.anim_diario)
                    main.vistaDiario.startAnimation(animationDiario)
                }

                override fun onAnimationRepeat(animation: Animation?) {
                    // No aplica
                }
            })

        }
    }

    private suspend fun existeNota(dao: BetterYouDao, id: Int): Nota? {
        return dao.getNota(id)
    }



}


