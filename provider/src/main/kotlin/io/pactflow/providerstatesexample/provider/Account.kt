package io.pactflow.providerstatesexample.provider

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Date
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToOne

@Entity
class AccountNumber(@Id @GeneratedValue(strategy=GenerationType.AUTO) val id: Long)

@Entity
@EntityListeners(AuditingEntityListener::class)
class Account @JvmOverloads constructor(
  @Id @GeneratedValue(strategy=GenerationType.AUTO) var id: Long,
  @Version var version: Long,
  var name: String,
  @OneToOne(cascade=[CascadeType.ALL]) var accountNumber: AccountNumber,
  var accountRef: String,
  @CreatedDate var createdDate: Date? = null,
  @LastModifiedDate var lastModifiedDate: Date? = null
)

interface AccountRepository: JpaRepository<Account, Long> {
  fun findOneByAccountNumberId(accountNumber: Long): Account
}
