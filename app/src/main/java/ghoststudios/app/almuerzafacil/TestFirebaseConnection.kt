package ghoststudios.app.almuerzafacil

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import ghoststudios.app.almuerzafacil.databinding.ActivityTestFirebaseConnectionBinding
import ghoststudios.app.almuerzafacil.ui.theme.Lunch

class TestFirebaseConnection : AppCompatActivity() {

    private lateinit var binding : ActivityTestFirebaseConnectionBinding
    private lateinit var firebaseRef:DatabaseReference
    private lateinit var storageRef :StorageReference
    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_test_firebase_connection)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setUpFirebase()



    }
    private fun setUpFirebase(){
        firebaseRef = FirebaseDatabase.getInstance().getReference("test")
        storageRef = FirebaseStorage.getInstance().getReference("Images")

        binding = ActivityTestFirebaseConnectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.imgAddTest.setImageResource(R.drawable.tutorial)

        val pickedImage = registerForActivityResult(ActivityResultContracts.GetContent()){
            //binding.imgAddTest.setImageURI(it)
            if(it != null){
                uri = it
            }

        }
        //binding.pickImageBtn.setOnClickListener{
          //  pickedImage.launch("image/*")
        //}

        binding.homeBtn.setOnClickListener{
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
        }


        //Send data to the realtime database
        binding.sendDataBtn.setOnClickListener{

            val lunchId = firebaseRef.push().key!!
            //val lunch = Lunch(lunchId,"Pan con pan","Descripción del pan con pan")
            var lunch : Lunch

            uri?.let {
                storageRef.child(lunchId).putFile(it)
                    .addOnSuccessListener { task->
                        task.metadata!!.reference!!.downloadUrl.addOnSuccessListener { url->
                            Toast.makeText(this, "image stored", Toast.LENGTH_SHORT).show()

                            val imgUrl = url.toString()

                            lunch = Lunch(lunchId,"Pan con pan","Descripción del pan con pan",100, imgUrl)

                            firebaseRef.child(lunchId).setValue(lunch).addOnCompleteListener{
                                Toast.makeText(this, "data stored", Toast.LENGTH_SHORT).show()
                            }.addOnFailureListener{
                                Toast.makeText(this, "data failed to store ", Toast.LENGTH_SHORT).show()
                            }

                        }
                    }
            }
        }
    }
}