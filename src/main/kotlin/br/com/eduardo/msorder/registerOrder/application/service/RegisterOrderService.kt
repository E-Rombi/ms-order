package br.com.eduardo.msorder.registerOrder.application.service

import br.com.eduardo.msorder.registerOrder.application.port.`in`.RegisterOrderUseCase
import br.com.eduardo.msorder.registerOrder.application.port.out.RegisterOrderPort
import br.com.eduardo.msorder.registerOrder.model.Order
import br.com.eduardo.msorder.registerOrder.model.OrderStatus
import br.com.eduardo.msorder.registerOrder.model.request.RegisterOrderRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import javax.validation.Valid

@Service
@Validated
class RegisterOrderService(
    val registerOrderPort: RegisterOrderPort
) : RegisterOrderUseCase {

    private val logger = LoggerFactory.getLogger(RegisterOrderUseCase::class.java)

    override fun execute(@Valid request: RegisterOrderRequest, tId: String): Order {
        with(request) {
            return toOrder()
                .also { order ->
                    items.forEach { item ->
                        order.addItem(item)
                    }
                    logger.info("action=itemsAdded, tId=$tId")
                }
                .also { order ->
                    order.calcOrder()
                    logger.info("action=orderCalculated, tId=$tId")
                }
                .also { order ->
                    if (request.autoConfirmation) order.confirm()
                    logger.info("action=confirmationOrder, autoConfirmation=${request.autoConfirmation}, tId=$tId")
                }
                .also { order ->
                    registerOrderPort.register(order)
                    logger.info("action=orderSaved, tId=$tId")
                }
                .also { order ->
                    if (order.status == OrderStatus.CONFIRMED) println("Cria mensagem no broker SNS")
                    logger.info("action=messageSent, orderStatus=${order.status}, orderId=${order.id}, tId=$tId")
                }
        }

    }
}