package br.com.erick.restApiKotlin.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity(name = "stocks")
data class StockDTO (
    @Id @GeneratedValue
    var id: Long? = null,
    val productId: Long,
    val quantityProduct: Int = 0
)