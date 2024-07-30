package com.cursokotlin.eco

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.cursokotlin.eco.databinding.FragmentAlbumBinding
import com.cursokotlin.eco.databinding.FragmentProjectBinding
import com.cursokotlin.eco.viewmodel.AlbumViewModel

class AlbumFragment : Fragment() {
    private var albumBinding: FragmentAlbumBinding? = null
    private val binding get() = albumBinding!!

    private lateinit var albumViewModel: AlbumViewModel
    private lateinit var albumView: View


    private val args: AlbumFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        albumBinding =  FragmentAlbumBinding.inflate(inflater, container, false)
       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        albumViewModel = (activity as MainActivity).albumViewModel
        //setUpAlbumRecyclerView()
        val albumName = args.album.albumTitle
        val albumNameTextView = binding.albumLbl
        albumNameTextView.text = albumName


        binding.beforeBtn.setOnClickListener{
            //view.findNavController().popBackStack(R.id.projectFragment,false)
           // val directions = AlbumFragmentDirections.actionAlbumFragmentToProjectFragment(albumName)
           // it.findNavController().navigate(directions)
        }

        //currentNote = args.note!!
        albumView = view
    }


}