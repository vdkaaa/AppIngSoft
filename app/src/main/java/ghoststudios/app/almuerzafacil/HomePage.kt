package ghoststudios.app.almuerzafacil

import android.os.Bundle
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
import ghoststudios.app.almuerzafacil.ui.theme.LunchAdapterClass

class HomePage : AppCompatActivity() {

    private lateinit var firebaseRef: DatabaseReference
    private lateinit var arrayOfLunches : ArrayList<Lunch>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        firebaseRef = FirebaseDatabase.getInstance().getReference("test")
        arrayOfLunches = arrayListOf()

        fetchData()
        val recyclerview = findViewById<RecyclerView>(R.id.recyclerViewHome)

        val adapter = LunchAdapterClass(arrayOfLunches)

        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter

    }
    private fun getSampleLunchList(): Array<Lunch> {
        return arrayOf(
            Lunch(
                id = "1",
                name = "Chicken Sandwich",
                description = "Grilled chicken sandwich with lettuce and tomato.",
                imgUri = "https://example.com/images/chicken_sandwich.jpg"
            ),
            Lunch(
                id = "2",
                name = "Caesar Salad",
                description = "Fresh romaine lettuce with Caesar dressing, croutons, and Parmesan cheese.",
                imgUri = "https://example.com/images/caesar_salad.jpg"
            ),
            Lunch(
                id = "3",
                name = "Veggie Wrap",
                description = "Whole wheat wrap filled with hummus, cucumber, tomatoes, and spinach.",
                imgUri = "https://example.com/images/veggie_wrap.jpg"
            ),
        )
    }

    private fun fetchData()
    {
        firebaseRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               arrayOfLunches.clear()
                if(snapshot.exists()){
                    for (lunchesSnap in snapshot.children){
                        val lunch = lunchesSnap.getValue(Lunch::class.java)
                        arrayOfLunches.add(lunch!!)
                    }
                }
                Toast.makeText(baseContext,"Loading lunchs: ${arrayOfLunches.size}", Toast.LENGTH_SHORT).show()

                val recyclerview = findViewById<RecyclerView>(R.id.recyclerViewHome)

                val adapter = LunchAdapterClass(arrayOfLunches)
                recyclerview.adapter = adapter

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext," error: ", Toast.LENGTH_SHORT).show()
            }

        })
    }
}