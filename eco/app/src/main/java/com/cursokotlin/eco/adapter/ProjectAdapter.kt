package com.cursokotlin.eco.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cursokotlin.eco.HomeFragmentDirections
import com.cursokotlin.eco.R
import com.cursokotlin.eco.databinding.ItemAlbumBinding
import com.cursokotlin.eco.databinding.ListItemBinding
import com.cursokotlin.eco.model.Project
import com.cursokotlin.eco.viewmodel.ProjectViewModel

class ProjectAdapter: RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>(){

    class ProjectViewHolder(val itemBinding: ListItemBinding): RecyclerView.ViewHolder(itemBinding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Project>(){
        override fun areItemsTheSame(oldItem: Project, newItem: Project): Boolean {
            return oldItem.id == newItem.id &&
                    oldItem.projectDesc == newItem.projectDesc &&
                    oldItem.projectTitle == newItem.projectTitle
        }

        override fun areContentsTheSame(oldItem: Project, newItem: Project): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        return ProjectViewHolder(
            ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val currentProject = differ.currentList[position]

        holder.itemBinding.projectTitle.text = currentProject.projectTitle
        holder.itemBinding.projectDesc.text = currentProject.projectDesc

        holder.itemView.setOnClickListener{
            val direction = HomeFragmentDirections.actionHomeFragmentToProjectFragment()
            it.findNavController().navigate(direction)
        }
    }

    private fun showPopupMenu(view: View, project: Project, context: Context) {
        PopupMenu(view.context, view).apply {
            menuInflater.inflate(R.menu.project_menu, menu)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_Delete -> {
                        deleteProject(project, context)
                        true
                    }
                    R.id.menu_Edit -> {
                        editProject(project, context)
                        true
                    }
                    else -> false
                }
            }
            show()
        }
    }

    private fun editProject(project: Project, context: Context) {

    }

    private fun deleteProject(project: Project, context: Context){

    }

}