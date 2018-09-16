package pl.sparkidea.service.invoicespl

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@EnableDiscoveryClient
@SpringBootApplication
@EnableReactiveMongoRepositories
class InvoiceApp

fun main(args: Array<String>) {
    runApplication<InvoiceApp>(*args)
}
