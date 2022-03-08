package br.com.eduardo.msorder.registerOrder.application.service

import br.com.eduardo.msorder.registerOrder.adapter.out.client.CustomerClient
import br.com.eduardo.msorder.registerOrder.adapter.out.client.ProductClient
import br.com.eduardo.msorder.registerOrder.application.port.`in`.RegisterOrderUseCase
import br.com.eduardo.msorder.registerOrder.application.port.out.OrderConfirmationPublisherPort
import br.com.eduardo.msorder.registerOrder.application.port.out.RegisterOrderPort
import br.com.eduardo.msorder.shared.model.Order
import br.com.eduardo.msorder.shared.model.OrderStatus
import br.com.eduardo.msorder.shared.model.exception.GenericException
import br.com.eduardo.msorder.shared.model.exception.ResourceNotFoundException
import br.com.eduardo.msorder.registerOrder.model.messaging.OrderConfirmedMessage
import br.com.eduardo.msorder.registerOrder.model.request.FindAllProductsByIdRequest
import br.com.eduardo.msorder.registerOrder.model.request.RegisterOrderRequest
import feign.FeignException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import javax.validation.Valid

@Service
@Validated
class RegisterOrderService(
    val registerOrderPort: RegisterOrderPort,
    val orderConfirmationPublisherPort: OrderConfirmationPublisherPort,
    val productClient: ProductClient,
    val customerClient: CustomerClient
) : RegisterOrderUseCase {

    private val logger = LoggerFactory.getLogger(RegisterOrderUseCase::class.java)

    override fun execute(@Valid request: RegisterOrderRequest, cId: String): Order {
        with(request) {
            return toOrder()
                .also {
                    

                    try {
                        customerClient.validCustomer(customer.id, cId)

                    } catch (e: FeignException.NotFound) {
                        logger.error("action=validCustomer, customerId=${customer.id}, " +
                                "status=${HttpStatus.NOT_FOUND}, cId=$cId")
                        throw ResourceNotFoundException("Customer not found")
                    } catch (e: FeignException) {
                        logger.error("action=validCustomer, customerId=${customer.id}, " +
                                "status=${HttpStatus.INTERNAL_SERVER_ERROR}, cId=$cId")
                        throw GenericException("Something went wrong")
                    }
                }
                .also {
                    val ids = items.map { it.product.barcode }.toSet()
                    try {
                        val response = productClient.findAllById(FindAllProductsByIdRequest(ids), cId).body

                        val others = ids.map { response?.ids?.contains(it) }

                        if (others.contains(false))
                            throw ResourceNotFoundException("Products not found")
                    } catch (e: FeignException) {
                        logger.error("${e.message}", e)
                        throw GenericException("Something went wrong")
                    }
                }
                .also { order ->
                    items.forEach { item ->
                        order.addItem(item)
                    }
                    logger.info("action=itemsAdded, tId=$cId")
                }
                .also { order ->
                    order.calcOrder()
                    logger.info("action=orderCalculated, tId=$cId")
                }
                .also { order ->
                    if (request.autoConfirmation) order.confirm()
                    logger.info("action=confirmationOrder, autoConfirmation=${request.autoConfirmation}, tId=$cId")
                }
                .also { order ->
                    registerOrderPort.register(order)
                    logger.info("action=orderSaved, tId=$cId")
                }
                .also { order ->
                    if (order.status == OrderStatus.CONFIRMED)
                        orderConfirmationPublisherPort.publish(OrderConfirmedMessage(order), cId).also {
                            logger.info("action=messageSent, orderStatus=${order.status}, orderId=${order.id},tId=$cId")
                        }
                }
        }

    }
}