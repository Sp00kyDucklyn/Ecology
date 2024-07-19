package com.cursokotlin.eco

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class FotografiaActivity : AppCompatActivity() {
    lateinit var btnCamera :Button
    lateinit var imgView :ImageView
    lateinit var btnVolver :Button
    private lateinit var camaraLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.fotografia)
        imgView = findViewById<ImageView>(R.id.imageView)
        btnCamera = findViewById<Button>(R.id.btnCamara)
        btnVolver = findViewById<Button>(R.id.btnVolverMenu)

        camaraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val imageBitmap = data?.extras?.get("data") as Bitmap
                imgView.setImageBitmap(imageBitmap)
            }
        }
        btnVolver.setOnClickListener {
            finish()  // This will close FotografiaActivity and return to MainActivity
        }
        btnCamera.setOnClickListener {
            abrirCamera()
        }
    }

    private fun abrirCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        camaraLauncher.launch(intent)
    }
}