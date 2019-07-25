package io.pactflow.providerstatesexample.provider

import org.h2.server.web.WebServlet
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.rest.core.config.RepositoryRestConfiguration
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer

@Configuration
class RepositoryConfig : RepositoryRestConfigurer {
  override fun configureRepositoryRestConfiguration(config: RepositoryRestConfiguration) {
    config.exposeIdsFor(Account::class.java, AccountNumber::class.java)
  }
}

@SpringBootApplication
@EnableJpaAuditing
class App {
  @Bean
  fun h2servletRegistration(): ServletRegistrationBean<*> {
    val registrationBean = ServletRegistrationBean(WebServlet())
    registrationBean.addUrlMappings("/console/*")
    return registrationBean
  }
}

fun main(args: Array<String>) {
  SpringApplication.run(App::class.java, *args)
}
