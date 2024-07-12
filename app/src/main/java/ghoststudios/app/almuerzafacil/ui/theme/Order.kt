package ghoststudios.app.almuerzafacil.ui.theme

import java.time.temporal.TemporalAmount
import java.util.Date

class Order(
    val id : String? = null,
    val idClient : String? = null,
    val lunch: String? = "",
    val dateOrdered: Date,
    val dateToDeliver:Date,
    var total: Int = 0,
    var wasDelivered : Boolean,
    var eatAtRestaurant:Boolean,
    var amount:Int
){
    constructor() : this(null, null, "", Date(), Date(), 0, false, true, 1)


}