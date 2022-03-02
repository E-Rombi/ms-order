package br.com.eduardo.msorder.registerOrder.application.port.`in`

import br.com.eduardo.msorder.registerOrder.model.Order
import br.com.eduardo.msorder.registerOrder.model.request.RegisterOrderRequest
import javax.validation.Valid

interface RegisterOrderUseCase {

    fun execute(@Valid request: RegisterOrderRequest, tId: String): Order
}
