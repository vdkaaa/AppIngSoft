package ghoststudios.app.almuerzafacil

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ghoststudios.app.almuerzafacil.ui.theme.User
import java.util.Calendar

class HomeClient : AppCompatActivity() {
    private lateinit var uidI: String
    private lateinit var nameI: String
    private lateinit var lastnameI: String
    private lateinit var emailI: String
    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_client)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtén SharedPreferences
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        // Obtén los extras del intent de manera segura y proporciona valores predeterminados
        emailI = intent.getStringExtra("email") ?: sharedPreferences.getString("email", "email@example.com")!!
        uidI = intent.getStringExtra("uid") ?: sharedPreferences.getString("uid", "default_uid")!!
        nameI = intent.getStringExtra("name") ?: sharedPreferences.getString("name", "Usuario")!!
        lastnameI = intent.getStringExtra("lastname") ?: sharedPreferences.getString("lastname", "Apellido")!!

        // Guarda los datos obtenidos en SharedPreferences
        sharedPreferences.edit().apply {
            putString("email", emailI)
            putString("uid", uidI)
            putString("name", nameI)
            putString("lastname", lastnameI)
            apply()
        }

        // Inicializa el usuario
        user = User(id = uidI, name = nameI, lastname = lastnameI, email = emailI, phone = null)

        // Agrega el log para verificar los datos del usuario
        Log.d("HomeClient", "User Data - ID: ${user?.id}, Name: ${user?.name}, Lastname: ${user?.lastname}, Email: ${user?.email}, Phone: ${user?.phone}")

        findViewById<Button>(R.id.btn_orderLunch).setOnClickListener {
            val intent = Intent(this, ChooseDayOfTheWeek::class.java)
            startActivityForResult(intent, REQUEST_CODE_CHOOSE_DAY)
        }

        val txtUser = findViewById<TextView>(R.id.tv_HomeClientTitle)
        txtUser.text = "Bienvenid@ $nameI,\n¿Qué deseas pedir hoy?"

        findViewById<Button>(R.id.btn_seeOrderLunch).setOnClickListener {
            val intent = Intent(this, OrdersOfClient::class.java).apply {
                putExtra("email", emailI)
                putExtra("uid", uidI)
                putExtra("name", nameI)
            }
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_CHOOSE_DAY -> {
                    val selectedDayOfWeek = data?.getIntExtra("selected_day_of_week", -1)
                    if (selectedDayOfWeek != null && selectedDayOfWeek != -1) {
                        val dayName = getDayName(selectedDayOfWeek)
                        println("ClientSelectLunches day get $dayName, num $selectedDayOfWeek")

                        val intent = Intent(this, ClientSelectLunches::class.java).apply {
                            putExtra("dayOfWeek", selectedDayOfWeek)
                            putExtra("email", emailI)
                            putExtra("uid", uidI)
                            putExtra("name", nameI)
                        }
                        startActivity(intent)
                    }
                }
                REQUEST_CODE_CHOOSE_DAY_SEE_ORDER -> {
                    val selectedDayOfWeek = data?.getIntExtra("selected_day_of_week", -1)
                    if (selectedDayOfWeek != null && selectedDayOfWeek != -1) {
                        val dayName = getDayName(selectedDayOfWeek)
                        println("OrdersOfClient day get $dayName, num $selectedDayOfWeek")

                        val intent = Intent(this, OrdersOfClient::class.java).apply {
                            putExtra("dayOfWeek", selectedDayOfWeek)
                            putExtra("email", emailI)
                            putExtra("uid", uidI)
                            putExtra("name", nameI)
                        }
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun getDayName(dayOfWeek: Int): String {
        return when (dayOfWeek) {
            Calendar.MONDAY -> "Lunes"
            Calendar.TUESDAY -> "Martes"
            Calendar.WEDNESDAY -> "Miércoles"
            Calendar.THURSDAY -> "Jueves"
            Calendar.FRIDAY -> "Viernes"
            Calendar.SATURDAY -> "Sábado"
            Calendar.SUNDAY -> "Domingo"
            else -> "Desconocido"
        }
    }

    companion object {
        private const val REQUEST_CODE_CHOOSE_DAY = 1
        private const val REQUEST_CODE_CHOOSE_DAY_SEE_ORDER = 2
    }
}
