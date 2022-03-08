package br.com.eduardo.msorder.registerOrder.adapter.out.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@FeignClient(name = "\${clients.ms-customer.name}", url = "\${clients.ms-customer.url}")
interface CustomerClient {

    @GetMapping("\${clients.ms-customer.routes.validCustomer}")
    fun validCustomer(
        @PathVariable customerId: String,
        @RequestHeader(required = false, name = "Correlation-id") cId:String
    ): ResponseEntity<Any>
}