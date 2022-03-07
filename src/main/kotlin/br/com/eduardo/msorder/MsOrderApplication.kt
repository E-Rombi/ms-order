package br.com.eduardo.msorder

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@SpringBootApplication
class MsOrderApplication

fun main(args: Array<String>) {
    runApplication<MsOrderApplication>(*args)
}
