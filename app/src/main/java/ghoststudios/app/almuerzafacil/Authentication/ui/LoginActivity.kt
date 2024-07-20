package ghoststudios.app.almuerzafacil.Authentication.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ghoststudios.app.almuerzafacil.HomeClient
import ghoststudios.app.almuerzafacil.HomeCompany
import ghoststudios.app.almuerzafacil.HomePage
import ghoststudios.app.almuerzafacil.ListCompanyLunches
import ghoststudios.app.almuerzafacil.ProviderType
import ghoststudios.app.almuerzafacil.databinding.ActivityLoginBinding
import ghoststudios.app.almuerzafacil.ui.theme.Lunch
import ghoststudios.app.almuerzafacil.ui.theme.User

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseRef = FirebaseDatabase.getInstance().getReference("users")
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
                                fetchData(loggedInUser, HomeCompany::class.java)
                            }else{
                                fetchData(loggedInUser,HomeClient::class.java)
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


    private fun fetchData(user:User,activityClass: Class<*> ) {
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (usersSnap in snapshot.children) {
                        val currentUser = usersSnap.getValue(User::class.java)
                        if(user.email == currentUser?.email){
                            LoadActivity(activityClass, currentUser!!)
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun showRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }



    private fun LoadActivity(activityClass: Class<*>,user:User){
        val homeIntent = Intent(this, activityClass).apply {
            putExtra("email", user.email)
            putExtra("name", user.name)
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