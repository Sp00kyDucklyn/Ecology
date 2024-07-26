package com.cursokotlin.eco.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cursokotlin.eco.repository.AlbumRepository

class AlbumViewModelFactory (val app: Application, private val albumRepository: AlbumRepository):ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlbumViewModel(app, albumRepository) as T
    }

}