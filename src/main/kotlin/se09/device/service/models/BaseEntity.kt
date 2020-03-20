package se09.device.service.models

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.GenericGenerator
import java.time.Instant
import javax.persistence.*

@MappedSuperclass
abstract class BaseEntity {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue
    open lateinit var id: String

    @Column(name = "updated_at")
    open var updatedAt: Instant = Instant.now()
    @Column(name = "created_at")
    open var createdAt: Instant = Instant.now()

    @JsonIgnore
    @Column(name = "deleted_at")
    open var deletedAt: Instant? = null

    @PrePersist
    open fun prePersist() {
        updatedAt = Instant.now()
        createdAt = updatedAt
    }

    @PreUpdate
    fun preUpdate() {
        updatedAt = Instant.now()
    }

}