package br.com.eduardo.msorder.registerOrder.adapter.`in`.web

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/orders")
class ConfirmationOrderController {

    @PatchMapping("/{orderId}/confirm")
    fun confirmOrder(
        @PathVariable orderId: String
    ): ResponseEntity<Any> {
        return ResponseEntity.ok().build()
    }
}