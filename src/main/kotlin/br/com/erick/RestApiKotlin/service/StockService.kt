package br.com.erick.RestApiKotlin.service

import br.com.erick.RestApiKotlin.model.Product
import br.com.erick.RestApiKotlin.model.Stock
import br.com.erick.RestApiKotlin.model.ProductStock
import java.util.*

interface StockService {
    fun create(productStock: ProductStock): Product

    fun getAllProduct(): List<Product>

    fun getAllStock(): List<Stock>

    fun getbyId( id: Long ) : Optional<Product>

    fun update(id: Long, productStock: ProductStock) : Optional<Product>

    fun addProduct(id: Long, quantity: Int) : Optional<Stock>

    fun delete(id: Long)

    fun updateStock(id: Long, quantity: Int)

    fun getStockByProductId(id: Long): Optional<Stock>

    fun getStockbyId(id: Long): Optional<Stock>

    fun deleteStock(id: Long)
}