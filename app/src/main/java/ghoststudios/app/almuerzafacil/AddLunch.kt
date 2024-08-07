package ghoststudios.app.almuerzafacil

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isGone
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import ghoststudios.app.almuerzafacil.ui.theme.Lunch
import ghoststudios.app.almuerzafacil.ui.theme.MyToolbar

class AddLunch : AppCompatActivity() {

    private var uri: Uri? = null
    private lateinit var storageRef: StorageReference
    private lateinit var firebaseRef: DatabaseReference
    private lateinit var email :String
    private lateinit var uid : String
    private lateinit var  savebtn : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_lunch)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        MyToolbar().show(this, getString(R.string.toolbarTitleAddLunch), true)

        savebtn = findViewById(R.id.btnSaveLunch)
        email = intent.getStringExtra("email")!!
        uid = intent.getStringExtra("uid")!!

        firebaseRef = FirebaseDatabase.getInstance().getReference("lunches")
        storageRef = FirebaseStorage.getInstance().getReference("Images")
        findViewById<ImageView>(R.id.previewImageLunch).setImageResource(R.drawable.uploadimg)

        val pickedImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
            findViewById<ImageView>(R.id.previewImageLunch).setImageURI(it)
            if (it != null) {
                uri = it
            }
        }

        findViewById<Button>(R.id.btnPickLunchImage).setOnClickListener {
            pickedImage.launch("image/*")
        }
        findViewById<Button>(R.id.btnSaveLunch).setOnClickListener {
            savebtn.isEnabled = false
            saveData()
        }
    }

    private fun saveData() {
        val lunchId = firebaseRef.push().key!!

        val refName = findViewById<TextInputEditText>(R.id.edtLunchName)
        val refDescription = findViewById<TextInputEditText>(R.id.edtDescriptionLunch)
        val refPrice = findViewById<EditText>(R.id.edtPriceLunch)

        val lunchName = refName.text.toString()
        val lunchDescription = refDescription.text.toString()
        val lunchPriceText = refPrice.text.toString()
        val lunchPrice = lunchPriceText.toIntOrNull()


        if (lunchName.isEmpty()) {
            refName.error = "Escribe un nombre para el Almuerzo"

        } else {
            refName.error = null
        }

        if (lunchDescription.isEmpty()) {
            refDescription.error = "Escribe una descripción para el Almuerzo"
        } else {
            refDescription.error = null
        }

        if (lunchPrice == null) {
            refPrice.error = "Valor no válido para el Almuerzo"
        } else {
            refPrice.error = null
        }

        if (lunchName.isNotEmpty() && lunchDescription.isNotEmpty() && lunchPrice != null) {
            uri?.let {
                storageRef.child(lunchId).putFile(it)
                    .addOnSuccessListener { task ->
                        task.metadata!!.reference!!.downloadUrl.addOnSuccessListener { url ->
                            val imgUrl = url.toString()
                            val lunch = Lunch(lunchId, lunchName, lunchDescription, lunchPrice, imgUrl)

                            Toast.makeText(this, "Subiendo Almuerzo", Toast.LENGTH_SHORT).show()
                            firebaseRef.child(lunchId).setValue(lunch).addOnCompleteListener {
                                Toast.makeText(this, "Almuerzo guardado exitosamente", Toast.LENGTH_SHORT).show()
                                savebtn.isEnabled = true
                            }.addOnFailureListener {
                                Toast.makeText(this, "Algo salió mal ", Toast.LENGTH_SHORT)
                                    .show()
                                savebtn.isEnabled = true
                            }

                            val intent = Intent(this, HomeCompany::class.java)
                            startActivity(intent)
                        }
                    }
            }


        }
    }
}