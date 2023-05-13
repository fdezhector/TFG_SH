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
abstract class BetterYouBBDD : RoomDatabase() {
    abstract val betterYouDao:BetterYouDao
    //Arquitectura singleton
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