package ghoststudios.app.almuerzafacil.ui.theme

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import ghoststudios.app.almuerzafacil.R
import java.util.Date

class LunchAdapterClass(private val lunches: ArrayList<Lunch>,
                        private val showMenu:(Boolean) ->Unit ):
    Adapter<LunchAdapterClass.ViewHolderClass>() {
    private var isEnable = false
    private var itemSelectedList = mutableListOf<Int>()
    private val viewHolders = SparseArray<ViewHolderClass>()

    class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView = itemView.findViewById(R.id.CV_imageLunch)
        var itemHeader: TextView = itemView.findViewById(R.id.cv_headerLunch)
        var itemDescription: TextView = itemView.findViewById(R.id.cv_descriptionLunch)
        var itemPrice: TextView = itemView.findViewById(R.id.cv_priceLunch)
        var itemTick: ImageView = itemView.findViewById(R.id.isSelectedTick)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_lunch, parent,false)
        return ViewHolderClass(v)
    }

    override fun getItemCount(): Int {
        return lunches.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, i: Int) {
        viewHolders.put(i, holder)
        holder.itemHeader.text  = lunches[i].name
        holder.itemDescription.text = lunches[i].description
        holder.itemPrice.text = lunches[i].price.toString()
        Picasso.get().load(lunches[i].imgUri).into(holder.itemImage)

        holder.itemView.setOnLongClickListener{
            selectItem(holder,i )
            true
        }
        holder.itemView.setOnClickListener{
            if(itemSelectedList.contains(i)){
                itemSelectedList.remove(i)
                holder.itemTick.visibility = View.GONE
                if(itemSelectedList.isEmpty()){
                    showMenu(false)
                    isEnable = false
                }

            }else if(isEnable){
                selectItem(holder,i )
            }
        }
    }

    private fun selectItem(holder: ViewHolderClass, i: Int) {
        isEnable = true
        itemSelectedList.add(i)
        showMenu(true)
        holder.itemTick.visibility = View.VISIBLE
    }
    fun scheduleOrder(cont:Context){

        if(itemSelectedList.isNotEmpty()){
            val clientID = "1111"
            val firebaseRef = FirebaseDatabase.getInstance().getReference("orders")
            val orderId = firebaseRef.push().key!!


            val currentDate = Date()
            val order = Order(
                id = orderId,
                idClient = clientID,
                dateOrdered = currentDate,
                dateToDeliver = currentDate,
                total = 0,
                wasDelivered = false
            )
            for (index in itemSelectedList){
                order.addLunch(lunches[index].id!!, 1)
                viewHolders[index]?.itemTick?.visibility = View.GONE
            }


            firebaseRef.child(orderId).setValue(order).addOnCompleteListener {
                Toast.makeText(cont, "data stored", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(cont, "data failed to store ", Toast.LENGTH_SHORT)
                    .show()
            }

        }
        showMenu(false)
        isEnable = false

    }

}