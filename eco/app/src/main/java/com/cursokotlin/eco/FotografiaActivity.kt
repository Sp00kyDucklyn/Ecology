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
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
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
import androidx.core.view.children
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.math.log

class FotografiaActivity : AppCompatActivity() {
    private lateinit var btnCamera :Button
    private lateinit var imgView :ImageView
    private lateinit var btnVolver :Button
    private lateinit var btnAceptar: Button
    private lateinit var btnGaleria: Button
    private var lastClickTime: Long = 0
    private var imagenfile :File? =null
    private var imageUri: Uri? = null
    private lateinit var btnComprimir: Button
    private lateinit var spinner: Spinner
    private lateinit var camaraLauncher: ActivityResultLauncher<Intent>
    private lateinit var galeriaLauncher: ActivityResultLauncher<Intent>
    private lateinit var projectName: String
    private lateinit var albumName: String
    private lateinit var buttonA: Button
    private lateinit var buttonB: Button
    private lateinit var buttonC: Button
    private lateinit var buttonD: Button
    private lateinit var buttonE: Button
    private lateinit var textViewSelection: TextView
    private lateinit var textViewSeekBarValue: TextView
    private lateinit var seekBar: SeekBar
    private lateinit var attributesA:LinearLayout
    private lateinit var attributesB:LinearLayout
    private lateinit var attributesC:LinearLayout
    private lateinit var attributesD:LinearLayout
    private lateinit var attributesE:LinearLayout

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.fotografia)
        imgView = findViewById<ImageView>(R.id.imageView)
        btnCamera = findViewById<Button>(R.id.btnCamara)
        btnGaleria = findViewById<Button>(R.id.btnGaleria)
        btnVolver = findViewById<Button>(R.id.btnVolverMenu)
        btnAceptar = findViewById(R.id.btnAceptar)
        buttonA = findViewById(R.id.buttonA)
        buttonB = findViewById(R.id.buttonB)
        buttonC = findViewById(R.id.buttonC)
        buttonD = findViewById(R.id.buttonD)
        buttonE = findViewById(R.id.buttonE)
        seekBar = findViewById(R.id.slider)
        textViewSelection = findViewById(R.id.textViewSelection)

        attributesA = findViewById(R.id.attributesA);
        attributesB = findViewById(R.id.attributesB);
        attributesC = findViewById(R.id.attributesC);
        attributesD = findViewById(R.id.attributesD);
        attributesE = findViewById(R.id.attributesE);

        // Ocultar todos los layouts de atributos inicialmente
        attributesA.setVisibility(View.GONE);
        attributesB.setVisibility(View.GONE);
        attributesC.setVisibility(View.GONE);
        attributesD.setVisibility(View.GONE);
        attributesE.setVisibility(View.GONE);


        val maxValue = 30 // 30 pasos, de 0 a 30, donde cada paso representa 0.1
        seekBar.max = maxValue

        // Establecer el fondo personalizado para la SeekBar
       // seekBar.background = ProgressDrawable(this@FotografiaActivity)

        // Texto que mostrará el valor actual de la SeekBar
        val textViewSliderValue = findViewById<TextView>(R.id.textViewSeekBarValue)

        // Al mover la SeekBar, actualizar el valor mostrado
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val value = progress / 10.0f // Convertir el progreso a un valor entre 0.0 y 3.0
                textViewSliderValue.text = String.format("%.1f", value)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Configuración de botones para actualizar la selección y reiniciar la SeekBar
        val buttons = listOf(buttonA, buttonB, buttonC, buttonD, buttonE)
        val attributeLayouts = listOf(attributesA, attributesB, attributesC, attributesD, attributesE)  // Layouts de atributos


        var selectedButton: Button? = null
        var selectedLetterButton: Button? = null
        var selectedAttributeButton: Button? = null

        buttons.forEach { button ->
            button.setOnClickListener {
                // Primero, restaurar el color de todos los botones de letras a su color por defecto
                buttons.forEach { it.setBackgroundColor(ContextCompat.getColor(this, R.color.default_button_color)) }

                // Establecer el color del botón de letra seleccionado
                button.setBackgroundColor(ContextCompat.getColor(this, R.color.selected_button_color))

                // Restaurar la visibilidad de los layouts de atributos
                attributeLayouts.forEach { it.visibility = View.GONE }

                // Mostrar el layout de atributos correspondiente a la letra seleccionada
                val selectedLayout = when (button.id) {
                    R.id.buttonA -> attributesA
                    R.id.buttonB -> attributesB
                    R.id.buttonC -> attributesC
                    R.id.buttonD -> attributesD
                    R.id.buttonE -> attributesE
                    else -> null
                }
                selectedLayout?.visibility = View.VISIBLE

                // Actualizar el botón de letra seleccionado
                selectedLetterButton = button
            }
        }
        attributeLayouts.forEach { layout ->
            layout.children.filterIsInstance<Button>().forEach { attributeButton ->
                attributeButton.setOnClickListener {
                    // Si ya hay un botón de atributo seleccionado en esta sección, restaurar su color
                    selectedAttributeButton?.setBackgroundColor(ContextCompat.getColor(this, R.color.default_button_color))

                    // Establecer el color del botón de atributo seleccionado
                    attributeButton.setBackgroundColor(ContextCompat.getColor(this, R.color.selected_button_color))

                    // Actualizar el botón de atributo seleccionado
                    selectedAttributeButton = attributeButton
                    textViewSelection.text= "${selectedLetterButton?.text} - ${attributeButton.text}"
                }
            }
        }


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

        galeriaLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val selectedImageUri = result.data?.data
                selectedImageUri?.let { uri ->
                    // Move the image to the desired folder and obtain the URI
                    val newImageUri = moveImageToFolder(uri)
                    imgView.setImageURI(newImageUri)
                    imageUri = newImageUri  // Guarda la URI para usarla al guardar la imagen
                    btnAceptar.visibility = View.VISIBLE  // Mostrar el botón Aceptar después de seleccionar una imagen
                }
            }
        }

        btnVolver.setOnClickListener {
            if (imagenfile != null) {

                if (imagenfile!!.exists()) {
                    imagenfile!!.delete()
                }
            }
            finish()
        }
        btnCamera.setOnClickListener {
            if (imagenfile != null) {

                if (imagenfile!!.exists()) {
                    imagenfile!!.delete()
                }
            }
            if (checkAndRequestPermissions()) {
                abrirCamera()
            }
        }
        btnGaleria.setOnClickListener {
            if (imagenfile != null) {

                if (imagenfile!!.exists()) {
                    imagenfile!!.delete()
                }
            }
            abrirGaleria()
        }
        btnAceptar.setOnClickListener {
            imageUri?.let {
                val firstTag = when (selectedLetterButton?.text) {
                    "A" -> "A-SUBSTRATES"
                    "B" -> "B-HERBS,LOWSHRUBSANDTREESLESSTHAN3FEET(1METER)"
                    "C" -> "C-LOW SHRUBS AND TREES"
                    "D" -> "D-TREES GREATER THAN 3 FEET"
                    "E" -> "E-PLANTS WITHIN CITY LIMITS"
                    else -> "UNKNOWN"
                }

                // 2. Determinar el valor del segundo tag basado en el botón de atributo seleccionado
                val secondTag = selectedAttributeButton?.text.toString()

                // 3. Obtener el valor de la SeekBar y convertirlo en String para usarlo como el tercer tag
                val thirdTag = textViewSliderValue.text.toString()

                // 4. Guardar la imagen con los metadatos
                saveImageWithTags(it, mapOf(
                    ExifInterface.TAG_USER_COMMENT to firstTag,  // Primer tag
                    ExifInterface.TAG_ARTIST to secondTag,      // Segundo tag
                    ExifInterface.TAG_IMAGE_DESCRIPTION to thirdTag  // Tercer tag
                ))

                Toast.makeText(this, "Image saved to $it", Toast.LENGTH_LONG).show()
                btnAceptar.visibility = View.GONE // Hide the Accept button after saving
                finish()
            }
        }
        buttonA.performClick()

    }
    private fun moveImageToFolder(uri: Uri): Uri? {
        // Logic to copy the image to the desired folder and return the new URI
        // This is a placeholder and should be implemented based on your folder structure

        // Example code:
        val storageDir = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "$projectName/$albumName")
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val destinationFile = File(storageDir, "JPEG_${timestamp}.jpg")
        imagenfile = destinationFile;
        try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(destinationFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            return FileProvider.getUriForFile(this, "${applicationContext.packageName}.fileprovider", destinationFile)
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Error moving image", Toast.LENGTH_SHORT).show()
            return null
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
    private fun abrirGaleria() {
        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
            return // Ignorar el segundo click si es muy rápido
        }
        lastClickTime = SystemClock.elapsedRealtime()
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galeriaLauncher.launch(intent)
    }
    private fun abrirCamera() {
        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
            return // Ignorar el segundo click si es muy rápido
        }
        lastClickTime = SystemClock.elapsedRealtime()
        val storageDir = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "$projectName/$albumName")
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val file = File(storageDir, "JPEG_${timestamp}.jpg")
        imagenfile =file;
        imageUri = FileProvider.getUriForFile(this, "${applicationContext.packageName}.fileprovider", file)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        }
        camaraLauncher.launch(intent)
    }
    private fun checkAndRequestPermissions(): Boolean {
        val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val writeStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val readStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        val permissionsNeeded = mutableListOf<String>()

        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.CAMERA)
        }
//        if (writeStoragePermission != PackageManager.PERMISSION_GRANTED) {
//            permissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//        }
//        if (readStoragePermission != PackageManager.PERMISSION_GRANTED) {
//            permissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
//        }

        return if (permissionsNeeded.isNotEmpty()) {
            // Solicitar los permisos que
            Log.d("Permisos", "Solicitando permisos: $permissionsNeeded")
            ActivityCompat.requestPermissions(this, permissionsNeeded.toTypedArray(), PERMISSION_REQUEST_CODE)
            false
        } else {
            true // Todos los permisos están concedidos
        }
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()) {
                    val allPermissionsGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
                    if (allPermissionsGranted) {
                        // Permisos concedidos, abre la cámara
                        abrirCamera()
                    } else {
                        // Muestra un mensaje informando que se necesita el permiso
                        Toast.makeText(this, "Se requieren permisos para usar la cámara y el almacenamiento", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }



}
