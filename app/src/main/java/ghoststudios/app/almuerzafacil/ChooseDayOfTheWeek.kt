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

        findViewById<Button>(R.id.btn_monday).setOnClickListener {
            setResultAndFinish(Calendar.MONDAY)
        }
        findViewById<Button>(R.id.btn_tuesday).setOnClickListener {
            setResultAndFinish(Calendar.TUESDAY)
        }
        findViewById<Button>(R.id.btn_wednesay).setOnClickListener {
            setResultAndFinish(Calendar.WEDNESDAY)
        }
        findViewById<Button>(R.id.btn_thursday).setOnClickListener {
            setResultAndFinish(Calendar.THURSDAY)
        }
        findViewById<Button>(R.id.btn_friday).setOnClickListener {
            setResultAndFinish(Calendar.FRIDAY)
        }
        findViewById<Button>(R.id.btn_saturday).setOnClickListener {
            setResultAndFinish(Calendar.SATURDAY)
        }
        findViewById<Button>(R.id.btn_sunday).setOnClickListener {
            setResultAndFinish(Calendar.SUNDAY)
        }
    }

    private fun setResultAndFinish(dayOfWeek: Int) {
        val resultIntent = Intent()
        println("returned day $dayOfWeek")
        resultIntent.putExtra("selected_day_of_week", dayOfWeek)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}