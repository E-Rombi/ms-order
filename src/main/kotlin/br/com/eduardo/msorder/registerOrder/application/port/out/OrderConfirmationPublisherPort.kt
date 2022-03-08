package br.com.eduardo.msorder.registerOrder.application.port.out

import br.com.eduardo.msorder.registerOrder.model.messaging.OrderConfirmedMessage

interface OrderConfirmationPublisherPort {

    fun publish(data: OrderConfirmedMessage, cId: String)
}