package br.com.eduardo.msorder.shared.application.service

import br.com.eduardo.msorder.registerOrder.application.port.out.OrderConfirmationPublisherPort
import br.com.eduardo.msorder.registerOrder.model.messaging.OrderConfirmedMessage
import br.com.eduardo.msorder.shared.application.port.`in`.ConfirmationOrderUseCase
import br.com.eduardo.msorder.shared.application.port.out.FindOrderByIdPort
import br.com.eduardo.msorder.shared.application.port.out.UpdateOrderPort
import br.com.eduardo.msorder.shared.model.exception.ConfirmationOrderException
import org.springframework.stereotype.Service

@Service
class ConfirmationOrderService(
    val findOrderByIdPort: FindOrderByIdPort,
    val updateOrderPort: UpdateOrderPort,
    val orderConfirmationPublisherPort: OrderConfirmationPublisherPort
) : ConfirmationOrderUseCase {

    override fun confirm(orderId: String, companyId: String, cId: String) {
        val order = findOrderByIdPort.find(orderId, companyId)

        if (!order.canBeConfirmed())
            throw ConfirmationOrderException("Sorry, just OPENED orders can be confirmed")

        order.confirm()
            .also {
                updateOrderPort.update(order.id!!, order.companyId, order)
            }
            .also {
                orderConfirmationPublisherPort.publish(OrderConfirmedMessage(order), cId)
            }
    }
}