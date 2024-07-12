package ghoststudios.app.almuerzafacil

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
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
import ghoststudios.app.almuerzafacil.ui.theme.User

class ClientSelectLunches : AppCompatActivity() {
    private lateinit var firebaseRef: DatabaseReference
    private lateinit var arrayOfLunches: ArrayList<Lunch>
    private lateinit var adapter: LunchAdapterClass
    private lateinit var email :String
    private lateinit var uid : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_client_select_lunches)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        email = intent.getStringExtra("email")!!
        uid = intent.getStringExtra("uid")!!
        val day = intent.getIntExtra("dayOfWeek", 1)!!

        Toast.makeText(this, "dia de la semana {$day}", Toast.LENGTH_SHORT).show()
        firebaseRef = FirebaseDatabase.getInstance().getReference("lunches")
        arrayOfLunches = arrayListOf()

        val recyclerview = findViewById<RecyclerView>(R.id.rv_OrderLunchesClient)
        recyclerview.layoutManager = LinearLayoutManager(this)
        adapter = LunchAdapterClass(arrayOfLunches){show ->ShowSelectedIcons(show)}
        recyclerview.adapter = adapter

        findViewById<Button>(R.id.OrderLunch_btn).setOnClickListener{

            val intent = Intent(this , OrderALunchSettings::class.java)
            intent.putExtra("email", email)
            intent.putExtra("uid", uid)
            intent.putExtra("dayOfWeek", day)
            val num = adapter.getListOfLunches().size
            Toast.makeText(this,"tam{$num}",Toast.LENGTH_SHORT).show()
            intent.putParcelableArrayListExtra("lunches", adapter.getListOfLunches())
            startActivity(intent)
        }

        fetchData()
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
                }
                Toast.makeText(baseContext, "Loading lunches: ${arrayOfLunches.size}", Toast.LENGTH_SHORT).show()
                val recyclerview = findViewById<RecyclerView>(R.id.rv_OrderLunchesClient)
                adapter = LunchAdapterClass(arrayOfLunches){show ->ShowSelectedIcons(show)}
                recyclerview.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun ShowSelectedIcons(show: Boolean) {
        val scheduleBTN = findViewById<Button>(R.id.OrderLunch_btn)
        scheduleBTN.visibility = if (show) View.VISIBLE else View.GONE
    }

}