package ghoststudios.app.almuerzafacil.ui.theme

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
import ghoststudios.app.almuerzafacil.R

class PedidosEmpresa : AppCompatActivity() {
    private lateinit var ordersRef: DatabaseReference
    private lateinit var usersRef: DatabaseReference
    private lateinit var lunchRef: DatabaseReference

    private lateinit var arrayOfOrders: ArrayList<Order>
    private lateinit var arrayOfUsers: ArrayList<User>
    private lateinit var arrayOfLunch: ArrayList<Lunch>

    private lateinit var adapter: PedidosAdapterClass
    private var day = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //this.supportActionBar?.hide()
        enableEdgeToEdge()
        setContentView(R.layout.activity_pedidos_empresa)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        MyToolbar().show(this, getString(R.string.toolbarTitleOrdersCompany), true)



        day = intent.getIntExtra("dayOfWeek", 1)

        ordersRef = FirebaseDatabase.getInstance().getReference("orders")
        usersRef = FirebaseDatabase.getInstance().getReference("users")
        lunchRef = FirebaseDatabase.getInstance().getReference("lunches")

        arrayOfOrders = arrayListOf()
        arrayOfUsers = arrayListOf()
        arrayOfLunch = arrayListOf()

        adapter = PedidosAdapterClass(arrayOfOrders, arrayOfUsers, arrayOfLunch)

        val P_recyclerview = findViewById<RecyclerView>(R.id.recyclerViewPedidos)
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
                            arrayOfOrders.add(it)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
                fetchUser()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun fetchUser() {
        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayOfUsers.clear()
                if (snapshot.exists()) {
                    for (userSnap in snapshot.children) {
                        val user = userSnap.getValue(User::class.java)
                        user?.let {
                            arrayOfUsers.add(it)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
                fetchLunch()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchLunch() {
        lunchRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayOfLunch.clear()
                if (snapshot.exists()) {
                    for (lunchSnap in snapshot.children) {
                        val user = lunchSnap.getValue(Lunch::class.java)
                        user?.let {
                            arrayOfLunch.add(it)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
                adapter = PedidosAdapterClass(arrayOfOrders, arrayOfUsers, arrayOfLunch)
                val P_recyclerview = findViewById<RecyclerView>(R.id.recyclerViewPedidos)
                P_recyclerview.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}