package io.pactflow.providerstatesexample.provider

import au.com.dius.pact.provider.junitsupport.Provider
import au.com.dius.pact.provider.junitsupport.State
import au.com.dius.pact.provider.junitsupport.loader.PactFolder
import au.com.dius.pact.provider.junit5.PactVerificationContext
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider
import org.junit.jupiter.api.TestTemplate
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
// import org.junit.jupiter.api.BeforeEach;
// import au.com.dius.pact.provider.junit5.HttpTestTarget;

@Provider("AccountService")
@PactFolder("pacts")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class PactVerificationTest {

  @LocalServerPort
  protected var port: Int = 0

  @Autowired
  lateinit var acountRepository: AcountRepository

  // @BeforeEach
  // fun setup(context: PactVerificationContext) {
  //   context.setTarget(new HttpTestTarget("localhost", port))
  // }

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
