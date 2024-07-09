package ghoststudios.app.almuerzafacil.Authentication.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import ghoststudios.app.almuerzafacil.HomePage
import ghoststudios.app.almuerzafacil.ListCompanyLunches
import ghoststudios.app.almuerzafacil.ProviderType
import ghoststudios.app.almuerzafacil.databinding.ActivityLoginBinding
import ghoststudios.app.almuerzafacil.ui.theme.User

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setup()
    }

    private fun setup() {
        binding.btnLogin.setOnClickListener {
            if (binding.emailEditTextLogin.text.isNotEmpty() && binding.passwordEditTextLogin.text.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(
                        binding.emailEditTextLogin.text.toString(),
                        binding.passwordEditTextLogin.text.toString()
                    )
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val user = it.result.user
                            val loggedInUser = User(
                                id = user?.uid,
                                email = user?.email,
                                password = binding.passwordEditTextLogin.text.toString()
                            )

                            if(binding.emailEditTextLogin.text.toString() == "samquiroz385@gmail.com"){
                                LoadActivity(ListCompanyLunches::class.java, loggedInUser)
                            }else{
                                LoadActivity(HomePage::class.java, loggedInUser)
                            }


                        } else {
                            showAlert()
                        }
                    }
            } else {
                showEmptyInformation()
            }
        }

        binding.btnSignIn.setOnClickListener {
            showRegister()
        }
    }

    private fun showRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }



    private fun LoadActivity(activityClass: Class<*>,user:User){
        val homeIntent = Intent(this, activityClass).apply {
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

    private fun showEmptyInformation() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Verifica que todos los campos estén completados")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
