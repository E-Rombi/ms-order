package br.com.eduardo.msorder.registerOrder.adapter.out.publisher

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.sns.AmazonSNSAsync
import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder
import com.amazonaws.services.sqs.AmazonSQSAsync
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.converter.MappingJackson2MessageConverter

@Configuration
class AwsMessagingConfiguration(
    @Value("\${cloud.aws.region.static}") val awsRegion: String,
    @Value("\${cloud.aws.credentials.accesskey}") val awsAccessKey: String,
    @Value("\${cloud.aws.credentials.secretkey}") val awsSecretKey: String,
    @Value("\${sqs.polling-message-size}") val pollingSize: Int,
    @Value("\${sqs.auto-startup}") val autoStartup: Boolean,
) {

    @Bean
    fun notificationMessageTemplate(): NotificationMessagingTemplate {
        return NotificationMessagingTemplate(buildAmazonSNSAsync())
    }

    @Bean
    fun queueMessagingTemplate(): QueueMessagingTemplate {
        return QueueMessagingTemplate(buildAmazonSQSAsync())
    }

    @Bean
    fun simpleMessageListenerContainerFactory(): SimpleMessageListenerContainerFactory {
        val simpleMessageListenerContainerFactory = SimpleMessageListenerContainerFactory()
        simpleMessageListenerContainerFactory.setAmazonSqs(buildAmazonSQSAsync())
        simpleMessageListenerContainerFactory.setMaxNumberOfMessages(pollingSize)
        simpleMessageListenerContainerFactory.setAutoStartup(autoStartup)
        return simpleMessageListenerContainerFactory
    }

    @Bean
    fun queueMessageHandlerFactory(objectMapper: ObjectMapper): QueueMessageHandlerFactory {
        val messageConverter = MappingJackson2MessageConverter()
        messageConverter.objectMapper = objectMapper

        val queueMessageHandlerFactory = QueueMessageHandlerFactory()
        queueMessageHandlerFactory.messageConverters = listOf(messageConverter)

        return queueMessageHandlerFactory
    }

    private fun buildAmazonSNSAsync(): AmazonSNSAsync {
        return AmazonSNSAsyncClientBuilder
            .standard()
            .withRegion(awsRegion)
            .withCredentials(AWSStaticCredentialsProvider(BasicAWSCredentials(awsAccessKey, awsSecretKey)))
            .build()
    }

    private fun buildAmazonSQSAsync(): AmazonSQSAsync {
        return AmazonSQSAsyncClientBuilder
            .standard()
            .withRegion(awsRegion)
            .withCredentials(AWSStaticCredentialsProvider(BasicAWSCredentials(awsAccessKey, awsSecretKey)))
            .build()
    }


}
