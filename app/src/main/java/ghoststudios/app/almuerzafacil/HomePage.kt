package ghoststudios.app.almuerzafacil

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
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

enum class ProviderType {
    BASIC
}

class HomePage : AppCompatActivity() {

    private lateinit var firebaseRef: DatabaseReference
    private lateinit var arrayOfLunches: ArrayList<Lunch>
    private lateinit var adapter: LunchAdapterClass

    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtener información del usuario desde el Intent
        val emailIntent = intent.getStringExtra("email")
        val nameIntent = intent.getStringExtra("name")
        user = User(name = nameIntent, email = emailIntent)

        // Resto de la configuración
        firebaseRef = FirebaseDatabase.getInstance().getReference("lunches")
        arrayOfLunches = arrayListOf()
        ShowOrderUserLunches()
        fetchData()

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerViewHome)
        adapter = LunchAdapterClass(arrayOfLunches){show ->ShowSelectedIcons(show)}
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter

        val scheduleOrder = findViewById<Button>(R.id.AgendarBtn)
        scheduleOrder.setOnClickListener {
            orderLunches()
        }

        /*val backBtn = findViewById<Button>(R.id.BcackClientOrderBTn)
        backBtn.setOnClickListener {
            val intent = Intent(this, ProvisionalLogIn::class.java)
             startActivity(intent)
        }*/

        // Uso de la información del usuario
        user?.let {
            Toast.makeText(this, "Bienvenido ${it.name +" "+ it.lastname} ", Toast.LENGTH_SHORT).show()
            val txtUser = findViewById<TextView>(R.id.txtIDUser)
            txtUser.text = "${it.email}, que deseas pedir hoy?"
        }
    }

    private fun ShowSelectedIcons(show: Boolean) {
        val scheduleBTN = findViewById<Button>(R.id.AgendarBtn)
        scheduleBTN.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun orderLunches() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Agendar almuerzos")
        alertDialog.setMessage("¿Quieres agendar estos almuerzos?")
        alertDialog.setPositiveButton("Agendar") { _, _ ->
            adapter.scheduleOrder(this)
            ShowSelectedIcons(false)
        }
        alertDialog.setNegativeButton("Cancelar") { _, _ -> }
        alertDialog.show()
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
                val recyclerview = findViewById<RecyclerView>(R.id.recyclerViewHome)
                adapter = LunchAdapterClass(arrayOfLunches){show ->ShowSelectedIcons(show)}
                recyclerview.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun ShowOrderUserLunches() {
        val lunchesOrders = findViewById<Button>(R.id.btnAlmuerzoAgendados)
        lunchesOrders.setOnClickListener {
            val intent = Intent(this, LunchesUser::class.java)
            startActivity(intent)
        }
    }
}
