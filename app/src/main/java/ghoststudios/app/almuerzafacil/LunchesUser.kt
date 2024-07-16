package ghoststudios.app.almuerzafacil

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
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
import ghoststudios.app.almuerzafacil.ui.theme.Order

class LunchesUser : AppCompatActivity() {
    private lateinit var firebaseRef: DatabaseReference
    private lateinit var ordersRef: DatabaseReference
    private lateinit var arrayOfLunches: ArrayList<Lunch>
    private lateinit var adapter: LunchAdapterClass
    private val userId = "1111" // ID del usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lunches_user)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Firebase references
        firebaseRef = FirebaseDatabase.getInstance().getReference("lunches")
        ordersRef = FirebaseDatabase.getInstance().getReference("orders")

        // Initialize the array list
        arrayOfLunches = arrayListOf()

        // Initialize the adapter
        adapter = LunchAdapterClass(arrayOfLunches){show ->ShowSelectedIcons(show)}

        // Set up RecyclerView
        val recyclerview = findViewById<RecyclerView>(R.id.recyclerViewUserOrder)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter

        fetchUserOrders()
        val backBtn = findViewById<Button>(R.id.ordersBackBtn)
        backBtn.setOnClickListener{
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
        }
    }
    private fun ShowSelectedIcons(show: Boolean) {
    }
    private fun fetchUserOrders() {
        ordersRef.orderByChild("idClient").equalTo(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayOfLunches.clear()
                if (snapshot.exists()) {
                    for (orderSnap in snapshot.children) {
                        val order = orderSnap.getValue(Order::class.java)
                        order?.let {
                            //for (lunchId in it.lunches.keys) {
                            //    fetchLunchDetails(lunchId)
                            //}
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchLunchDetails(lunchId: String) {
        firebaseRef.child(lunchId).addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                val lunch = snapshot.getValue(Lunch::class.java)
                lunch?.let {
                    arrayOfLunches.add(it)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showMenu(show: Boolean) {
        // Implementar lógica para mostrar u ocultar menú si es necesario
    }
}
