package ghoststudios.app.almuerzafacil

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar
import java.util.Date

class ChooseDayOfTheWeek : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_choose_day_of_the_week)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val todayDate = Date()

        val btn1 = findViewById<Button>(R.id.btn_chooseDay1)
        btn1.setOnClickListener {
            //setResultAndFinish(Calendar.MONDAY)
            setResultAndFinish((todayDate.day + 1) % 7+ 1)
        }
        btn1.text = getDayName((todayDate.day + 1) % 7+ 1)
        val btn2 = findViewById<Button>(R.id.btn_chooseDay2)
        btn2.setOnClickListener {
            //setResultAndFinish(Calendar.TUESDAY)
            setResultAndFinish((todayDate.day + 2) % 7 + 1)
        }
        btn2.text = getDayName((todayDate.day + 2) % 7+ 1)
        val btn3 = findViewById<Button>(R.id.btn_chooseDay3)
        btn3.setOnClickListener {
            //setResultAndFinish(Calendar.WEDNESDAY)
            setResultAndFinish((todayDate.day + 3) % 7+ 1)
        }
        btn3.text = getDayName((todayDate.day + 3) % 7+ 1)
        val btn4 = findViewById<Button>(R.id.btn_chooseDay4)
        btn4.setOnClickListener {
            //setResultAndFinish(Calendar.THURSDAY)
            setResultAndFinish((todayDate.day + 4) % 7+ 1)
        }
        btn4.text = getDayName((todayDate.day + 4) % 7+ 1)
        val btn5 = findViewById<Button>(R.id.btn_chooseDay5)
        btn5.setOnClickListener {
            //setResultAndFinish(Calendar.FRIDAY)
            setResultAndFinish((todayDate.day + 5) % 7+ 1)
        }
        btn5.text = getDayName((todayDate.day + 5) % 7 + 1)
        val btn6 = findViewById<Button>(R.id.btn_chooseDay6)
        btn6.setOnClickListener {
            //setResultAndFinish(Calendar.SATURDAY)
            setResultAndFinish((todayDate.day + 6) % 7+ 1)
        }
        btn6.text = getDayName((todayDate.day + 6) % 7+ 1)
        val btn7 = findViewById<Button>(R.id.btn_chooseDay7)
        btn7.setOnClickListener {
            //setResultAndFinish(Calendar.SUNDAY)
            setResultAndFinish((todayDate.day + 7) % 7+ 1)
        }
        btn7.text = getDayName((todayDate.day + 7) % 7+ 1)
    }

    private fun setResultAndFinish(dayOfWeek: Int) {
        val resultIntent = Intent()
        println("returned day $dayOfWeek")
        resultIntent.putExtra("selected_day_of_week", dayOfWeek)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
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
}