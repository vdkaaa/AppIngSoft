package ghoststudios.app.almuerzafacil

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import ghoststudios.app.almuerzafacil.databinding.ActivityListAlmuerzosEmpresaBinding

class ListAlmuerzosEmpresa : AppCompatActivity() {

    private lateinit var binding : ActivityListAlmuerzosEmpresaBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityListAlmuerzosEmpresaBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navController = findNavController(R.id.fragmentContainerView3)
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment,R.id.addFragment))

        setupActionBarWithNavController(navController, appBarConfiguration)
    }
}