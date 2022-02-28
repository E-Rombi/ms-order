package br.com.eduardo.msorder.registerOrder.application.service

import br.com.eduardo.msorder.registerOrder.application.port.`in`.RegisterOrderUseCase
import br.com.eduardo.msorder.registerOrder.model.Order
import br.com.eduardo.msorder.registerOrder.model.request.RegisterOrderRequest
import org.springframework.stereotype.Service

@Service
class RegisterOrderService : RegisterOrderUseCase {

    override fun execute(request: RegisterOrderRequest, tId: String): Order {
        with(request) {
            return toOrder()
                .also { order ->
                    items.forEach { item ->
                        order.addItem(item)
                    }
                }.also { order ->
                    order.calcTotal()
                }
        }

    }
}