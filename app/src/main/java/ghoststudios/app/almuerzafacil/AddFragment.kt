package ghoststudios.app.almuerzafacil

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import ghoststudios.app.almuerzafacil.databinding.FragmentAddBinding
import ghoststudios.app.almuerzafacil.databinding.FragmentHomeBinding


class AddFragment : Fragment() {
    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    private var uri: Uri? = null
    private lateinit var storageRef: StorageReference
    private lateinit var firebaseRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        firebaseRef = FirebaseDatabase.getInstance().getReference("launchs")
        storageRef = FirebaseStorage.getInstance().getReference("Images")
        binding.imgLunch.setImageResource(R.drawable.tutorial)

        val pickedImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
            binding.imgLunch.setImageURI(it)
            if (it != null) {
                uri = it
            }

        }
        binding.btnpickLunchImage.setOnClickListener {
            pickedImage.launch("image/*")
        }
        binding.btnSendAlmuerzo.setOnClickListener {
            saveData()
        }

        return binding.root
    }

    private fun saveData() {
        val lunchId = firebaseRef.push().key!!
        val nombreAlmuerzo = binding.edtNA.text.toString()
        val descripcionAlmuerzo = binding.edtDA.text.toString()
        val valorAlmuerzoText = binding.edtValor.text.toString()
        val valorAlmuerzo = valorAlmuerzoText.toIntOrNull()




        if (nombreAlmuerzo.isEmpty()) {
            binding.edtNombreAlmuerzo.error = "Escribe un nombre para el Almuerzo"

        } else {
            binding.edtNombreAlmuerzo.error = null
        }

        if (descripcionAlmuerzo.isEmpty()) {
            binding.edtDescripcionAlmuerzo.error = "Escribe una descripción para el Almuerzo"
        } else {
            binding.edtDescripcionAlmuerzo.error = null
        }

        if (valorAlmuerzo == null) {
            binding.edtValor.error = "Valor no válido para el Almuerzo"
        } else {
            binding.edtValor.error = null
        }

        if (nombreAlmuerzo.isNotEmpty() && descripcionAlmuerzo.isNotEmpty() && valorAlmuerzo != null) {
            uri?.let {
                storageRef.child(lunchId).putFile(it)
                    .addOnSuccessListener { task ->
                        task.metadata!!.reference!!.downloadUrl.addOnSuccessListener { url ->
                            val imgUrl = url.toString()
                            val lunch = Lunch(lunchId, nombreAlmuerzo, descripcionAlmuerzo, valorAlmuerzo, imgUrl)

                            Toast.makeText(context, "image stored", Toast.LENGTH_SHORT).show()
                            firebaseRef.child(lunchId).setValue(lunch).addOnCompleteListener {
                                Toast.makeText(context, "data stored", Toast.LENGTH_SHORT).show()
                            }.addOnFailureListener {
                                Toast.makeText(context, "data failed to store ", Toast.LENGTH_SHORT)
                                    .show()
                            }


                        }
                    }
            }


        }
    }
}