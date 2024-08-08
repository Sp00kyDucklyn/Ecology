package com.cursokotlin.eco

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class ImageDialogFragment : DialogFragment() {
    companion object {
        private const val ARG_IMAGE_PATH = "imagePath"
        private const val ARG_IMAGE_NAME = "imageName"
        private const val ARG_IMAGE_DESCRIPTION = "imageDescription"
        fun newInstance(imagePath: String, imageName: String,imageDescription: String): ImageDialogFragment {
            val fragment = ImageDialogFragment()
            val args = Bundle().apply {
                putString(ARG_IMAGE_PATH, imagePath)
                putString(ARG_IMAGE_NAME, imageName)
                putString(ARG_IMAGE_DESCRIPTION, imageDescription)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.TransparentDialog) // Aplica el estilo personalizado
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_image, container, false)
        val closeButton = view.findViewById<Button>(R.id.closeButton)
        val imagePath = arguments?.getString(ARG_IMAGE_PATH)
        val imageName = arguments?.getString(ARG_IMAGE_NAME)
        val imageDescription = arguments?.getString(ARG_IMAGE_DESCRIPTION)
        val imageView = view.findViewById<ImageView>(R.id.imageView)
        val imageNameTextView = view.findViewById<TextView>(R.id.imageNameTextView)
        val imageDescriptionTextView = view.findViewById<TextView>(R.id.dialogDescription)

        if (imagePath != null) {
            val bitmap = BitmapFactory.decodeFile(imagePath)
            imageView.setImageBitmap(bitmap)
        }
        imageNameTextView.text = imageName
        // Puedes establecer una descripción larga aquí si es necesario
        imageDescriptionTextView.text = imageDescription ?: "Descripción no disponible"
        closeButton.setOnClickListener {
            dismiss() // Cierra el diálogo cuando se toca el botón
        }
        return view
    }
}