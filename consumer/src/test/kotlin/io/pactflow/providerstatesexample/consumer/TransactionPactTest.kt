package io.pactflow.providerstatesexample.consumer

import au.com.dius.pact.consumer.MockServer
import au.com.dius.pact.consumer.dsl.PactDslJsonBody
import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.core.model.annotations.Pact
import au.com.dius.pact.core.model.matchingrules.RegexMatcher
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasKey
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import au.com.dius.pact.core.model.PactSpecVersion; // required for v4.6.x to set pactVersion

@ExtendWith(PactConsumerTestExt::class)
@PactTestFor(providerName = "AccountService")
@SpringBootTest
class TransactionPactTest {

  @Autowired
  private lateinit var controller: TransactionController

  @Pact(consumer = "TransactionService")
  fun accounts(builder: PactDslWithProvider) = builder
      .given("Account Test001 exists", "accountRef", "Test001")
      .uponReceiving("a request to get the account details")
      .path("/accounts/search/findOneByAccountNumberId")
      .queryParameterFromProviderState("accountNumber", "\${accountNumber}", "100")
      .willRespondWith()
      .status(200)
      .headers(mapOf("Content-Type" to "application/hal+json"))
      .body(
        PactDslJsonBody()
          .integerType("id", 1)
          .integerType("version", 0)
          .stringType("name", "Test")
          .stringValue("accountRef", "Test001")
          .datetime("createdDate", "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
          .datetime("lastModifiedDate", "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
          .`object`("accountNumber")
            .valueFromProviderState("id", "\${accountNumber}", 100)
          .closeObject()!!
            .`object`("_links")
            .`object`("self")
              .matchUrl("href", "http://localhost:8080" as String?, "accounts" as Any, RegexMatcher("\\d+", "100") as Any)
            .closeObject()!!
            .`object`("account")
            .matchUrl("href", "http://localhost:8080" as String?, "accounts" as Any, RegexMatcher("\\d+", "200") as Any)
            .closeObject()!!
          .closeObject() as PactDslJsonBody
      ).toPact()

  @Test
  @PactTestFor(pactMethod = "accounts", pactVersion = PactSpecVersion.V3)
  fun testNewTransaction(mockServer: MockServer) {
    controller.providerUrl = mockServer.getUrl()
    val result = controller.new(100)
    assertThat(result, hasKey("account"))
    val account = result["account"] as Account
    assertThat(account.accountNumber?.id, `is`(equalTo(100)))
  }
}
