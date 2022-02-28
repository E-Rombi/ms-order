package br.com.eduardo.msorder.registerOrder.model.request

import br.com.eduardo.msorder.registerOrder.model.Customer
import br.com.eduardo.msorder.registerOrder.model.ItemDiscountType
import br.com.eduardo.msorder.registerOrder.model.Order
import br.com.eduardo.msorder.registerOrder.model.Seller
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

data class RegisterOrderRequest(
    @field:NotNull val issueDate: Instant,
    @field:NotNull val autoConfirmation: Boolean,
    @field:NotNull val deliveryDate: LocalDate,
    @field:NotNull @field:Valid val customer: CustomerInfoRequest,
    @field:NotNull @field:Valid val seller: SellerInfoRequest,
    @field:NotNull @field:Valid val items: Set<ItemRequest>
) {
    fun toOrder(): Order {
        return Order(
            this.issueDate,
            this.deliveryDate,
            this.customer.toModel(),
            this.seller.toModel()
        )
    }
}

data class CustomerInfoRequest(
    @field:NotBlank val id: String,
    @field:NotBlank val fantasyName: String
) {
    fun toModel(): Customer = Customer(this.id, this.fantasyName)
}

data class SellerInfoRequest(
    @field:NotBlank val id: String,
    @field:NotBlank val fantasyName: String
) {
    fun toModel(): Seller = Seller(this.id, this.fantasyName)
}

class ItemRequest(
    @field:NotNull @field:Valid val productInfo: ProductInfo,
    @field:NotNull @field:Positive val quantity: BigDecimal,
    @field:NotNull @field:Positive val price: BigDecimal,
    val discount: ItemDiscountInfo
)

class ProductInfo(
    @field:NotBlank val barcode: String,
    @field:NotBlank val description: String
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

data class ItemDiscountInfo(
    val type: ItemDiscountType,
    val value: BigDecimal
)