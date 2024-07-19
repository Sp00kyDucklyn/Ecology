package com.cursokotlin.eco

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.cursokotlin.eco.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.background=null
        val addsBtn: FloatingActionButton

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


    }
}