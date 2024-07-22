package ghoststudios.app.almuerzafacil

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import ghoststudios.app.almuerzafacil.ui.theme.PedidosEmpresa
import java.util.Calendar
import androidx.navigation.fragment.findNavController
import ghoststudios.app.almuerzafacil.ui.theme.User

class HomeCompany : AppCompatActivity() {
    private lateinit var uidI: String
    private lateinit var nameI: String
    private lateinit var lastnameI: String
    private lateinit var emailI: String
    private var user: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()

        setContentView(R.layout.activity_home_company)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtén los extras del intent de manera segura y proporciona valores predeterminados
        emailI = intent.getStringExtra("email") ?: "email@example.com"
        uidI = intent.getStringExtra("uid") ?: "default_uid"
        nameI = intent.getStringExtra("name") ?: "Usuario"
        lastnameI = intent.getStringExtra("lastname") ?: "Apellido"

        // Inicializa el usuario
        user = User(id = uidI, name = nameI, lastname = lastnameI, email = emailI, phone = null)

        findViewById<Button>(R.id.BtnCreateLunchHomeComany).setOnClickListener{
            val intent = Intent(this, AddLunch::class.java).apply {
                putExtra("email", emailI)
                putExtra("uid", uidI)
                putExtra("name", nameI)
            }

            startActivity(intent)
        }
        findViewById<Button>(R.id.BtnUploadLunchesHomeCompany).setOnClickListener{
            val intent = Intent(this, ChooseDayOfTheWeek::class.java)
            startActivityForResult(intent, REQUEST_CODE_CHOOSE_DAY_UPLOAD_LUNCH)
        }
        findViewById<Button>(R.id.BtnSeePendingOrdersHomeCompany).setOnClickListener{
            val intent = Intent(this, PedidosEmpresa::class.java)
            startActivity(intent)
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {

            if(requestCode == REQUEST_CODE_CHOOSE_DAY_UPLOAD_LUNCH){
                val selectedDayOfWeek = data?.getIntExtra("selected_day_of_week", -1)
                if (selectedDayOfWeek != null && selectedDayOfWeek != -1) {
                    val dayName = getDayName(selectedDayOfWeek)
                    Toast.makeText(this, "Selected Day: $dayName", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, SeeCompanyLunchesDayOfTheWeek::class.java)
                    intent.putExtra("dayOfWeek", selectedDayOfWeek)
                    startActivity(intent)
                }
            }

            if(requestCode == REQUEST_CODE_CHOOSE_DAY_SEE_PENDING_ORDERS){
                val selectedDayOfWeek = data?.getIntExtra("selected_day_of_week", -1)
                if (selectedDayOfWeek != null && selectedDayOfWeek != -1) {
                    val dayName = getDayName(selectedDayOfWeek)
                    Toast.makeText(this, "Selected Day: $dayName", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, PedidosEmpresa::class.java)
                    intent.putExtra("dayOfWeek", selectedDayOfWeek)
                    startActivity(intent)
                }
            }


        }
    }

    private fun getDayName(dayOfWeek: Int): String {
        return when (dayOfWeek) {
            Calendar.MONDAY -> "Monday"
            Calendar.TUESDAY -> "Tuesday"
            Calendar.WEDNESDAY -> "Wednesday"
            Calendar.THURSDAY -> "Thursday"
            Calendar.FRIDAY -> "Friday"
            Calendar.SATURDAY -> "Saturday"
            Calendar.SUNDAY -> "Sunday"
            else -> "Unknown"
        }
    }

    companion object {
        private const val REQUEST_CODE_CHOOSE_DAY_UPLOAD_LUNCH = 1
        private const val REQUEST_CODE_CHOOSE_DAY_SEE_PENDING_ORDERS = 2
    }
}