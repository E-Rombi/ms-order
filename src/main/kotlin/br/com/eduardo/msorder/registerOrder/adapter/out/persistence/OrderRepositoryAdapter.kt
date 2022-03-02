package br.com.eduardo.msorder.registerOrder.adapter.out.persistence

import br.com.eduardo.msorder.registerOrder.application.port.out.RegisterOrderPort
import br.com.eduardo.msorder.registerOrder.model.Order
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import org.springframework.stereotype.Repository

@Repository
class OrderRepositoryAdapter(
    val dynamoDBMapper: DynamoDBMapper
) : RegisterOrderPort {

    override fun register(order: Order): Order {
        dynamoDBMapper.save(order)
        return order
    }
}