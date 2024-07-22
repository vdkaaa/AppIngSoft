package ghoststudios.app.almuerzafacil.ui.theme


import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.google.firebase.database.FirebaseDatabase
import ghoststudios.app.almuerzafacil.R

class PedidosAdapterClass (private val pedidos: ArrayList<Order>, private val users: ArrayList<User>):
    Adapter<PedidosAdapterClass.PViewHolderClass>() {

    private val viewHolders = SparseArray<PViewHolderClass>()

    class PViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewName: TextView = itemView.findViewById(R.id.PtextViewName)
        var textViewTime: TextView = itemView.findViewById(R.id.PtextViewTime)
        var textViewTime2 : TextView = itemView.findViewById(R.id.PtextViewTime2)
        var textViewLunch: TextView = itemView.findViewById(R.id.PtextViewLunch)
        var textViewQuantity: TextView = itemView.findViewById(R.id.PtextViewQuantity)
        var textViewForHereOrToGo: TextView = itemView.findViewById(R.id.PtextViewForHereOrToGo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PViewHolderClass {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_view_ver_pedidos_empresa, parent, false)
        return PViewHolderClass(v)
    }
    override  fun getItemCount(): Int{
        return pedidos.size
    }

    override fun onBindViewHolder(holder: PViewHolderClass, i: Int) {
        viewHolders.put(i, holder)

        holder.textViewName.text = getUserName(pedidos[i].idClient!!)
        holder.textViewTime.text = "Hora Pedido: ${pedidos[i].dateOrdered.hours} : ${pedidos[i].dateOrdered.minutes}"
        holder.textViewTime2.text = "Hora Retiro: ${pedidos[i].dateToDeliver.hours} : ${pedidos[i].dateToDeliver.minutes}"
        holder.textViewLunch.text = pedidos[i].lunch
        holder.textViewQuantity.text = pedidos[i].amount.toString()

        if(pedidos[i].eatAtRestaurant){
            holder.textViewForHereOrToGo.text = "Para Servir"
        }else{
            holder.textViewForHereOrToGo.text = "Para LLevar"
        }
    }
    fun fetchData(){
        val firebaseRef = FirebaseDatabase.getInstance().getReference("users")
    }
    fun getUserName(id : String):String{
        for(user in users){
            if(id==user.id){
                return user.name!!
            }
        }
        return ""
    }
}