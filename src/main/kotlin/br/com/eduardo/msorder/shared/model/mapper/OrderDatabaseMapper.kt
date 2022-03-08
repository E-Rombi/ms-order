package br.com.eduardo.msorder.shared.model.mapper

import br.com.eduardo.msorder.shared.model.*
import br.com.eduardo.msorder.shared.model.persistence.*
import java.time.Instant
import java.time.LocalDate

class OrderDatabaseMapper {

    companion object {

        fun toOrder(orderDatabase: OrderDatabase): Order {
            with(orderDatabase) {
                return Order(
                    this.companyId.orEmpty(),
                    Instant.parse(this.issueDate!!),
                    LocalDate.parse(this.deliveryDate!!),
                    toCustomer(this.customer!!),
                    toSeller(this.seller!!)
                ).apply {
                    id = orderDatabase.id!!
                    items = toItems(orderDatabase.items!!)
                    totalQuantities = orderDatabase.totalQuantities!!
                    totalDiscount = orderDatabase.totalDiscount!!
                    totalProducts = orderDatabase.totalProducts!!
                    totalOrder = orderDatabase.totalOrder!!
                    createdAt = orderDatabase.createdAt?.let { Instant.parse(it) }
                    updatedAt = orderDatabase.updatedAt?.let { Instant.parse(it) }
                }
            }
        }

        private fun toCustomer(customerDatabase: CustomerDatabase): Customer {
            return Customer(customerDatabase.id!!, customerDatabase.fantasyName!!)
        }

        private fun toSeller(sellerDatabase: SellerDatabase): Seller {
            return Seller(sellerDatabase.id!!, sellerDatabase.fantasyName!!)
        }

        private fun toItems(mutableSet: MutableSet<ItemDatabase>): MutableSet<Item> {
            return mutableSet.map {
                Item(
                    toProduct(it.product!!),
                    it.quantity!!,
                    it.price!!,
                    toItemDiscount(it.discountInfo!!)
                ).apply {
                    totalProduct = it.totalProduct!!
                    totalDiscountItem = it.totalDiscountItem!!
                    totalItem = it.totalItem!!
                }
            }.toMutableSet()
        }

        private fun toProduct(productDatabase: ProductDatabase): Product {
            return Product(productDatabase.barcode!!, productDatabase.description!!)
        }

        private fun toItemDiscount(itemDiscountDatabase: ItemDiscountDatabase): ItemDiscount {
            return ItemDiscount(itemDiscountDatabase.type!!, itemDiscountDatabase.value!!)
        }
    }
}