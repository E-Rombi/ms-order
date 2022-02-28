package br.com.eduardo.msorder.registerOrder.application.port.`in`

import br.com.eduardo.msorder.registerOrder.model.Order
import br.com.eduardo.msorder.registerOrder.model.request.RegisterOrderRequest

interface RegisterOrderUseCase {

    fun execute(request: RegisterOrderRequest, tId: String): Order
}
