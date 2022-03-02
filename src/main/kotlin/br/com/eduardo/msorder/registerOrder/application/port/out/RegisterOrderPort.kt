package br.com.eduardo.msorder.registerOrder.application.port.out

import br.com.eduardo.msorder.registerOrder.model.Order

interface RegisterOrderPort {

    fun register(order: Order): Order
}
