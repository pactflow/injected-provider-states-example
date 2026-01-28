package io.pactflow.providerstatesexample.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.restclient.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.core.ParameterizedTypeReference
import org.springframework.hateoas.EntityModel
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

data class Account(
  var id: Int,
  var accountNumber: AccountNumber?,
  var version: Int,
  var name: String = "",
  var accountRef: String = "",
  var createdDate: String = "",
  var lastModifiedDate: String = ""
)

data class AccountNumber(var id: Int)

@RestController
class TransactionController {

  @Autowired
  private lateinit var restTemplate: RestTemplate

  @Value("\${provider.url}")
  lateinit var providerUrl: String

  private val logger = KotlinLogging.logger {} 

  @RequestMapping("/transactions", method = [RequestMethod.POST])
  fun new(@RequestParam accountNumber: Int): Map<String, Any> {
    logger.info { "Creating a new transaction for account $accountNumber" }
    val url = "$providerUrl/accounts/search/findOneByAccountNumberId?accountNumber=$accountNumber"
    logger.info { "Getting account details -> $url" }
    val responseEntity = restTemplate.exchange(url, HttpMethod.GET, null,
    object : ParameterizedTypeReference<EntityModel<Account>>(){})
      return if (responseEntity.statusCode == HttpStatus.OK) {
      logger.info { "Got response ${responseEntity.body}" }
      logger.info { "          Id ${responseEntity.body?.content?.id}" }
      val account: Account? = responseEntity.body?.content
      logger.info { "     Content $account" }
      if (account != null) {
        mapOf("account" to account)
      } else {
        emptyMap()
      }
    } else emptyMap()
  }

}

@SpringBootApplication
class App {
  @Bean
  fun restTemplate(builder: RestTemplateBuilder) = builder.build()

  @Bean
  fun objectMapper() = ObjectMapper().registerKotlinModule()
}

fun main(args: Array<String>) {
  SpringApplication.run(App::class.java, *args)
}
