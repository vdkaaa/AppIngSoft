package ghoststudios.app.almuerzafacil

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
import ghoststudios.app.almuerzafacil.ui.theme.MyToolbar
import ghoststudios.app.almuerzafacil.ui.theme.Order
import ghoststudios.app.almuerzafacil.ui.theme.AgendadosClienteAdapterClass
import ghoststudios.app.almuerzafacil.ui.theme.Lunch
import java.util.Calendar
import java.util.Date

class OrdersOfClient : AppCompatActivity() {

    private lateinit var ordersRef: DatabaseReference
    private lateinit var lunchesRef: DatabaseReference
    private lateinit var arrayOfOrders: ArrayList<Order>
    private lateinit var arrayOfLunches: ArrayList<Lunch>
    private lateinit var adapter: AgendadosClienteAdapterClass
    private lateinit var email :String
    private lateinit var uid : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_orders_of_client)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        MyToolbar().show(this, getString(R.string.toolbarTitleOrderOfClient), true)


        email = intent.getStringExtra("email")!!
        uid = intent.getStringExtra("uid")!!

        //println("OrdersOfClient day num $day")
        //Toast.makeText(this, "dia {$day}", Toast.LENGTH_SHORT).show()

        ordersRef = FirebaseDatabase.getInstance().getReference("orders")
        lunchesRef = FirebaseDatabase.getInstance().getReference("lunches")

        arrayOfOrders = arrayListOf()
        arrayOfLunches = arrayListOf()


        adapter = AgendadosClienteAdapterClass(arrayOfOrders, arrayOfLunches)

        val P_recyclerview = findViewById<RecyclerView>(R.id.rv_ClientOrders)
        P_recyclerview.layoutManager = LinearLayoutManager(this)
        P_recyclerview.adapter = adapter

        fetchOrders()

    }

    private fun fetchOrders() {
        ordersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayOfOrders.clear()
                if (snapshot.exists()) {
                    for (orderSnap in snapshot.children) {
                        val order = orderSnap.getValue(Order::class.java)
                        order?.let {
                            val calendar = Calendar.getInstance()
                            val date = Date()
                            calendar.time = date
                            if(uid==order.idClient){
                                arrayOfOrders.add(it)
                            }
                        }
                    }
                    adapter.notifyDataSetChanged()
                    fetchLunches()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun fetchLunches() {
        lunchesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayOfLunches.clear()
                if (snapshot.exists()) {
                    for (lunchSnap in snapshot.children) {
                        val lunch = lunchSnap.getValue(Lunch::class.java)
                        lunch?.let {
                            arrayOfLunches.add(it)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
                adapter = AgendadosClienteAdapterClass(arrayOfOrders, arrayOfLunches)
                val P_recyclerview = findViewById<RecyclerView>(R.id.rv_ClientOrders)
                P_recyclerview.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}