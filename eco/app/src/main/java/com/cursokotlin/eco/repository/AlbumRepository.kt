package com.cursokotlin.eco.repository

import androidx.lifecycle.LiveData
import androidx.room.Query
import com.cursokotlin.eco.database.AlbumDao
import com.cursokotlin.eco.database.DataBase
import com.cursokotlin.eco.model.Album

class AlbumRepository (private val db:DataBase){

    suspend fun insertAlbum(album: Album) = db.getAlbumDao().insertAlbum(album)
    suspend fun updateAlbum(album: Album) = db.getAlbumDao().updateAlbum(album)
    suspend fun deleteAlbum(album: Album) = db.getAlbumDao().deleteAlbum(album)

    fun getAllAlbumes() = db.getAlbumDao().getAllAlbumes()

    fun getAlbumsByProjectId(projectId: Int): LiveData<List<Album>> {
        return db.getAlbumDao().getAlbumsByProjectId(projectId)
    }
}