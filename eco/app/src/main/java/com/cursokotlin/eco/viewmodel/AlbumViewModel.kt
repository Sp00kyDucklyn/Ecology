package com.cursokotlin.eco.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.cursokotlin.eco.model.Album
import com.cursokotlin.eco.repository.AlbumRepository
import kotlinx.coroutines.launch

class AlbumViewModel(app: Application, private val albumRepository: AlbumRepository):AndroidViewModel(app) {


    fun insertAlbum(album: Album)=
        viewModelScope.launch {
            albumRepository.insertAlbum(album)
        }

    fun updateAlbum(album: Album)=
        viewModelScope.launch {
            albumRepository.updateAlbum(album)
        }

    fun deleteAlbum(album: Album)=
        viewModelScope.launch {
            albumRepository.deleteAlbum(album)
        }

    fun getAllAlbum() = albumRepository.getAllAlbumes()

    fun getAlbumsByProjectId(projectId: Int): LiveData<List<Album>> {
        return albumRepository.getAlbumsByProjectId(projectId)
    }
}