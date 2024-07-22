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
import java.util.Calendar

class AgendadosClienteAdapterClass (private val ordenes: ArrayList<Order>, private val lunches: ArrayList<Lunch>):
    Adapter<AgendadosClienteAdapterClass.AgViewHolderClass>() {

    private val viewHolders = SparseArray<AgViewHolderClass>()

    class AgViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewNameDay: TextView = itemView.findViewById(R.id.Ag_textViewDay)
        var textViewTime: TextView = itemView.findViewById(R.id.Ag_textViewTime)
        var textViewLunch: TextView = itemView.findViewById(R.id.Ag_textViewLunchName)
        var textViewQuantity: TextView = itemView.findViewById(R.id.Ag_textViewQuantity)
        var textViewForHereOrToGo: TextView = itemView.findViewById(R.id.Ag_textViewForHereOrToGo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgViewHolderClass {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_view_ver_pedido_cliente, parent, false)
        return AgViewHolderClass(v)
    }

    override fun getItemCount(): Int {
        return ordenes.size
    }

    override fun onBindViewHolder(holder: AgViewHolderClass, i: Int) {
        viewHolders.put(i, holder)

        holder.textViewNameDay.text = getDayName(ordenes[i].dateToDeliver.day)
        holder.textViewTime.text = "Hora Retiro: ${ordenes[i].dateToDeliver.hours} : ${ordenes[i].dateToDeliver.minutes}"
        holder.textViewLunch.text = getLunchesName(ordenes[i].lunch!!)
        holder.textViewQuantity.text = "Cantidad: " +ordenes[i].amount.toString()

        if(ordenes[i].eatAtRestaurant){
            holder.textViewForHereOrToGo.text = "Para Servir"
        }else{
            holder.textViewForHereOrToGo.text = "Para LLevar"
        }

    }
    fun getLunchesName(id : String):String{
        for(lunch in lunches){
            if(id==lunch.id){
                return lunch.name!!
            }
        }
        return ""
    }
    private fun getDayName(dayOfWeek: Int): String {
        return when (dayOfWeek) {
            Calendar.MONDAY -> "Lunes"
            Calendar.TUESDAY -> "Martes"
            Calendar.WEDNESDAY -> "Miércoles"
            Calendar.THURSDAY -> "Jueves"
            Calendar.FRIDAY -> "Viernes"
            Calendar.SATURDAY -> "Sábado"
            Calendar.SUNDAY -> "Domingo"
            else -> "Desconocido"
        }
    }
}