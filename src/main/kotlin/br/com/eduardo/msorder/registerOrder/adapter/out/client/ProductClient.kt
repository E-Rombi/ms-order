package br.com.eduardo.msorder.registerOrder.adapter.out.client

import br.com.eduardo.msorder.registerOrder.model.request.FindAllProductsByIdRequest
import br.com.eduardo.msorder.registerOrder.model.response.FindAllProductsByIdResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@FeignClient(name = "\${clients.ms-product.name}", url = "\${clients.ms-product.url}")
interface ProductClient {

    @PostMapping("\${clients.ms-product.routes.findAllById}")
    fun findAllById(
        @RequestBody request: FindAllProductsByIdRequest,
        @RequestHeader(required = false, name = "Correlation-id") cId:String
    ): ResponseEntity<FindAllProductsByIdResponse>
}