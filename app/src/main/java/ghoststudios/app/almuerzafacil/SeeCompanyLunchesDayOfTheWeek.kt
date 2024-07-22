package ghoststudios.app.almuerzafacil

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ghoststudios.app.almuerzafacil.ui.theme.Lunch
import ghoststudios.app.almuerzafacil.ui.theme.LunchAdapterClass
import ghoststudios.app.almuerzafacil.ui.theme.LunchesPosted
import ghoststudios.app.almuerzafacil.ui.theme.MyToolbar
import java.util.Calendar

class SeeCompanyLunchesDayOfTheWeek : AppCompatActivity() {
    private lateinit var firebaseRef: DatabaseReference
    private lateinit var adapter: LunchAdapterClass
    private lateinit var firebaseRefLunchesPosted: DatabaseReference
    private lateinit var arrayOfLunches: ArrayList<Lunch>
    private lateinit var arrayOfLunchesToShow: ArrayList<Lunch>
    private var day = 0
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_see_company_lunches_day_of_the_week)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        MyToolbar().show(this, getString(R.string.toolbarTitleLunchesDayCompany), true)

        sharedPreferences = getSharedPreferences("ghoststudios.app.almuerzafacil.PREFERENCES", Context.MODE_PRIVATE)
        day = intent.getIntExtra("dayOfWeek", sharedPreferences.getInt("savedDayOfWeek", Calendar.MONDAY))
        saveDayToPreferences(day)

        firebaseRef = FirebaseDatabase.getInstance().getReference("lunches")
        arrayOfLunches = arrayListOf()
        arrayOfLunchesToShow = arrayListOf()
        firebaseRefLunchesPosted = FirebaseDatabase.getInstance().getReference("lunchesPosted")

        val recyclerview = findViewById<RecyclerView>(R.id.rv_CompanyLunches)
        recyclerview.layoutManager = LinearLayoutManager(this)
        adapter = LunchAdapterClass(arrayOfLunches) { show -> ShowEditButtons(show) }
        recyclerview.adapter = adapter
        fetchData()

        findViewById<TextView>(R.id.id_txtNameDay).text = getDayName(day)
        findViewById<Button>(R.id.btn_addLunch).setOnClickListener {
            val intent = Intent(this, PostLunches::class.java).apply {
                putExtra("dayOfWeek", day)

            }
            startActivity(intent)
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

    private fun getLunches() {
        firebaseRefLunchesPosted.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayOfLunchesToShow.clear()
                if (snapshot.exists()) {
                    for (lunchesSnap in snapshot.children) {
                        val lunch = lunchesSnap.getValue(LunchesPosted::class.java)
                        for (item in arrayOfLunches) {
                            for (lunchIDToCheck in lunch!!.lunchPosted) {
                                if (item.id == lunchIDToCheck && lunch.datePosted!!.day == day) {
                                    arrayOfLunchesToShow.add(item)
                                    break
                                }
                            }
                        }
                    }
                }
                val recyclerview = findViewById<RecyclerView>(R.id.rv_CompanyLunches)
                adapter = LunchAdapterClass(arrayOfLunchesToShow) { show -> ShowSelectedIcons(show) }
                recyclerview.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchData() {
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayOfLunches.clear()
                if (snapshot.exists()) {
                    for (lunchesSnap in snapshot.children) {
                        val lunch = lunchesSnap.getValue(Lunch::class.java)
                        arrayOfLunches.add(lunch!!)
                    }
                    getLunches()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun ShowEditButtons(show: Boolean) {
        val editButton = findViewById<View>(R.id.EditLunch_btn)
        val deleteButton = findViewById<View>(R.id.DeleteLunch_btn)
        editButton.visibility = if (show) View.VISIBLE else View.GONE
        deleteButton.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun editLunch(lunch: Lunch) {
        val intent = Intent(this, EditLunchActivity::class.java)
        intent.putExtra("lunch", lunch)
        startActivity(intent)
    }

    private fun deleteLunch(lunch: Lunch) {
        firebaseRef.child(lunch.id!!).removeValue().addOnSuccessListener {
            Toast.makeText(this, "Almuerzo eliminado", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Error al eliminar el almuerzo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun ShowSelectedIcons(show: Boolean) {
    }

    private fun saveDayToPreferences(dayOfWeek: Int) {
        with(sharedPreferences.edit()) {
            putInt("savedDayOfWeek", dayOfWeek)
            apply()
        }
    }
}
