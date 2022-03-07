package br.com.eduardo.msorder.registerOrder.model.request

data class FindAllProductsByIdRequest(
    val ids: Set<String>
)