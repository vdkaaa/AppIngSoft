package ghoststudios.app.almuerzafacil.ui.theme

import androidx.appcompat.app.AppCompatActivity
import ghoststudios.app.almuerzafacil.R

class MyToolbar {
    fun show(activities:AppCompatActivity, title:String, upButton: Boolean){
        activities.setSupportActionBar(activities.findViewById(R.id.defaultToolbar))
        activities.supportActionBar?.title = title
        activities.supportActionBar?.setDisplayHomeAsUpEnabled(upButton)
    }

}