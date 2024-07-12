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

class PedidosAdapterClass (private val pedidos: ArrayList<Order>):
    Adapter<PedidosAdapterClass.PViewHolderClass>() {

    private val viewHolders = SparseArray<PViewHolderClass>()
    private var isEnable= false

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
        holder.textViewName.text = pedidos[i].idClient
        holder.textViewTime.text = pedidos[i].dateOrdered.toString()
        holder.textViewTime2.text = pedidos[i].dateToDeliver.toString()
        holder.textViewLunch.text = pedidos[i].id
        holder.textViewQuantity.text = pedidos[i].total.toString()
       //holder.textViewForHereOrToGo.text = pedidos[i].wasDelivered.toString()
        if(pedidos[i].wasDelivered){
            holder.textViewForHereOrToGo.text = "Para LLevar"
        }else{
            holder.textViewForHereOrToGo.text = "Para Servir"
        }
    }
}