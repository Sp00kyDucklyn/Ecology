package com.cursokotlin.eco

import android.app.AlertDialog
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cursokotlin.eco.adapter.AlbumAdapter
import com.cursokotlin.eco.databinding.FragmentProjectBinding
import com.cursokotlin.eco.model.Album
import com.cursokotlin.eco.model.Project
import com.cursokotlin.eco.viewmodel.AlbumViewModel
import com.cursokotlin.eco.viewmodel.ProjectViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class ProjectFragment : Fragment(R.layout.fragment_project) {

    private var projectBinding: FragmentProjectBinding? = null
    private val binding get() = projectBinding!!

    private lateinit var  projectViewModel: ProjectViewModel
    private lateinit var albumViewModel: AlbumViewModel
    private lateinit var albumAdapter: AlbumAdapter
    private lateinit var projectView: View
    private lateinit var comprimirBtn: FloatingActionButton

    private val args: ProjectFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment¿

        projectBinding = FragmentProjectBinding.inflate(inflater, container, false)


        return binding.root
    }
    private fun compressFilesToZip(sourceDir: File, zipFile: File) {
        try {
            val fos = FileOutputStream(zipFile)
            val zos = ZipOutputStream(fos)

            // Función recursiva para añadir archivos y carpetas al ZIP
            fun addFolderToZip(folder: File, parentPath: String) {
                for (file in folder.listFiles() ?: emptyArray()) {
                    val zipEntryName = parentPath + file.name
                    if (file.isDirectory) {
                        // Añadir la carpeta al ZIP
                        zos.putNextEntry(ZipEntry(zipEntryName + "/"))
                        zos.closeEntry()
                        // Llamada recursiva para añadir el contenido de la carpeta
                        addFolderToZip(file, "$zipEntryName/")
                    } else {
                        // Añadir el archivo al ZIP
                        FileInputStream(file).use { fis ->
                            zos.putNextEntry(ZipEntry(zipEntryName))
                            val buffer = ByteArray(1024)
                            var length: Int
                            while (fis.read(buffer).also { length = it } >= 0) {
                                zos.write(buffer, 0, length)
                            }
                            zos.closeEntry()
                        }
                    }
                }
            }

            addFolderToZip(sourceDir, "")

            zos.close()
            fos.close()

            Toast.makeText(requireContext(), "Archivos comprimidos en ${zipFile.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Error al comprimir archivos", Toast.LENGTH_SHORT).show()
        }
    }
    private fun compressImages(folderPath: String) {
        val projectName = folderPath // Define el nombre de la carpeta del proyecto
        val storageDir = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), projectName)

        if (storageDir.exists() && storageDir.isDirectory) {
            val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
            val dateTime = dateFormat.format(Date())

            // Crear el nombre del archivo ZIP con el nombre del proyecto, la fecha y la hora
            val zipFileName = "${storageDir.name}_$dateTime.zip"
            val zipFile = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), zipFileName)

            compressFilesToZip(storageDir, zipFile)
        } else {
            Toast.makeText(requireContext(), "Carpeta inexistente", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //projectViewModel = (activity as MainActivity).projectViewModel
        albumViewModel = (activity as MainActivity).albumViewModel



        val projectName = args.project.projectTitle
        val projectNameTextView = binding.projectLbl
        projectNameTextView.text = projectName

        comprimirBtn = binding.root.findViewById(R.id.comprimirBtn)
        binding.beforeBtn.setOnClickListener{
            findNavController().navigateUp()
        }
        binding.photoBtn.setOnClickListener{
            val action =ProjectFragmentDirections.actionProjectFragmentToAddAlbum(args.project)
            it.findNavController().navigate(action)
           // it.findNavController().navigate(R.id.action_projectFragment_to_addAlbum)
        }
        //currentNote = args.note!!
        comprimirBtn.setOnClickListener {
            compressImages(projectName)
        }
        //
        setUpAlbumRecyclerView()
        projectView = view

    }

    private fun setUpAlbumRecyclerView() {

        val project = args.project 
        albumAdapter = AlbumAdapter(project)
        binding.mRecycler.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = albumAdapter
        }

        activity?.let {
            albumViewModel.getAlbumsByProjectId(project.id).observe(viewLifecycleOwner) { albums ->
                albumAdapter.differ.submitList(albums)
                updateUI(albums)
            }
        }

    }

    private fun updateUI(album: List<Album>?) {
        print("popo entro a este metodo UI")
        if(album !=null){
            if(album.isNotEmpty()){
                binding.mRecycler.visibility = View.VISIBLE
            }else{
                binding.mRecycler.visibility = View.GONE
            }
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        projectBinding = null
    }


}