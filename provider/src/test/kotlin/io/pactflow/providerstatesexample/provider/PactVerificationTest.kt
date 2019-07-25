package io.pactflow.providerstatesexample.provider

import au.com.dius.pact.provider.junit.Provider
import au.com.dius.pact.provider.junit.State
import au.com.dius.pact.provider.junit.loader.PactFolder
import au.com.dius.pact.provider.junit5.PactVerificationContext
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider
import org.junit.jupiter.api.TestTemplate
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@Provider("AccountService")
@PactFolder("pacts")
@SpringBootTest
class PactVerificationTest {

  @Autowired
  lateinit var acountRepository: AcountRepository

  @TestTemplate
  @ExtendWith(PactVerificationInvocationContextProvider::class)
  fun testTemplate(context: PactVerificationContext) {
    context.verifyInteraction()
  }

  @State("Account Test001 exists")
  fun createAccount(params: Map<String, String>): Map<String, Any> {
    val account = Account(0, 0, params["accountRef"]!!, AccountNumber(0), params["accountRef"]!!)
    val persistedAccount = acountRepository.save(account)
    return mapOf("accountNumber" to persistedAccount.accountNumber.id)
  }

}
