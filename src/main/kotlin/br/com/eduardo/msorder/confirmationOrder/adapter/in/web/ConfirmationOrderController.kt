package br.com.eduardo.msorder.confirmationOrder.adapter.`in`.web

import br.com.eduardo.msorder.shared.application.port.`in`.ConfirmationOrderUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/orders")
class ConfirmationOrderController(
    val confirmationOrderUseCase: ConfirmationOrderUseCase
) {

    @PatchMapping("/{orderId}/confirm")
    fun confirmOrder(
        @RequestHeader(required = false, value = "Correlation-id") correlationId: String?,
        @RequestParam companyId: String,
        @PathVariable orderId: String
    ): ResponseEntity<Any> {
        val cId = correlationId ?: UUID.randomUUID().toString()

        confirmationOrderUseCase.confirm(orderId, companyId, cId)

        return ResponseEntity.ok().build()
    }
}
