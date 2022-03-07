package br.com.eduardo.msorder.registerOrder.adapter.out.publisher

import br.com.eduardo.msorder.registerOrder.application.port.out.OrderConfirmationPublisherPort
import br.com.eduardo.msorder.registerOrder.model.messaging.EventTemplate
import br.com.eduardo.msorder.registerOrder.model.messaging.OrderConfirmedMessage
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate
import org.springframework.messaging.MessagingException
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class OrderConfirmationPublisher(
    private val notificationMessagingTemplate: NotificationMessagingTemplate,
    @Value("\${messaging.order.event-type.order-confirmed.topic}")
    private val topic: String,
    @Value("\${messaging.order.source}")
    private val source: String
) : OrderConfirmationPublisherPort {

    private val logger = LoggerFactory.getLogger(OrderConfirmationPublisherPort::class.java)

    override fun publish(data: OrderConfirmedMessage, tId: String) {
        try {
            logger.info("action=orderConfirmedPublishing, topic=$topic, orderId=, tId=$tId")

            notificationMessagingTemplate.convertAndSend(topic, createEventTemplate(data, tId))
        } catch (e: MessagingException) {
            logger.error("action=orderConfirmedPublishing, topic=$topic, tId=$tId")
        }
    }

    private fun createEventTemplate(data: OrderConfirmedMessage, tId: String): EventTemplate<OrderConfirmedMessage> {
        return EventTemplate(
            source = source,
            timestamp = Instant.now(),
            tId = tId,
            data = data
        )
    }
}