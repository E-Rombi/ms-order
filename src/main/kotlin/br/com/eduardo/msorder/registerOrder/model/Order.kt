package br.com.eduardo.msorder.registerOrder.model

import br.com.eduardo.msorder.registerOrder.model.request.ItemRequest
import br.com.eduardo.msorder.registerOrder.model.request.ProductInfo
import com.amazonaws.services.dynamodbv2.datamodeling.*
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.util.*

@DynamoDBTable(tableName = "order")
class Order(
    @field:DynamoDBAttribute
    var companyId: String,
    @field:DynamoDBAttribute @field:DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    var issueDate: Instant,
    @field:DynamoDBAttribute @field:DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    var deliveryDate: LocalDate,
    @field:DynamoDBAttribute
    var customer: Customer,
    @field:DynamoDBAttribute
    var seller: Seller
) {
    @field:DynamoDBHashKey @field:DynamoDBAutoGeneratedKey
    var id: String? = null

    @field:DynamoDBAttribute @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.L)
    var items: MutableSet<Item> = mutableSetOf()

    @field:DynamoDBAttribute @field:DynamoDBTypeConvertedEnum
    var status: OrderStatus = OrderStatus.OPENNED

    @field:DynamoDBAttribute @field:DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    var confirmationDate: Instant? = null

    @field:DynamoDBAttribute
    var totalProducts: BigDecimal = BigDecimal.ZERO

    @field:DynamoDBAttribute
    var totalDiscount: BigDecimal = BigDecimal.ZERO

    @field:DynamoDBAttribute
    var total: BigDecimal = BigDecimal.ZERO

    @field:DynamoDBAttribute
    @field:DynamoDBAutoGeneratedTimestamp(strategy = DynamoDBAutoGenerateStrategy.CREATE)
    @field:DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    var createdAt: Date? = null

    @field:DynamoDBAttribute
    @field:DynamoDBAutoGeneratedTimestamp
    @field:DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    var updatedAt: Date? = null

    fun addItem(item: ItemRequest) {
        this.items.add(
            Item(
                item.product.toProduct(),
                item.quantity,
                item.price,
                item.discount.toItemDiscount()
            ).also { it.calcItem() }
        )
    }

    fun calcOrder() {
        this.totalProducts = this.items.map { it.totalProduct }.sumOf { it }
        this.totalDiscount = this.items.map { it.totalDiscountItem }.sumOf { it }
        this.total = this.items.map { it.totalItem }.sumOf { it }
    }

    fun confirm() {
        apply {
            status = OrderStatus.CONFIRMED
            confirmationDate = Instant.now()
        }
    }
}

@DynamoDBDocument
data class Customer(
    @field:DynamoDBAttribute val id: String,
    @field:DynamoDBAttribute val fantasyName: String
)

@DynamoDBDocument
data class Seller(
    @field:DynamoDBAttribute val id: String,
    @field:DynamoDBAttribute val fantasyName: String
)

@DynamoDBDocument
class Item(
    @field:DynamoDBAttribute val product: Product,
    @field:DynamoDBAttribute val quantity: BigDecimal,
    @field:DynamoDBAttribute val price: BigDecimal,
    @field:DynamoDBAttribute val discountInfo: ItemDiscount
) {
    @field:DynamoDBAttribute var totalProduct: BigDecimal = BigDecimal.ZERO
    @field:DynamoDBAttribute var totalDiscountItem: BigDecimal = BigDecimal.ZERO
    @field:DynamoDBAttribute var totalItem: BigDecimal = BigDecimal.ZERO

    fun calcItem() {
        totalProduct = price.multiply(quantity)

        totalDiscountItem = discountInfo.type.calculate(totalProduct, discountInfo.value)

        totalItem = totalProduct.subtract(totalDiscountItem)
    }
}

@DynamoDBDocument
data class Product(
    @field:DynamoDBAttribute val barcode: String,
    @field:DynamoDBAttribute val description: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductInfo

        if (barcode != other.barcode) return false

        return true
    }

    override fun hashCode(): Int {
        return barcode.hashCode()
    }
}

@DynamoDBDocument
data class ItemDiscount(
    @field:DynamoDBTypeConvertedEnum
    @field:DynamoDBAttribute
    val type: ItemDiscountType,
    @field:DynamoDBAttribute
    val value: BigDecimal
)

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

enum class OrderStatus {
    OPENNED, CANCELLED, CONFIRMED, SEPARATED, DELIVERED;
}