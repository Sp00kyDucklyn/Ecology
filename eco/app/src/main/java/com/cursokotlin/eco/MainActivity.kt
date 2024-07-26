package com.cursokotlin.eco

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.cursokotlin.eco.database.DataBase
import com.cursokotlin.eco.databinding.ActivityMainBinding
import com.cursokotlin.eco.repository.AlbumRepository
import com.cursokotlin.eco.repository.EtiquetesRepository
import com.cursokotlin.eco.repository.ProjectRepository
import com.cursokotlin.eco.viewmodel.AlbumViewModel
import com.cursokotlin.eco.viewmodel.AlbumViewModelFactory
import com.cursokotlin.eco.viewmodel.EtiqueteViewModel
import com.cursokotlin.eco.viewmodel.EtiqueteViewModelFactory
import com.cursokotlin.eco.viewmodel.ProjectViewModel
import com.cursokotlin.eco.viewmodel.ProjectViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    lateinit var projectViewModel: ProjectViewModel
    lateinit var albumViewModel: AlbumViewModel
    lateinit var etiqueteViewModel: EtiqueteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViewModels()


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.background=null
        val addsBtn = findViewById<FloatingActionButton>(R.id.fabMenu)

        bottomNav.setupWithNavController(navController)


        // Configurar el listener para manejar la navegación cuando se hace clic en un elemento del menú
        bottomNav.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_Home -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.menu_Etiquete -> {
                    navController.navigate(R.id.etiquetesFragment)
                    true
                }
                else -> false
            }
        }

        addsBtn.setOnClickListener{
            navController.navigate(R.id.addFragment)
        }

    }


    private fun setupViewModels() {
        // Obtener la instancia de la base de datos
        val database = DataBase.invoke(this)

        // Crear repositorios
        val albumRepository = AlbumRepository(database)
        val projectRepository = ProjectRepository(database)
        val etiqueteRepository = EtiquetesRepository(database)

        // Crear ViewModelProviderFactory
        val albumViewModelFactory = AlbumViewModelFactory(application, albumRepository)
        val projectViewModelFactory = ProjectViewModelFactory(application, projectRepository)
        val etiqueteViewModelFactory = EtiqueteViewModelFactory(application, etiqueteRepository)

        // Inicializar los ViewModels
        albumViewModel = ViewModelProvider(this, albumViewModelFactory)[AlbumViewModel::class.java]
        projectViewModel = ViewModelProvider(this, projectViewModelFactory)[ProjectViewModel::class.java]
        etiqueteViewModel = ViewModelProvider(this, etiqueteViewModelFactory)[EtiqueteViewModel::class.java]
    }

}