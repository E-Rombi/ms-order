package br.com.eduardo.msorder.registerOrder.adapter.`in`.web

import br.com.eduardo.msorder.registerOrder.application.port.`in`.RegisterOrderUseCase
import br.com.eduardo.msorder.registerOrder.model.request.RegisterOrderRequest
import org.slf4j.LoggerFactory
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
        @RequestHeader(required = false, name = "Tracer-id") tracerId: String?,
        @RequestBody request: RegisterOrderRequest,
        uriComponentsBuilder: UriComponentsBuilder
    ): ResponseEntity<Any> {
        val tId = tracerId ?: UUID.randomUUID().toString()

        val order = registerOrderUseCase.execute(request, tId)

        return ResponseEntity.ok().build()
    }

}
