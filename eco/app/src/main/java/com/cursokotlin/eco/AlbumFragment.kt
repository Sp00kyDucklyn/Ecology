package com.cursokotlin.eco

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.Toast
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.cursokotlin.eco.databinding.FragmentAlbumBinding
import com.cursokotlin.eco.databinding.FragmentProjectBinding
import com.cursokotlin.eco.model.Project
import com.cursokotlin.eco.viewmodel.AlbumViewModel
import com.cursokotlin.eco.viewmodel.ProjectViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import java.io.File

class AlbumFragment : Fragment() {
    private var albumBinding: FragmentAlbumBinding? = null
    private val binding get() = albumBinding!!
    private lateinit var tableLayout: TableLayout
    private var projectName: String? = null
    private var albumName: String? = null
    private lateinit var albumViewModel: AlbumViewModel

    private lateinit var projectViewModel: ProjectViewModel
    private lateinit var albumView: View

    private lateinit var camaraBtn: FloatingActionButton

    private val args: AlbumFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        albumName="DefaultAlbum"
        // Inflate the layout for this fragment
        albumBinding =  FragmentAlbumBinding.inflate(inflater, container, false)
        tableLayout = binding.root.findViewById(R.id.tableLayout)



        return binding.root
    }
    private fun loadImages() {
        val imageFiles = getImageFiles()
        val imageMargin = 8 // Margen en píxeles
        val numColumns = 3 // Número de imágenes por fila

        val displayMetrics = context?.resources?.displayMetrics
        val screenWidth = displayMetrics?.widthPixels ?: 1080 // Ancho de la pantalla en píxeles

        val imageSize = (screenWidth / numColumns) - (imageMargin * (numColumns + 1)) // Tamaño de imagen

        var row: TableRow? = null
        for (i in imageFiles.indices) {
            if (i % numColumns == 0) {
                row = TableRow(context)
                tableLayout.addView(row)
            }

            val imageView = ImageView(context)
            val imgFile = imageFiles[i]

            // Usar Glide para cargar la imagen de manera eficiente
            Glide.with(this)
                .load(imgFile) // Ruta del archivo de imagen
                .override(imageSize, imageSize) // Ajusta el tamaño de la imagen
                .centerCrop() // Recorta la imagen para llenar el ImageView
                .into(imageView) // Carga la imagen en el ImageView

            // Configuración de diseño para el ImageView
            val params = TableRow.LayoutParams(
                imageSize,
                imageSize
            ).apply {
                setMargins(imageMargin, imageMargin, imageMargin, imageMargin)
            }

            imageView.layoutParams = params
            imageView.adjustViewBounds = true
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP

            val imageDescription = """"""  // Descripción de imagen (si es necesario)
            imageView.setOnClickListener {
                // Suponiendo que tienes un método para obtener metadatos de la imagen
                val metadata = getMetadataForImage(imgFile) // Implementa esta función

                ImageDialogFragment.newInstance(
                    imgFile.absolutePath,
                    imgFile.name,
                    imageDescription,
                    metadata // Pasa los metadatos aquí
                ).show(childFragmentManager, "ImageDialog")
            }

            row?.addView(imageView)
        }
    }

    private fun getMetadataForImage(imgFile: File): Map<String, String> {
        // Aquí puedes cargar los metadatos usando ExifInterface u otro método
        val exif = ExifInterface(imgFile.absolutePath)
        return mapOf(
            "Tipo" to (exif.getAttribute(ExifInterface.TAG_USER_COMMENT) ?: "N/A"),
            "Substrates" to (exif.getAttribute(ExifInterface.TAG_ARTIST) ?: "N/A"),
            "Valor" to (exif.getAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION) ?: "N/A")
        )
    }

    private fun getImageFiles(): List<File> {
        val projectDir = projectName?.let { name ->
            File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), name)
        } ?: return emptyList()

         val albumDir = albumName?.let { name ->
            File(projectDir, name)
        } ?: return emptyList()

        return if (albumDir.exists()) {
            findImagesInDir(albumDir)
        } else {
            emptyList()
        }
    }

    private fun findImagesInDir(dir: File): List<File> {

        val imageFiles = mutableListOf<File>()
        val filesAndDirs = dir.listFiles() ?: return imageFiles

        for (file in filesAndDirs) {
            if (file.isDirectory) {
                imageFiles.addAll(findImagesInDir(file))
            } else if (file.isFile && (file.extension == "jpg" || file.extension == "jpeg" || file.extension == "png")) {
                imageFiles.add(file)
            }
        }
        return imageFiles
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        albumName = args.album.albumTitle
        albumViewModel = (activity as MainActivity).albumViewModel
        //setUpAlbumRecyclerView()
        projectViewModel = (activity as MainActivity).projectViewModel
        lifecycleScope.launch {
            projectName = projectViewModel.getProjectTitleById(args.album.projectId) ?: "Unknown Project"
            loadImages()
        }


        val albumNameTextView = binding.albumLbl
        albumNameTextView.text = albumName
        camaraBtn = binding.root.findViewById(R.id.photoBtn)
        camaraBtn.setOnClickListener {
            val intent = Intent(activity, FotografiaActivity::class.java).apply {
                putExtra("PROJECT_NAME",projectName)
                putExtra("ALBUM_NAME", albumName)
            }
            startActivity(intent)
        }

        binding.beforeBtn.setOnClickListener{
            findNavController().navigateUp()
        }

        //currentNote = args.note!!
        albumView = view
    }


}