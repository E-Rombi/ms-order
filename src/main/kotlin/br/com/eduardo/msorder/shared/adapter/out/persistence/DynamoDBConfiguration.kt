package br.com.eduardo.msorder.shared.adapter.out.persistence

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DynamoDBConfig {

    @Value("\${cloud.aws.region.static}")
    private lateinit var region: String

    @Value("\${cloud.aws.credentials.accesskey}")
    private lateinit var amazonAWSAccessKey: String

    @Value("\${cloud.aws.credentials.secretkey}")
    private lateinit var amazonAWSSecretKey: String

    @Bean
    fun buildDynamoDbMapper(): DynamoDBMapper {
        return DynamoDBMapper(buildAmazonDynamoDb())
    }

    private fun buildAmazonDynamoDb(): AmazonDynamoDB {
        return AmazonDynamoDBClientBuilder
            .standard()
            .withRegion(region)
            .withCredentials(
                AWSStaticCredentialsProvider(
                    BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey)
                )
            ).build()

    }
}