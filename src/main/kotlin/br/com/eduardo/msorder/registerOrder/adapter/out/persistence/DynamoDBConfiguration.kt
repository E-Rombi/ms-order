package br.com.eduardo.msorder.registerOrder.adapter.out.persistence

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class DynamoDBConfig {

    @Value("\${amazon.aws.accesskey}")
    private lateinit var amazonAWSAccessKey: String

    @Value("\${amazon.aws.secretkey}")
    private lateinit var amazonAWSSecretKey: String

    @Bean
    fun buildDynamoDbMapper(): DynamoDBMapper {
        return DynamoDBMapper(buildAmazonDynamoDb())
    }

    private fun buildAmazonDynamoDb(): AmazonDynamoDB {
        return AmazonDynamoDBClientBuilder
            .standard()
            .withEndpointConfiguration(
                AwsClientBuilder.EndpointConfiguration(
                    "dynamodb.us-east-1.amazonaws.com",
                    "us-east-1"
                )
            )
            .withCredentials(
                AWSStaticCredentialsProvider(
                    BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey)
                )
            ).build()

    }
}