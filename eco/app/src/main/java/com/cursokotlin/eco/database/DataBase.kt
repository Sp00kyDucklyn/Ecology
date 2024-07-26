package com.cursokotlin.eco.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cursokotlin.eco.model.Album
import com.cursokotlin.eco.model.Etiquete
import com.cursokotlin.eco.model.Project

@Database(entities = [Project::class, Album::class, Etiquete::class], version = 5)
abstract class DataBase: RoomDatabase() {


    abstract fun getProjectDao(): ProjectDao
    abstract fun getAlbumDao():AlbumDao
    abstract fun getEtiqueteDao():EtiqueteDao

    companion object{
        //Ayuda a que los cambios echos en un hilo, sean inmediatamente visibles en otros
        @Volatile
        //Singleton
        private var instance:DataBase? = null

        //Asegura que solo un hilo pueda ejecutar este pedazo de código al tiempo
        private var LOCK = Any()

        operator fun invoke(context: Context) = instance ?:
        synchronized(LOCK){
            instance ?:
            createDataBase(context).also{
                instance = it
            }
        }

        private fun createDataBase(context: Context)=
            Room.databaseBuilder(
                context.applicationContext,
                DataBase::class.java,
                name = "note_db"
            ).build()

    }

}