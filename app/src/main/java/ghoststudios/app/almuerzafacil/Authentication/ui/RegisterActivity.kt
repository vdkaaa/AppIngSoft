package ghoststudios.app.almuerzafacil.Authentication.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import ghoststudios.app.almuerzafacil.HomePage
import ghoststudios.app.almuerzafacil.ProviderType
import ghoststudios.app.almuerzafacil.databinding.ActivityRegisterBinding
import ghoststudios.app.almuerzafacil.ui.theme.User

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setup()
    }

    private fun setup() {
        title = "Registro"
        binding.btnRegister.setOnClickListener {
            if (binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()) {
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
                                email = user?.email,
                                password = binding.passwordEditText.text.toString()
                            )
                            showHomeClient(newUser)
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

    private fun showHomeClient(user: User) {
        val homeIntent = Intent(this, HomePage::class.java).apply {
            putExtra("email", user.email)
            putExtra("provider", ProviderType.BASIC.name)
            putExtra("uid", user.id)
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
