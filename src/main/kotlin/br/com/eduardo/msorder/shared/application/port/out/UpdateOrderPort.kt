package br.com.eduardo.msorder.shared.application.port.out

import br.com.eduardo.msorder.shared.model.Order

interface UpdateOrderPort {

    fun update(id: String, companyId: String, order: Order)
}
