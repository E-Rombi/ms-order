package br.com.eduardo.msorder.registerOrder.model.messaging

import br.com.eduardo.msorder.registerOrder.model.Customer
import br.com.eduardo.msorder.registerOrder.model.Item
import br.com.eduardo.msorder.registerOrder.model.Order
import br.com.eduardo.msorder.registerOrder.model.Product
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate

class OrderConfirmedMessage(
    val id: String,
    val issueDate: Instant,
    val confirmationDate: Instant,
    val deliveryDate: LocalDate,
    val customer: Customer,
    val items: Set<ItemSeparation>,
    val totalQuantities: BigDecimal
) {
    constructor(order: Order) : this(
        order.id!!,
        order.issueDate,
        order.confirmationDate!!,
        order.deliveryDate,
        order.customer,
        order.items.map { ItemSeparation(it) }.toSet(),
        order.totalQuantities
    )
}

class ItemSeparation(
    val product: Product,
    val quantity: BigDecimal
) {
    constructor(item: Item): this(item.product, item.quantity)
}
