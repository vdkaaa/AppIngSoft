package ghoststudios.app.almuerzafacil

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import ghoststudios.app.almuerzafacil.databinding.ActivityTestFirebaseConnectionBinding

class TestFirebaseConnection : AppCompatActivity() {

    lateinit var binding : ActivityTestFirebaseConnectionBinding
    lateinit var firebaseRef:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_test_firebase_connection)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        firebaseRef =FirebaseDatabase.getInstance().getReference("test")
        binding = ActivityTestFirebaseConnectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sendDataText.setOnClickListener{
            firebaseRef.setValue("Toma p...").addOnCompleteListener(){
                Toast.makeText(this, "data stored", Toast.LENGTH_SHORT).show()
            }


        }

    }
}