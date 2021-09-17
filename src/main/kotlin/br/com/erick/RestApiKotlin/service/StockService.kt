package br.com.erick.RestApiKotlin.service

import br.com.erick.RestApiKotlin.model.Product
import br.com.erick.RestApiKotlin.model.Stock
import br.com.erick.RestApiKotlin.model.UpdateProduct
import java.util.*

interface StockService {
    fun create(product: Product): Product

    fun getAllProduct(): List<Product>

    fun getAllStock(): List<Stock>

    fun getbyId( id: Long ) : Optional<Product>

    fun update(id: Long, updateProduct: UpdateProduct) : Optional<Product>

    fun delete(id: Long)

    fun updateStock(id: Long, quantity: Int)

    fun getProductById(id: Long): Optional<Stock>
}