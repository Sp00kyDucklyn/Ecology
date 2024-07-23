package com.cursokotlin.eco

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
    private var imageBitmap: Bitmap? = null
    private lateinit var btnComprimir: Button

    private lateinit var camaraLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.fotografia)
        imgView = findViewById<ImageView>(R.id.imageView)
        btnCamera = findViewById<Button>(R.id.btnCamara)
        btnVolver = findViewById<Button>(R.id.btnVolverMenu)
        btnAceptar = findViewById(R.id.btnAceptar)

        btnComprimir = findViewById(R.id.btnComprimir)

        camaraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                imageBitmap = data?.extras?.get("data") as Bitmap
                imgView.setImageBitmap(imageBitmap)
                btnAceptar.visibility = View.VISIBLE // Show the Accept button
            }
        }
        btnVolver.setOnClickListener {
            finish()  // This will close FotografiaActivity and return to MainActivity
        }
        btnCamera.setOnClickListener {
            if (checkAndRequestPermissions()) {
                abrirCamera()
            }
        }
        btnAceptar.setOnClickListener {
            imageBitmap?.let {
                saveImageToExternalStorage(it)
                Toast.makeText(this, "Image saved", Toast.LENGTH_LONG).show()
                btnAceptar.visibility = View.GONE // Hide the Accept button after saving
            }
        }
        btnComprimir.setOnClickListener {
            compressImages()
        }
    }

    private fun abrirCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
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

    private fun saveImageToExternalStorage(bitmap: Bitmap) {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "JPEG_$timestamp.jpg"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (!storageDir?.exists()!!) {
            storageDir.mkdirs()
        }
        val file = File(storageDir, fileName)
        try {
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.close()
            Toast.makeText(this, "Image saved to ${file.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show()
        }
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

    private fun compressFilesToZip(files: List<File>, zipFile: File) {
        try {
            val fos = FileOutputStream(zipFile)
            val zos = ZipOutputStream(fos)

            for (file in files) {
                val fis = FileInputStream(file)
                val zipEntry = ZipEntry(file.name)
                zos.putNextEntry(zipEntry)

                val buffer = ByteArray(1024)
                var length: Int
                while (fis.read(buffer).also { length = it } >= 0) {
                    zos.write(buffer, 0, length)
                }

                zos.closeEntry()
                fis.close()
            }

            zos.close()
            fos.close()

            Toast.makeText(this, "Files compressed to ${zipFile.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Error compressing files", Toast.LENGTH_SHORT).show()
        }
    }
    private fun compressImages() {
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (storageDir != null && storageDir.exists()) {
            val imageFiles = storageDir.listFiles { file -> file.extension == "jpg" || file.extension == "jpeg" || file.extension == "png" }?.toList() ?: emptyList()
            if (imageFiles.isNotEmpty()) {
                val zipFile = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "images.zip")
                compressFilesToZip(imageFiles, zipFile)
            } else {
                Toast.makeText(this, "No images found to compress", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "No storage directory found", Toast.LENGTH_SHORT).show()
        }
    }
}
