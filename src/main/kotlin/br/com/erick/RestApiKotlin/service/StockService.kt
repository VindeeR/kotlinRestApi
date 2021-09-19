package br.com.erick.RestApiKotlin.service

import br.com.erick.RestApiKotlin.model.Product
import br.com.erick.RestApiKotlin.model.Stock
import br.com.erick.RestApiKotlin.model.ProductStock
import java.util.*

interface StockService {
    fun createProduct(productStock: ProductStock): Product

    fun getAllProduct(): List<Product>

    fun getAllStock(): List<Stock>

    fun getStockById(id: Long): Optional<Stock>

    fun getProductById( id: Long ) : Optional<Product>

    fun getStockByProductId(id: Long): Optional<Stock>

    fun updateProduct(id: Long, productStock: ProductStock) : Optional<Product>

    fun addQuantityStock(id: Long, quantity: Int) : Optional<Stock>

    fun updateStock(id: Long, quantity: Int)

    fun deleteProduct(id: Long)

    fun deleteStock(id: Long)
}