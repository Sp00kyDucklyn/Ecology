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
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cursokotlin.eco.adapter.AlbumAdapter
import com.cursokotlin.eco.databinding.FragmentProjectBinding
import com.cursokotlin.eco.model.Album
import com.cursokotlin.eco.model.Project
import com.cursokotlin.eco.viewmodel.AlbumViewModel
import com.cursokotlin.eco.viewmodel.ProjectViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProjectFragment : Fragment(R.layout.fragment_project) {

    private var projectBinding: FragmentProjectBinding? = null
    private val binding get() = projectBinding!!

    private lateinit var  projectViewModel: ProjectViewModel
    private lateinit var albumViewModel: AlbumViewModel
    private lateinit var albumAdapter: AlbumAdapter
    private lateinit var projectView: View

    private val args: ProjectFragmentArgs by navArgs()

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

        //projectViewModel = (activity as MainActivity).projectViewModel
        albumViewModel = (activity as MainActivity).albumViewModel



        val projectName = args.project.projectTitle
        val projectNameTextView = binding.projectLbl
        projectNameTextView.text = projectName


        binding.photoBtn.setOnClickListener{
            it.findNavController().navigate(R.id.action_projectFragment_to_addAlbum)
        }
        //currentNote = args.note!!

        setUpAlbumRecyclerView()
        projectView = view

    }

    private fun setUpAlbumRecyclerView() {
        print("popo entro a este metodo")
        albumAdapter = AlbumAdapter()
        binding.mRecycler.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = albumAdapter
        }

        activity?.let {
            albumViewModel.getAllAlbum().observe(viewLifecycleOwner){album ->
                albumAdapter.differ.submitList(album)
                updateUI(album)
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