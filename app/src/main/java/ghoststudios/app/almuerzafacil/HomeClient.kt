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
import ghoststudios.app.almuerzafacil.ui.theme.PedidosEmpresa
import java.util.Calendar

class HomeClient : AppCompatActivity() {
    private lateinit var email :String
    private lateinit var uid : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_client)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        email = intent.getStringExtra("email")!!
        uid = intent.getStringExtra("uid")!!

        findViewById<Button>(R.id.btn_orderLunch).setOnClickListener{
            val intent = Intent(this, ChooseDayOfTheWeek::class.java)
            startActivityForResult(intent, REQUEST_CODE_CHOOSE_DAY)
        }
        findViewById<Button>(R.id.btn_seeOrderLunch).setOnClickListener{
            val intent = Intent(this, OrdersOfClient::class.java)
            intent.putExtra("email", email)
            intent.putExtra("uid", uid)
            startActivity(intent)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK)
        {
            if (requestCode == REQUEST_CODE_CHOOSE_DAY) {
                val selectedDayOfWeek = data?.getIntExtra("selected_day_of_week", -1)
                if (selectedDayOfWeek != null && selectedDayOfWeek != -1) {
                    val dayName = getDayName(selectedDayOfWeek)
                    //Toast.makeText(this, "Selected Day: $dayName", Toast.LENGTH_SHORT).show()
                    println("ClientSelectLunches day get $dayName , num $selectedDayOfWeek")

                    val intent = Intent(this, ClientSelectLunches::class.java)
                    intent.putExtra("dayOfWeek", selectedDayOfWeek)
                    intent.putExtra("email", email)
                    intent.putExtra("uid", uid)
                    startActivity(intent)
                }
            }
            if (requestCode == REQUEST_CODE_CHOOSE_DAY_SEE_ORDER) {
                val selectedDayOfWeek = data?.getIntExtra("selected_day_of_week", -1)
                if (selectedDayOfWeek != null && selectedDayOfWeek != -1) {
                    val dayName = getDayName(selectedDayOfWeek)
                    println("OrdersOfClient day get $dayName , num $selectedDayOfWeek")
                    //Toast.makeText(this, "Selected Day: $dayName", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, OrdersOfClient::class.java)
                    intent.putExtra("dayOfWeek", selectedDayOfWeek)
                    intent.putExtra("email", email)
                    intent.putExtra("uid", uid)
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
        private const val REQUEST_CODE_CHOOSE_DAY = 1
        private const val REQUEST_CODE_CHOOSE_DAY_SEE_ORDER = 2
    }
}