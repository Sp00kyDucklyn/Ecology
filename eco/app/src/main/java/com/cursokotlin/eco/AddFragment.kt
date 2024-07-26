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
import com.cursokotlin.eco.databinding.FragmentAddBinding
import com.cursokotlin.eco.model.Project
import com.cursokotlin.eco.viewmodel.ProjectViewModel

class AddFragment : Fragment(R.layout.fragment_add), MenuProvider {

    private var addProjectBinding: FragmentAddBinding? = null
    private val binding get() = addProjectBinding!!

    private lateinit var projectViewModel: ProjectViewModel
    private lateinit var addProjectView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        addProjectBinding = FragmentAddBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        projectViewModel = (activity as MainActivity).projectViewModel
        addProjectView = view
    }


    private fun saveProject(view: View){
        val addProjectTitle = binding.addNoteTitle.text.toString().trim()
        val addNoteDesc = binding.addNoteDesc.text.toString().trim()

        if(addProjectTitle.isNotEmpty()){
            val project = Project(0,addProjectTitle,addNoteDesc)
            projectViewModel.insertProject(project)

            Toast.makeText(addProjectView.context, "Project Saved", Toast.LENGTH_SHORT).show()
            view.findNavController().popBackStack(R.id.homeFragment, false)

        }else{

            Toast.makeText(addProjectView.context, "Plese enter note title", Toast.LENGTH_SHORT).show()

        }

    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_add_project,menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            R.id.saveMenu ->{
                saveProject(addProjectView)
                true
            }
            else -> false

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        addProjectBinding = null
    }

}