package com.example.tfg_sh.bbdd

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tfg_sh.bbdd.dao.BetterYouDao
import com.example.tfg_sh.bbdd.entidades.Diario
import com.example.tfg_sh.bbdd.entidades.Evento
import com.example.tfg_sh.bbdd.entidades.Nota
import com.example.tfg_sh.bbdd.entidades.Tarea

@Database(
    entities = [
        Nota::class,
        Diario::class,
        Evento::class,
        Tarea::class
    ],
    version = 1
)

/**
 * Esta clase lo que va ha hacer es una patron de diseño Singleton,
 * permite que sólo tenga una instancia y proporcionar un punto de acceso
 * global a ella. Si no esta instanciada va a crear la BBDD y si ya esta creada coge
 * la instancia de la BBDD creada.
 */
abstract class BetterYouBBDD : RoomDatabase() {
    abstract val betterYouDao:BetterYouDao

    companion object {
        @Volatile
        private var INSTANCE : BetterYouBBDD? = null
        fun getInstance(context : Context):BetterYouBBDD{
            synchronized(this){
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    BetterYouBBDD::class.java,
                    "betteryou_db"
                ).build().also{
                    INSTANCE = it
                }
            }
        }
    }
}