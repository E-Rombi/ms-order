package br.com.eduardo.msorder.registerOrder.model

import br.com.eduardo.msorder.registerOrder.model.request.ItemDiscountInfo
import br.com.eduardo.msorder.registerOrder.model.request.ItemRequest
import br.com.eduardo.msorder.registerOrder.model.request.ProductInfo
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate

class Order(
    val issueDate: Instant,
    val deliveryDate: LocalDate,
    val customer: Customer,
    val seller: Seller
) {
    val items: MutableSet<Item> = mutableSetOf()

    fun addItem(item: ItemRequest) {
        this.items.add(
            Item(
                item.productInfo,
                item.quantity,
                item.price,
                item.discount
            ).also { it.calcTotal() }
        )
    }

    fun calcTotal() {

    }
}

data class Customer(
    val id: String,
    val fantasyName: String
)

data class Seller(
    val id: String,
    val fantasyName: String
)

class Item(
    val productInfo: ProductInfo,
    val quantity: BigDecimal,
    val price: BigDecimal,
    val discountInfo: ItemDiscountInfo
) {
    var discount: BigDecimal = BigDecimal.ZERO
    var total: BigDecimal = BigDecimal.ZERO

    fun calcTotal() {
        val totalWithOutDiscount = price.multiply(quantity)

        discount = discountInfo.type.calculate(totalWithOutDiscount, discountInfo.value)

        total = totalWithOutDiscount.subtract(discount)
    }
}

enum class ItemDiscountType {
    VALUE {
        override fun calculate(currentTotal: BigDecimal, amount: BigDecimal): BigDecimal = amount
    },
    PERCENT {
        override fun calculate(currentTotal: BigDecimal, amount: BigDecimal): BigDecimal {
            return currentTotal.multiply(amount)
        }
    };

    abstract fun calculate(currentTotal: BigDecimal, amount: BigDecimal = BigDecimal.ZERO): BigDecimal
}



