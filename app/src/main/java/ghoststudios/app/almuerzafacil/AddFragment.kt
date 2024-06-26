package ghoststudios.app.almuerzafacil

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import ghoststudios.app.almuerzafacil.databinding.FragmentAddBinding
import ghoststudios.app.almuerzafacil.databinding.FragmentHomeBinding


class AddFragment : Fragment() {
    private var _binding : FragmentAddBinding? = null
    private val binding get()= _binding!!

    private lateinit var  firebaseRef : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        firebaseRef = FirebaseDatabase.getInstance().getReference("launchs")

        binding.btnSendAlmuerzo.setOnClickListener{
            println("a")
            saveData()

        }


        return binding.root
    }

    private fun saveData() {
        println("b")
        val nombreAlmuerzo = binding.edtNA.text.toString()
        val descripcionAlmuerzo = binding.edtDA.text.toString()
        val valorAlmuerzoText = binding.edtValor.text.toString()
        val valorAlmuerzo = valorAlmuerzoText.toIntOrNull()
        println(nombreAlmuerzo)
        println(descripcionAlmuerzo)
        println(valorAlmuerzoText)
        if(nombreAlmuerzo.isEmpty()) {
            binding.edtNombreAlmuerzo.error = "Escribe un nombre para el Almuerzo" // AGREGAR A LOS STRINGS DPS
        }
        if(descripcionAlmuerzo.isEmpty()) {
            binding.edtDescripcionAlmuerzo.error = "Escribe una descripción para el Almuerzo" // AGREGAR A LOS STRINGS DPS

        }

        if (valorAlmuerzo == null) {
            binding.edtValorAlmuerzo.error = "Valor no válido para el Almuerzo"// AGREGAR A LOS STRINGS DPS
        }
        val concatId = firebaseRef.push().key!!
        val almuerzo = Almuerzo(concatId, nombreAlmuerzo, descripcionAlmuerzo, valorAlmuerzo)
        println("d")
        firebaseRef.child(concatId).setValue(almuerzo)
            .addOnCompleteListener{
                println("listenerOnComplete")
                Toast.makeText(context, " Almuerzo enviado exitosamente", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                println("listenerOnDFail")
                Toast.makeText(context, " Error ${it.message}", Toast.LENGTH_SHORT).show()
            }.addOnCanceledListener{
                println("listenercancel")
            }


    }


}