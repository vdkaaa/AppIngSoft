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
import com.google.firebase.storage.StorageReference
import ghoststudios.app.almuerzafacil.databinding.FragmentAddBinding
import ghoststudios.app.almuerzafacil.databinding.FragmentHomeBinding


class AddFragment : Fragment() {
    private var _binding : FragmentAddBinding? = null
    private val binding get()= _binding!!
    private var uri: Uri? = null
    private lateinit var storageRef : StorageReference
    private lateinit var  firebaseRef : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        firebaseRef = FirebaseDatabase.getInstance().getReference("launchs")

        binding.btnSendAlmuerzo.setOnClickListener {
            saveData()
        }

        return binding.root
    }

    private fun saveData() {
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
        binding.imgAddTest.setImageResource(R.drawable.tutorial)

        val pickedImage = registerForActivityResult(ActivityResultContracts.GetContent()){
            binding.imgAddTest.setImageURI(it)
            if(it != null){
                uri = it
            }

        }
        binding.pickImageBtn.setOnClickListener{
            pickedImage.launch("image/*")
        }


        if (nombreAlmuerzo.isNotEmpty() && descripcionAlmuerzo.isNotEmpty() && valorAlmuerzo != null) {
            val concatId = firebaseRef.push().key!!
            val almuerzo = Almuerzo(concatId, nombreAlmuerzo, descripcionAlmuerzo, valorAlmuerzo)

            firebaseRef.child(concatId).setValue(almuerzo)
                .addOnCompleteListener {
                    Toast.makeText(context, "Almuerzo enviado exitosamente", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }


    }
}