package ghoststudios.app.almuerzafacil

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import ghoststudios.app.almuerzafacil.ui.theme.Lunch
import ghoststudios.app.almuerzafacil.ui.theme.MyToolbar

class EditLunchActivity : AppCompatActivity() {
    private lateinit var firebaseRef: DatabaseReference
    private lateinit var lunch: Lunch
    private lateinit var lunchNameEditText: EditText
    private lateinit var lunchDescriptionEditText: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_lunch)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //MyToolbar().show(this, getString(R.string.toolbarTitleEditLunch), true)

        lunch = intent.getParcelableExtra("lunch")!!
        firebaseRef = FirebaseDatabase.getInstance().getReference("lunches").child(lunch.id!!)

        lunchNameEditText = findViewById(R.id.lunchNameEditText)
        lunchDescriptionEditText = findViewById(R.id.lunchDescriptionEditText)
        saveButton = findViewById(R.id.saveButton)

        lunchNameEditText.setText(lunch.name)
        lunchDescriptionEditText.setText(lunch.description)

        saveButton.setOnClickListener {
            saveLunchDetails()
        }
    }

    private fun saveLunchDetails() {
        val updatedName = lunchNameEditText.text.toString()
        val updatedDescription = lunchDescriptionEditText.text.toString()

        if (updatedName.isNotEmpty() && updatedDescription.isNotEmpty()) {
            val updatedLunch = lunch.copy(name = updatedName, description = updatedDescription)
            firebaseRef.setValue(updatedLunch).addOnSuccessListener {
                Toast.makeText(this, "Lunch details updated successfully", Toast.LENGTH_SHORT).show()
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to update lunch details", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
        }
    }
}
