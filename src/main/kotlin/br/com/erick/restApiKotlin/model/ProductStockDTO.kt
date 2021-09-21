package br.com.erick.restApiKotlin.model

import javax.persistence.GeneratedValue
import javax.persistence.Id

data class ProductStockDTO(
    @Id @GeneratedValue
    var id: Long? = null,
    var name: String,
    var description: String,
    var quantity: Int
)