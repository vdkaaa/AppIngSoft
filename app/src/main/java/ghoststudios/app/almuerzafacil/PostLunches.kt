package ghoststudios.app.almuerzafacil

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
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
import java.util.Date

class PostLunches : AppCompatActivity() {

    private lateinit var firebaseRef: DatabaseReference
    private lateinit var firebaseRefStoreData: DatabaseReference
    private lateinit var arrayOfLunches: ArrayList<Lunch>
    private lateinit var arrayOfLunchesPosted: ArrayList<LunchesPosted>
    private lateinit var adapter: LunchAdapterClass
    private var day = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_post_lunches)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        MyToolbar().show(this, getString(R.string.toolbarTitlePostLunches), true)

        day = intent.getIntExtra("dayOfWeek", 1)
        val dayNextWeek = getNextDayOfWeek(day)

        println("PostLunches activity day $day, next week $dayNextWeek")

        firebaseRef = FirebaseDatabase.getInstance().getReference("lunches")
        firebaseRefStoreData = FirebaseDatabase.getInstance().getReference("lunchesPosted")
        arrayOfLunches = arrayListOf()
        arrayOfLunchesPosted = arrayListOf()

        val recyclerview = findViewById<RecyclerView>(R.id.rv_PostLunches)
        recyclerview.layoutManager = LinearLayoutManager(this)
        adapter = LunchAdapterClass(arrayOfLunches){ show -> showSelectedIcons(show) }
        recyclerview.adapter = adapter

        findViewById<Button>(R.id.btn_PostLunches).setOnClickListener{
            val dateToPost = getNextDayOfWeek(day)
            val listOfIDs = adapter.getListOfLunchesIDs()
            val lunchesId = firebaseRef.push().key!!

            val lunches = LunchesPosted(lunchesId, listOfIDs, dateToPost)

            adapter.unSelectLunches()
            var updated = false
            for (item in arrayOfLunchesPosted){
                if(item.datePosted?.day == day){
                    val updatedValue = item
                    for (lunchesToAdd in listOfIDs){
                        updatedValue.lunchPosted.add(lunchesToAdd)
                    }
                    firebaseRefStoreData.child(item.id).setValue(updatedValue)
                    updated = true
                }
            }
            if (!updated) {
                firebaseRefStoreData.child(lunchesId).setValue(lunches).addOnCompleteListener {
                    Toast.makeText(this, "data stored", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, SeeCompanyLunchesDayOfTheWeek::class.java).apply {
                        putExtra("dayOfWeek", day)
                    }
                    startActivity(intent)
                }.addOnFailureListener {
                    Toast.makeText(this, "data failed to store", Toast.LENGTH_SHORT).show()
                }
            }
        }
        fetchDataPostedLunches()
    }

    private fun fetchData() {
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayOfLunches.clear()
                if (snapshot.exists()) {
                    for (lunchesSnap in snapshot.children) {
                        val lunch = lunchesSnap.getValue(Lunch::class.java)
                        if (lunch != null && (arrayOfLunchesPosted.isEmpty() ||
                                    !arrayOfLunchesPosted[0].lunchPosted.contains(lunch.id))) {
                            arrayOfLunches.add(lunch)
                        }
                    }
                }
                Toast.makeText(baseContext, "Loading lunches: ${arrayOfLunches.size}", Toast.LENGTH_SHORT).show()
                val recyclerview = findViewById<RecyclerView>(R.id.rv_PostLunches)
                adapter = LunchAdapterClass(arrayOfLunches) { show -> showSelectedIcons(show) }
                recyclerview.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchDataPostedLunches() {
        firebaseRefStoreData.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayOfLunchesPosted.clear()
                if (snapshot.exists()) {
                    for (lunchesSnap in snapshot.children) {
                        val lunchPosted = lunchesSnap.getValue(LunchesPosted::class.java)
                        if (day == lunchPosted?.datePosted?.day) {
                            arrayOfLunchesPosted.add(lunchPosted)
                        }
                    }
                }
                fetchData()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showSelectedIcons(show: Boolean) {
        val btnPost = findViewById<Button>(R.id.btn_PostLunches)
        btnPost.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun getNextDayOfWeek(dayOfWeek: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = Date()

        val daysUntilNextDay = (dayOfWeek  - calendar.get(Calendar.DAY_OF_WEEK) + 7) % 7

        if (daysUntilNextDay == 0) {
            calendar.add(Calendar.WEEK_OF_YEAR, 1)
        } else {
            calendar.add(Calendar.DAY_OF_YEAR, daysUntilNextDay )
        }

        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek+1)
        return calendar.time
    }
}
