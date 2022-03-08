package br.com.eduardo.msorder.registerOrder.model.response

import br.com.eduardo.msorder.shared.model.OrderStatus

data class OrderRegisteredResponse(
    val id: String,
    val status: OrderStatus
)