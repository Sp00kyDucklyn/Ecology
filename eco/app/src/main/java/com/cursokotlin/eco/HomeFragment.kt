package com.cursokotlin.eco

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cursokotlin.eco.adapter.ProjectAdapter
import com.cursokotlin.eco.databinding.FragmentHomeBinding
import com.cursokotlin.eco.model.Project
import com.cursokotlin.eco.viewmodel.ProjectViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


class HomeFragment : Fragment(R.layout.fragment_home), SearchView.OnQueryTextListener, MenuProvider {


    private var homeBinding: FragmentHomeBinding? = null
    private val binding get() = homeBinding!!

    private lateinit var projectViewModel: ProjectViewModel
    private lateinit var projectAdapter: ProjectAdapter




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)

        //btnOpenCameraActivity = binding.root.findViewById(R.id.button)

       // btnOpenCameraActivity.setOnClickListener {
         //   val intent = Intent(activity, FotografiaActivity::class.java)
           // startActivity(intent)
       // }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        projectViewModel = (activity as MainActivity).projectViewModel
        setUpHomeRecyclerView()

    }

    private fun setUpHomeRecyclerView() {

      projectAdapter = ProjectAdapter()
        binding.homeRecyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = projectAdapter
        }

        activity?.let {
            projectViewModel.getAllProjects().observe(viewLifecycleOwner){project ->
                projectAdapter.differ.submitList(project)
                updateUI(project)
            }
        }
    }

    private fun updateUI(project: List<Project>?) {
        if(project !=null){
            if(project.isNotEmpty()){
                binding.homeRecyclerView.visibility = View.VISIBLE
            }else{
                binding.homeRecyclerView.visibility = View.GONE
            }
        }

    }

    private fun searchProject(query: String?){
        val searchQuery = "%$query"

        projectViewModel.searchProjects(searchQuery).observe(this){list ->
           projectAdapter.differ.submitList(list)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(newText != null){
            searchProject(newText)
        }
        return true
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        val menuSearch = menu.findItem(R.id.searchView).actionView as SearchView
        menuSearch.isSubmitButtonEnabled = false
        menuSearch.setOnQueryTextListener(this)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        homeBinding = null
    }

}