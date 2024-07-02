package ghoststudios.app.almuerzafacil.ui.theme

import java.util.Date

class Order(
    val id : String? = null,
    val idClient : String? = null,
    val lunches: MutableMap<String, Int> = mutableMapOf(),
    val dateOrdered: Date,
    val dateToDeliver:Date,
    var total: Int = 0,
    var wasDelivered : Boolean
){
    constructor() : this(null, null, mutableMapOf(), Date(), Date(), 0, false)
    fun addLunch(lunchID: String, cantidad: Int) {
        lunches[lunchID] = (lunches[lunchID] ?: 0) + cantidad
        calculateTotal()
    }

    fun deleteLunch(lunchID: String, cantidad: Int) {
        val currentCantidad = lunches[lunchID] ?: 0
        if (currentCantidad > cantidad) {
            lunches[lunchID] = currentCantidad - cantidad
        } else {
            lunches.remove(lunchID)
        }
        calculateTotal()
    }

    fun calculateTotal() {

    }

    fun setOrderAsDelivered() {

        if (lunches.isNotEmpty()) {
            wasDelivered = true
            println("Pedido confirmado: $id")
        } else {
            println("El pedido no tiene almuerzos agregados.")
        }
    }
}