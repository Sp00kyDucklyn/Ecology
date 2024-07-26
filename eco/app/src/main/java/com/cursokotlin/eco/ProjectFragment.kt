package com.cursokotlin.eco

import android.app.AlertDialog
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
import androidx.navigation.fragment.navArgs
import com.cursokotlin.eco.databinding.FragmentProjectBinding
import com.cursokotlin.eco.model.Project
import com.cursokotlin.eco.viewmodel.ProjectViewModel

class ProjectFragment : Fragment(R.layout.fragment_project) {

    private var projectBinding: FragmentProjectBinding? = null
    private val binding get() = projectBinding!!

    private lateinit var  projectViewModel: ProjectViewModel
    private lateinit var projectView: View
    private lateinit var currentProject: Project

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        projectBinding = FragmentProjectBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        projectViewModel = (activity as MainActivity).projectViewModel
        //currentNote = args.note!!
        projectView = view

    }

    override fun onDestroy() {
        super.onDestroy()
        projectBinding = null
    }


}