package br.com.erick.RestApiKotlin.model

import javax.persistence.GeneratedValue
import javax.persistence.Id

open class ProductStock(
    @Id @GeneratedValue
    var id: Long? = null,
    var name: String,
    var description: String,
    var quantity: Int
)