
package com.example.cupcake.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/** Preço de um único cupcake */
private const val PRICE_PER_CUPCAKE = 2.00

/**Custo adicional para pegar o pedido no mesmo dia*/
private const val PRICE_FOR_SAME_DAY_PICKUP = 3.00

/**
 * [OrderViewModel] tem todas as informações sobre um pedido de cupcakes
 * em termos de quantidade, sabor e data de entrega. É aqui também onde
 * calculamos o preço total baseado nos detalhes da entrega
 */
class OrderViewModel : ViewModel() {

    //Atributo que define a quantidade do pedido
    private val _quantity = MutableLiveData<Int>()
    val quantity: LiveData<Int> = _quantity

    //Atributo que define o sabor do pedido
    private val _flavor = MutableLiveData<String>()
    val flavor: LiveData<String> = _flavor

    //Atributo que lista possíveis datas de entrega
    val dateOptions: List<String> = getPickupOptions()

    //Atributo que define a data
    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    //Atributo que define o preço do pedido até o momento
    private val _price = MutableLiveData<Double>()
    val price: LiveData<String> = Transformations.map(_price) {
        /*
        Formata o preço de acordo com a moeda local e retorna
        essa informação como um LiveData<String>
         */
        NumberFormat.getCurrencyInstance().format(it)
    }

    init {
        //Quando o OrderViewModel é chamado pela primeira vez
        //reseta todas as informações do pedido
        resetOrder()
    }

    /**
     * Método que define a quantidade de cupcakes do pedido
     *
     * @param numberCupcakes to order
     */
    fun setQuantity(numberCupcakes: Int) {
        _quantity.value = numberCupcakes
        updatePrice()
    }

    /**
     *
     *Método que define o sabor do cupcake para o pedido. Apenas
     * 1 sabor pode ser selecionado
     *
     * @param desiredFlavor is the cupcake flavor as a string
     */
    fun setFlavor(desiredFlavor: String) {
        _flavor.value = desiredFlavor
    }

    /**
     * Define a data de entrega do pedido
     *
     * @param pickupDate is the date for pickup as a string
     */
    fun setDate(pickupDate: String) {
        _date.value = pickupDate
        updatePrice()
    }

    /**
     *
     * Método que retorna true se o sabor não for selecionado para o pedido
     */
    fun hasNoFlavorSet(): Boolean {
        return _flavor.value.isNullOrEmpty()
    }

    /**
     * Método para resetar todas as informações do pedido para valores
     * padrões
     */
    fun resetOrder() {
        _quantity.value = 0
        _flavor.value = ""
        _date.value = dateOptions[0]
        _price.value = 0.0
    }

    /**
     * Atualiza o preço com base nos detalhes do pedido
     */
    private fun updatePrice() {
        //Aqui é usado um operador elvis, dizendo que se a quantidade
        //for nula, ele usa o 0 como quantidade
        var calculatedPrice = (quantity.value ?: 0) * PRICE_PER_CUPCAKE
        //Se a data de entrega for no mesmo dia, adiciona uma tarifa
        if (dateOptions[0] == _date.value) {
            calculatedPrice += PRICE_FOR_SAME_DAY_PICKUP
        }
        _price.value = calculatedPrice
    }

    /**
     *
     * Retorna uma lista de datas, começando com a data atual
     * e mais três adicionais
     */
    private fun getPickupOptions(): List<String> {
        val options = mutableListOf<String>()
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        val calendar = Calendar.getInstance()
        repeat(4) {
            options.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }
        return options
    }
}