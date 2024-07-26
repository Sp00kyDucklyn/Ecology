package com.cursokotlin.eco.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cursokotlin.eco.repository.AlbumRepository
import com.cursokotlin.eco.repository.ProjectRepository

class ProjectViewModelFactory (val app: Application, private val projectRepository: ProjectRepository):
    ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProjectViewModel(app, projectRepository) as T
    }

}