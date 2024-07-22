package ghoststudios.app.almuerzafacil

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.FirebaseDatabase
import ghoststudios.app.almuerzafacil.ui.theme.Lunch
import ghoststudios.app.almuerzafacil.ui.theme.MyToolbar
import ghoststudios.app.almuerzafacil.ui.theme.Order
import java.util.Calendar
import java.util.Date

class OrderALunchSettings : AppCompatActivity() {

    private lateinit var eatType: RadioGroup
    private lateinit var hourEat: RadioGroup
    private var eatAtRestaurantOptions = true
    private var hourTakeFood = 12
    private var minuteTakeFood = 30
    private lateinit var email :String
    private lateinit var uid : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_order_alunch_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        MyToolbar().show(this, getString(R.string.toolbarTitleOrderALunchSettings), true)


        val listOfLunches = intent.getParcelableArrayListExtra<Lunch>("lunches")
        email = intent.getStringExtra("email")!!
        uid = intent.getStringExtra("uid")!!
        val day = intent.getIntExtra("dayOfWeek", 1)


        eatType = findViewById(R.id.ServirOLlevar)
        hourEat = findViewById(R.id.horaARecibir)

        eatType.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.servirRadioButton -> {
                    eatAtRestaurantOptions = true
                }

                R.id.llevarRadioButton -> {
                    eatAtRestaurantOptions = false
                }
            }
        }
        hourEat.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_1130 -> {
                    hourTakeFood = 11
                    minuteTakeFood = 30
                }
                R.id.rb_1200 -> {
                    hourTakeFood = 12
                    minuteTakeFood = 0
                }
                R.id.rb_1230 -> {
                    hourTakeFood = 12
                    minuteTakeFood = 30
                }
                R.id.rb_1300 -> {
                    hourTakeFood = 13
                    minuteTakeFood = 0
                }
                R.id.rb_1330 -> {
                    hourTakeFood = 13
                    minuteTakeFood = 30
                }
                R.id.rb_1400 -> {
                    hourTakeFood = 14
                    minuteTakeFood = 0
                }
            }
        }

        val amount = findViewById<EditText>(R.id.cantidadInputField)

        findViewById<Button>(R.id.AgendarAlmuerzoBtn).setOnClickListener {

            val clientID = uid
            val firebaseRef = FirebaseDatabase.getInstance().getReference("orders")
            val orderId = firebaseRef.push().key!!
            val currentDate = Date()

            var dateToReceive = getNextDayOfWeek(day)
            dateToReceive.hours = hourTakeFood
            dateToReceive.minutes = minuteTakeFood
            val order = Order(
                id = orderId,
                idClient = clientID,
                lunch = listOfLunches!![0].id,
                dateOrdered = currentDate,
                dateToDeliver = dateToReceive,
                total = 0,
                wasDelivered = false,
                eatAtRestaurant = eatAtRestaurantOptions,
                amount = amount.text.toString().toInt()
            )
            println(order.dateOrdered)

            firebaseRef.child(orderId).setValue(order).addOnCompleteListener {
                Toast.makeText(this, "data stored", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomeClient::class.java)
                intent.putExtra("email", email)
                intent.putExtra("uid", uid)
                startActivity(intent)
            }.addOnFailureListener {
                Toast.makeText(this, "data failed to store ", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun getNextDayOfWeek(dayOfWeek: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = Date()

        val daysUntilNextDay = (dayOfWeek - calendar.get(Calendar.DAY_OF_WEEK) + 7) % 7

        if (daysUntilNextDay == 0) {
            calendar.add(Calendar.WEEK_OF_YEAR, 1)
        } else {
            calendar.add(Calendar.DAY_OF_YEAR, daysUntilNextDay)
        }

        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek +1)
        return calendar.time
    }
}