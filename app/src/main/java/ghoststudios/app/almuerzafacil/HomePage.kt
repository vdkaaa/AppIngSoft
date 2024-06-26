package ghoststudios.app.almuerzafacil

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

class HomePage : AppCompatActivity() {

    private lateinit var firebaseRef: DatabaseReference
    private lateinit var arrayOfLunches : ArrayList<Lunch>
    private lateinit var adapter: LunchAdapterClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        firebaseRef = FirebaseDatabase.getInstance().getReference("lunches")
        arrayOfLunches = arrayListOf()

        fetchData()
        val recyclerview = findViewById<RecyclerView>(R.id.recyclerViewHome)

        adapter = LunchAdapterClass(arrayOfLunches){show ->ShowSelectedIcons(show)}

        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter
        val scheduleOrder = findViewById<Button>(R.id.AgendarBtn)
        scheduleOrder.setOnClickListener{
            orderLunches()
        }
    }
    fun ShowSelectedIcons( show:Boolean){
        val scheduleBTN= findViewById<Button>(R.id.AgendarBtn)
        if(show){
            scheduleBTN.visibility = View.VISIBLE
        }else{
            scheduleBTN.visibility = View.GONE
        }

    }
    fun orderLunches(){
        var alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Agendar almuerzos")
        alertDialog.setMessage("¿Quieres agendar estos almuerzos?")
        alertDialog.setPositiveButton("Agendar"){_,_->
            adapter.scheduleOrder(this)
            ShowSelectedIcons(false)

        }
        alertDialog.setNegativeButton("Cancelar"){_,_->}
        alertDialog.show()
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

                adapter = LunchAdapterClass(arrayOfLunches){show ->ShowSelectedIcons(show)}
                recyclerview.adapter = adapter

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext," error: ", Toast.LENGTH_SHORT).show()
            }

        })
    }
}