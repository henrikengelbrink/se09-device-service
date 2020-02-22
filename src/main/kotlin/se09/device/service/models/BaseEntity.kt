package se09.device.service.models

import com.fasterxml.jackson.annotation.JsonIgnore
import io.micronaut.data.annotation.GeneratedValue
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type
import java.time.Instant
import java.util.*
import javax.persistence.*

@MappedSuperclass
abstract class BaseEntity {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue
    @Type(type="pg-uuid")
    var id: UUID = UUID.randomUUID()

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