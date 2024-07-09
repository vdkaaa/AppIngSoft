package ghoststudios.app.almuerzafacil

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ghoststudios.app.almuerzafacil.databinding.FragmentHomeBinding
import ghoststudios.app.almuerzafacil.ui.theme.Lunch
import ghoststudios.app.almuerzafacil.ui.theme.LunchAdapterClass

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseRef: DatabaseReference
    private lateinit var arrayOfLunches: ArrayList<Lunch>
    private lateinit var adapter: LunchAdapterClass

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_addFragment_to_homeFragment)
        }
        firebaseRef = FirebaseDatabase.getInstance().getReference("lunches")
        arrayOfLunches = arrayListOf()
        ShowOrderUserLunches()
        fetchData()

        adapter = LunchAdapterClass(arrayOfLunches) { show -> showMenu(show) }

        binding.recyclerViewAlmuerzosEmpresa.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewAlmuerzosEmpresa.adapter = adapter

        firebaseRef = FirebaseDatabase.getInstance().getReference("lunches")
        arrayOfLunches = arrayListOf()
        ShowOrderUserLunches()
        fetchData()

        adapter = LunchAdapterClass(arrayOfLunches) { show -> showMenu(show) }

        binding.recyclerViewAlmuerzosEmpresa.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewAlmuerzosEmpresa.adapter = adapter

        binding.btnVolver.setOnClickListener {
            val intent = Intent(context, ProvisionalLogIn::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    private fun showMenu(show: Boolean) {
        // Implementar lógica para mostrar u ocultar menú si es necesario
    }

    fun orderLunches() {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Agendar almuerzos")
        alertDialog.setMessage("¿Quieres agendar estos almuerzos?")
        alertDialog.setPositiveButton("Agendar") { _, _ ->
            adapter.scheduleOrder(requireContext())
        }
        alertDialog.setNegativeButton("Cancelar") { _, _ -> }
        alertDialog.show()
    }

    private fun fetchData() {
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayOfLunches.clear()
                if (snapshot.exists()) {
                    for (lunchesSnap in snapshot.children) {
                        val lunch = lunchesSnap.getValue(Lunch::class.java)
                        if (lunch != null) {
                            arrayOfLunches.add(lunch)
                        }
                    }
                }
                Toast.makeText(context, "Loading lunches: ${arrayOfLunches.size}", Toast.LENGTH_SHORT).show()
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun ShowOrderUserLunches() {
        binding.btnAlmuerzoPedidos.setOnClickListener {
            val intent = Intent(context, LunchesUser::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
