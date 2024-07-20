package ghoststudios.app.almuerzafacil.Authentication.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import ghoststudios.app.almuerzafacil.HomeClient
import ghoststudios.app.almuerzafacil.ProviderType
import ghoststudios.app.almuerzafacil.databinding.ActivityRegisterBinding
import ghoststudios.app.almuerzafacil.ui.theme.User

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseRef = FirebaseDatabase.getInstance().getReference("users")


        setup()
    }

    private fun setup() {
        binding.btnRegister.setOnClickListener {
            if (binding.nameEditText.text.isNotEmpty() &&
                binding.lastnameEditText.text.isNotEmpty() &&
                binding.emailEditText.text.isNotEmpty() &&
                binding.passwordEditText.text.isNotEmpty() &&
                binding.editTextPhone.text.isNotEmpty()) {

                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(
                        binding.emailEditText.text.toString(),
                        binding.passwordEditText.text.toString()
                    )
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val user = it.result.user
                            val newUser = User(
                                id = user?.uid,
                                name = binding.nameEditText.text.toString(),
                                lastname = binding.lastnameEditText.text.toString(),
                                email = user?.email,
                                password = binding.passwordEditText.text.toString(),
                                phone = binding.editTextPhone.text.toString().toInt()
                            )
                            saveUsertoFireBase(newUser)
                        } else {
                            showAlert()
                        }
                    }
            }
        }
        binding.btnBack.setOnClickListener {
            showLogin()
        }
    }

    private fun saveUsertoFireBase(user: User) {

        firebaseRef.child(user.id!!).setValue(user).addOnCompleteListener {
            Toast.makeText(this, "data stored", Toast.LENGTH_SHORT).show()

        }.addOnFailureListener {
            Toast.makeText(this, "data failed to store ", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun showHomeClient(user: User) {
        val homeIntent = Intent(this, HomeClient::class.java).apply {
            putExtra("name", user.name)
            putExtra("lastname", user.lastname)
            putExtra("email", user.email)
            putExtra("provider", ProviderType.BASIC.name)
            putExtra("uid", user.id)
            putExtra("phone", user.phone)
        }
        startActivity(homeIntent)
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showLogin() {
        val loginActivity = Intent(this, LoginActivity::class.java)
        startActivity(loginActivity)
    }
}