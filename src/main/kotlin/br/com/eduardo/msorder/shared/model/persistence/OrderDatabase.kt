package br.com.eduardo.msorder.shared.model.persistence

import br.com.eduardo.msorder.shared.model.ItemDiscountType
import br.com.eduardo.msorder.shared.model.OrderStatus
import com.amazonaws.services.dynamodbv2.datamodeling.*
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@DynamoDBTable(tableName = "order")
class OrderDatabase {
    @field:DynamoDBHashKey
    var id: String? = null

    @field:DynamoDBRangeKey
    var companyId: String? = null

    @field:DynamoDBAttribute
    var issueDate: String? = null

    @field:DynamoDBAttribute
    var deliveryDate: String? = null

    @field:DynamoDBAttribute
    var customer: CustomerDatabase? = null

    @field:DynamoDBAttribute
    var seller: SellerDatabase? = null

    @field:DynamoDBAttribute @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.L)
    var items: MutableSet<ItemDatabase>? = null

    @field:DynamoDBAttribute @field:DynamoDBTypeConvertedEnum
    var status: OrderStatus? = null

    @field:DynamoDBAttribute
    var confirmationDate: String? = null

    @field:DynamoDBAttribute
    var totalQuantities: BigDecimal? = null

    @field:DynamoDBAttribute
    var totalProducts: BigDecimal? = null

    @field:DynamoDBAttribute
    var totalDiscount: BigDecimal? = null

    @field:DynamoDBAttribute
    var totalOrder: BigDecimal? = null

    @field:DynamoDBAttribute
    @field:DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    var createdAt: String? = null

    @field:DynamoDBAttribute
    @field:DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    var updatedAt: String? = null
}

@DynamoDBDocument
class CustomerDatabase {
    @field:DynamoDBAttribute var id: String? = null
    @field:DynamoDBAttribute var fantasyName: String? = null
}


@DynamoDBDocument
class SellerDatabase {
    @field:DynamoDBAttribute var id: String? = null
    @field:DynamoDBAttribute var fantasyName: String? = null
}

@DynamoDBDocument
class ItemDatabase {
    @field:DynamoDBAttribute var product: ProductDatabase? = null
    @field:DynamoDBAttribute var quantity: BigDecimal? = null
    @field:DynamoDBAttribute var price: BigDecimal? = null
    @field:DynamoDBAttribute var discountInfo: ItemDiscountDatabase? = null
    @field:DynamoDBAttribute var totalProduct: BigDecimal? = null
    @field:DynamoDBAttribute var totalDiscountItem: BigDecimal? = null
    @field:DynamoDBAttribute var totalItem: BigDecimal? = null
}

@DynamoDBDocument
class ProductDatabase {
    @field:DynamoDBAttribute var barcode: String? = null
    @field:DynamoDBAttribute var description: String? = null
}

@DynamoDBDocument
class ItemDiscountDatabase {
    @field:DynamoDBTypeConvertedEnum
    @field:DynamoDBAttribute
    var type: ItemDiscountType? = null
    @field:DynamoDBAttribute
    var value: BigDecimal? = null
}