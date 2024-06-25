package ghoststudios.app.almuerzafacil

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import ghoststudios.app.almuerzafacil.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding :FragmentHomeBinding? = null
    private val binding get()= _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.btnAdd.setOnClickListener{
            findNavController().navigate(R.id.action_addFragment_to_homeFragment)
        }



        return binding.root
    }


}