package br.com.eduardo.msorder.registerOrder.application.service

import br.com.eduardo.msorder.registerOrder.adapter.out.client.ProductClient
import br.com.eduardo.msorder.registerOrder.application.port.`in`.RegisterOrderUseCase
import br.com.eduardo.msorder.registerOrder.application.port.out.OrderConfirmationPublisherPort
import br.com.eduardo.msorder.registerOrder.application.port.out.RegisterOrderPort
import br.com.eduardo.msorder.registerOrder.model.Order
import br.com.eduardo.msorder.registerOrder.model.OrderStatus
import br.com.eduardo.msorder.registerOrder.model.exception.ResourceNotFoundException
import br.com.eduardo.msorder.registerOrder.model.messaging.OrderConfirmedMessage
import br.com.eduardo.msorder.registerOrder.model.request.FindAllProductsByIdRequest
import br.com.eduardo.msorder.registerOrder.model.request.RegisterOrderRequest
import feign.FeignException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import java.lang.RuntimeException
import javax.validation.Valid
import kotlin.math.log

@Service
@Validated
class RegisterOrderService(
    val registerOrderPort: RegisterOrderPort,
    val orderConfirmationPublisherPort: OrderConfirmationPublisherPort,
    val productClient: ProductClient
) : RegisterOrderUseCase {

    private val logger = LoggerFactory.getLogger(RegisterOrderUseCase::class.java)

    override fun execute(@Valid request: RegisterOrderRequest, tId: String): Order {
        with(request) {
            return toOrder()
                .also {
                    val ids = items.map { it.product.barcode }.toSet()
                    try {
                        val response = productClient.findAllById(FindAllProductsByIdRequest(ids), tId).body

                        val others = ids.map { response?.ids?.contains(it) }

                        if (others.contains(false))
                            throw ResourceNotFoundException("Products not found")
                    } catch (e: FeignException.FeignClientException) {
                        logger.error("${e.message}", e)
                        throw RuntimeException()
                    }
                }
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
                    if (order.status == OrderStatus.CONFIRMED)
                        orderConfirmationPublisherPort.publish(OrderConfirmedMessage(order), tId).also {
                            logger.info("action=messageSent, orderStatus=${order.status}, orderId=${order.id},tId=$tId")
                        }
                }
        }

    }
}