package io.pactflow.providerstatesexample.consumer

import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.core.ParameterizedTypeReference
import org.springframework.hateoas.Resource
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import java.net.URI

data class Account(
  var id: Int,
  var accountNumber: Int,
  var version: Int,
  var name: String,
  var accountRef: String,
  var createdDate: String,
  var lastModifiedDate: String
)

data class AccountNumber(var id: Int)

@RestController
class TransactionController {

  @Autowired
  private lateinit var restTemplate: RestTemplate

  @Value("\${provider.url}")
  private lateinit var providerUrl: String

  @RequestMapping("/transactions", method = [RequestMethod.POST])
  fun new(@RequestParam account: Int): Map<String, Any> {
    logger.info { "Creating a new transaction for account $account" }
    val url = "$providerUrl/accounts/$account"
    logger.info { "Getting account details -> $url" }
    val responseEntity = restTemplate.exchange(url, HttpMethod.GET, null,
      object : ParameterizedTypeReference<Resource<Account>>() {})
    return if (responseEntity.statusCode == HttpStatus.OK) {
      logger.info { "Got response ${responseEntity.body}" }
      logger.info { "          Id ${responseEntity.body.id}" }
      val account = responseEntity.body.content
      logger.info { "     Content $account" }
      val accountNumber = restTemplate.exchange(responseEntity.body.getLink("accountNumber").href, HttpMethod.GET, null,
        object : ParameterizedTypeReference<Resource<AccountNumber>>() {}).body
      account.id = URI(responseEntity.body.id.href).path.split("/").last().toInt()
      account.accountNumber = URI(accountNumber.id.href).path.split("/").last().toInt()
      mapOf("account" to account)
    } else emptyMap()
  }

  companion object: KLogging()
}

@SpringBootApplication
class App {
  @Bean
  fun restTemplate(builder: RestTemplateBuilder) = builder.build()
}

fun main(args: Array<String>) {
  SpringApplication.run(App::class.java, *args)
}
