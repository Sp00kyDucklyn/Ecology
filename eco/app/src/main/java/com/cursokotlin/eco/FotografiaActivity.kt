package com.cursokotlin.eco

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class FotografiaActivity : AppCompatActivity() {
    lateinit var btnCamera :Button
    lateinit var imgView :ImageView
    lateinit var btnVolver :Button
    lateinit var btnAceptar: Button

    private var imageUri: Uri? = null
    private lateinit var btnComprimir: Button
    private lateinit var spinner: Spinner
    private lateinit var camaraLauncher: ActivityResultLauncher<Intent>
    private lateinit var projectName: String
    private lateinit var albumName: String
    private lateinit var buttonA: Button
    private lateinit var buttonB: Button
    private lateinit var buttonC: Button
    private lateinit var buttonD: Button
    private lateinit var buttonE: Button
    private lateinit var textViewSelection: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.fotografia)
        imgView = findViewById<ImageView>(R.id.imageView)
        btnCamera = findViewById<Button>(R.id.btnCamara)
        btnVolver = findViewById<Button>(R.id.btnVolverMenu)
        btnAceptar = findViewById(R.id.btnAceptar)
        buttonA = findViewById(R.id.buttonA)
        buttonB = findViewById(R.id.buttonB)
        buttonC = findViewById(R.id.buttonC)
        buttonD = findViewById(R.id.buttonD)
        buttonE = findViewById(R.id.buttonE)
        textViewSelection = findViewById(R.id.textViewSelection)
       
        findViewById<SeekBar>(R.id.slider).apply {
            max = 3 // Establecer el rango del SeekBar a 0-3
            background = ProgressDrawable(this@FotografiaActivity) // Cambia YourActivityName por el nombre real
        }
        textViewValue.text = "0.0"


        projectName = intent.getStringExtra("PROJECT_NAME") ?: "DefaultProject"
        albumName = intent.getStringExtra("ALBUM_NAME") ?: "DefaultAlbum"
        camaraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                imageUri?.let {
                    val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(it))
                    imgView.setImageBitmap(bitmap)
                    btnAceptar.visibility = View.VISIBLE // Show the Accept button
                } // Show the Accept button
            }
        }

        btnVolver.setOnClickListener {
            if (imageUri != null) {
                // Delete the temporary image file if it exists and was not saved
                val tempFile = File(imageUri!!.path!!)
                if (tempFile.exists()) {
                    tempFile.delete()
                }
            }
            finish()
        }
        btnCamera.setOnClickListener {
            if (checkAndRequestPermissions()) {
                abrirCamera()
            }
        }
        btnAceptar.setOnClickListener {
            imageUri?.let {
                saveImageWithTags(it, mapOf(
                    ExifInterface.TAG_USER_COMMENT to "Your comment hesre",
                    ExifInterface.TAG_ARTIST to "Your name here"
                ))
                Toast.makeText(this, "Image saved to $it", Toast.LENGTH_LONG).show()
                btnAceptar.visibility = View.GONE // Hide the Accept button after saving
                finish()
            }
        }

    }
    private fun saveImageWithTags(imageUri: Uri, tags: Map<String, String>) {
        try {
            contentResolver.openFileDescriptor(imageUri, "rw")?.use {
                val exif = ExifInterface(it.fileDescriptor)
                tags.forEach { (tag, value) ->
                    exif.setAttribute(tag, value)
                }
                exif.saveAttributes()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Error adding tags to image", Toast.LENGTH_SHORT).show()
        }
    }
    private fun abrirCamera() {
        val storageDir = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "$projectName/$albumName")
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val file = File(storageDir, "JPEG_${timestamp}.jpg")
        imageUri = FileProvider.getUriForFile(this, "${applicationContext.packageName}.fileprovider", file)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        }
        camaraLauncher.launch(intent)
    }
    private fun checkAndRequestPermissions(): Boolean {
        val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val writeStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        val listPermissionsNeeded = ArrayList<String>()

        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }

        if (listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toTypedArray(), 1)
            return false
        }
        return true
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    abrirCamera()
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



}
