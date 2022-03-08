package br.com.eduardo.msorder.registerOrder.adapter.out.client

import br.com.eduardo.msorder.registerOrder.model.request.FindAllProductsByIdRequest
import br.com.eduardo.msorder.registerOrder.model.response.FindAllProductsByIdResponse
import feign.Body
import feign.Headers
import feign.Param
import org.apache.http.entity.ContentType
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.HttpMethod
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