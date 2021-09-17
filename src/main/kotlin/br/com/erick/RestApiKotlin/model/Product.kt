package br.com.erick.RestApiKotlin.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity(name = "products")
data class Product(
    @Id @GeneratedValue
    var id: Long? = 0,
    var name: String,
    var description: String
)