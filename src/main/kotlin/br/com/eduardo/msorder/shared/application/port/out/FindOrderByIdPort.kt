package br.com.eduardo.msorder.shared.application.port.out

import br.com.eduardo.msorder.shared.model.Order

interface FindOrderByIdPort {

    fun find(id: String, companyId: String): Order
}