package br.com.eduardo.msorder.registerOrder.adapter.`in`.web

import br.com.eduardo.msorder.registerOrder.application.port.`in`.RegisterOrderUseCase
import br.com.eduardo.msorder.registerOrder.model.request.RegisterOrderRequest
import br.com.eduardo.msorder.registerOrder.model.response.OrderRegisteredResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import java.util.*

@RestController
@RequestMapping("/orders")
class RegisterOrderController(
    val registerOrderUseCase: RegisterOrderUseCase
) {

    private val logger = LoggerFactory.getLogger(RegisterOrderController::class.java)

    @PostMapping
    fun registerOrder(
        @RequestHeader(required = false, name = "Correlation-id") correlationId: String?,
        @RequestBody request: RegisterOrderRequest,
        uriComponentsBuilder: UriComponentsBuilder
    ): ResponseEntity<OrderRegisteredResponse> {
        val cId = correlationId ?: UUID.randomUUID().toString()

        val order = registerOrderUseCase.execute(request, cId)

        val uri = uriComponentsBuilder.path("/orders/{id}").buildAndExpand(order.id).toUri()
        return ResponseEntity.created(uri).body(
            OrderRegisteredResponse(order.id!!, order.status)
        ).also {
            logger.info("action=registerOrder, status=${HttpStatus.CREATED}, cId=$cId")
        }
    }

}
