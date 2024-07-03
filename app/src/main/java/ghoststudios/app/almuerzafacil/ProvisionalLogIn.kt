package ghoststudios.app.almuerzafacil

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ProvisionalLogIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_provisional_log_in)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var btnCompany = findViewById<Button>(R.id.buttonEntrarEmp)
        var btnClient = findViewById<Button>(R.id.buttonEntrarCliente)

        btnCompany.setOnClickListener{
            val intent = Intent(this, ListAlmuerzosEmpresa::class.java)
            startActivity(intent)
        }
        btnClient.setOnClickListener{
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
        }

    }
}