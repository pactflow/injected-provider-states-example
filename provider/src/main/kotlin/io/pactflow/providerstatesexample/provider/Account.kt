package io.pactflow.providerstatesexample.provider

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.data.repository.PagingAndSortingRepository
import java.util.Date
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToOne

@Entity
class AccountNumber(@Id @GeneratedValue(strategy=GenerationType.TABLE) val id: Long)

@Entity
@EntityListeners(AuditingEntityListener::class)
class Account @JvmOverloads constructor(
  @Id @GeneratedValue(strategy=GenerationType.AUTO) var id: Long,
  @Version var version: Long,
  var name: String,
  @OneToOne var accountNumber: AccountNumber,
  var accountRef: String,
  @CreatedDate var createdDate: Date? = null,
  @LastModifiedDate var lastModifiedDate: Date? = null
)

interface AcountNumberRepository: PagingAndSortingRepository<AccountNumber, Long>

interface AcountRepository: PagingAndSortingRepository<Account, Long>
