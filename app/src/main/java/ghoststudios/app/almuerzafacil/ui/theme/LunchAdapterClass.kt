package ghoststudios.app.almuerzafacil.ui.theme

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.squareup.picasso.Picasso
import ghoststudios.app.almuerzafacil.Lunch
import ghoststudios.app.almuerzafacil.R

class LunchAdapterClass(private val lunch: ArrayList<Lunch>):
    Adapter<LunchAdapterClass.ViewHolderClass>() {


    class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView = itemView.findViewById(R.id.CV_imageLunch)
        var itemHeader: TextView = itemView.findViewById(R.id.cv_headerLunch)
        var itemDescription: TextView = itemView.findViewById(R.id.cv_descriptionLunch)
        var itemPrice: TextView = itemView.findViewById(R.id.cv_priceLunch)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_lunch, parent,false)
        return ViewHolderClass(v);
    }

    override fun getItemCount(): Int {
        return lunch.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, i: Int) {
        holder.itemHeader.text  = lunch[i].name
        holder.itemDescription.text = lunch[i].description
        holder.itemPrice.text = lunch[i].price.toString()
        Picasso.get().load(lunch[i].imgUri).into(holder.itemImage)

    }
}