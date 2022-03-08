package br.com.eduardo.msorder.shared.application.port.`in`

interface ConfirmationOrderUseCase {

    fun confirm(orderId: String, companyId: String, cId: String)
}
