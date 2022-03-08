package br.com.eduardo.msorder.shared.adapter.out.persistence

import br.com.eduardo.msorder.registerOrder.application.port.out.RegisterOrderPort
import br.com.eduardo.msorder.shared.model.Order
import br.com.eduardo.msorder.shared.application.port.out.FindOrderByIdPort
import br.com.eduardo.msorder.shared.application.port.out.UpdateOrderPort
import br.com.eduardo.msorder.shared.model.exception.ResourceNotFoundException
import br.com.eduardo.msorder.shared.model.mapper.OrderDatabaseMapper
import br.com.eduardo.msorder.shared.model.persistence.OrderDatabase
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
class OrderRepositoryAdapter(
    val dynamoDBMapper: DynamoDBMapper
) : RegisterOrderPort, FindOrderByIdPort, UpdateOrderPort {

    override fun register(order: Order): Order {
        order.createdAt = Instant.now()

        dynamoDBMapper.save(order)
        return order
    }

    override fun find(id: String, companyId: String): Order {
        val orderDatabase = dynamoDBMapper.load(OrderDatabase::class.java, id, companyId)
            ?: throw ResourceNotFoundException("Order not found")

        return OrderDatabaseMapper.toOrder(orderDatabase)
    }

    override fun update(id: String, companyId: String, order: Order) {
        order.updatedAt = Instant.now()

        dynamoDBMapper.save(order, buildUpdateExpression(id, companyId))
    }

    private fun buildUpdateExpression(id: String, companyId: String): DynamoDBSaveExpression {
        return DynamoDBSaveExpression()
            .withExpectedEntry(
                "id",
                ExpectedAttributeValue(
                    AttributeValue().withS(id)
                )
            )
            .withExpectedEntry(
                "companyId",
                ExpectedAttributeValue(
                    AttributeValue().withS(companyId)
                )
            )
    }
}