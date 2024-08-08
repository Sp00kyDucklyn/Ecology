package com.cursokotlin.eco

import android.os.Bundle
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
import com.cursokotlin.eco.databinding.FragmentAddBinding
import com.cursokotlin.eco.model.Album
import com.cursokotlin.eco.model.Project
import com.cursokotlin.eco.viewmodel.AlbumViewModel
import com.cursokotlin.eco.viewmodel.ProjectViewModel

class AddAlbum : Fragment(R.layout.fragment_add_album), MenuProvider {

    private var addAlbumBinding: FragmentAddBinding? = null
    private val binding get() = addAlbumBinding!!

    private lateinit var albumViewModel: AlbumViewModel
    private lateinit var albumProjectView: View

    private val args: AddAlbumArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        addAlbumBinding = FragmentAddBinding.inflate(inflater,container,false)
        binding.addNoteHeading.text ="Add Album"
        binding.photoBtn.setOnClickListener {
            saveProject(albumProjectView)
            findNavController().navigateUp()
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        albumViewModel = (activity as MainActivity).albumViewModel

        albumProjectView = view
    }


    private fun saveProject(view: View){
        val addAlbumTitle = binding.addNoteTitle.text.toString().trim()
        val coords = binding.addNoteDesc.text.toString().trim()
        val projectId = args.projectid.id

            if(addAlbumTitle.isNotEmpty()){
                val album = Album(0, projectId,addAlbumTitle,coords)
                albumViewModel.insertAlbum(album)

                Toast.makeText(albumProjectView.context, "Album Saved", Toast.LENGTH_SHORT).show()
                view.findNavController().popBackStack(R.id.homeFragment, false)

            }else{

                Toast.makeText(albumProjectView.context, "Plese enter album title", Toast.LENGTH_SHORT).show()

            }


    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_add_project,menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            R.id.saveMenu ->{
                saveProject(albumProjectView)
                true
            }
            else -> false

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        addAlbumBinding = null
    }

}