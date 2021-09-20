package br.com.erick.restApiKotlin.service

import br.com.erick.restApiKotlin.model.Product
import br.com.erick.restApiKotlin.model.Stock
import br.com.erick.restApiKotlin.model.ProductStock
import java.util.*

interface StockService {
    fun createProduct(productStock: ProductStock): Product

    fun getAllProduct(): List<Product>

    fun getAllStock(): List<Stock>

    fun getStockById(id: Long): Optional<Stock>

    fun getProductById( id: Long ) : Optional<Product>

    fun updateProduct(id: Long, productStock: ProductStock) : Optional<Product>

    fun addQuantityStock(id: Long, quantity: Int) : Optional<Stock>

    fun updateStock(id: Long, quantity: Int)

    fun deleteProduct(id: Long)

    fun deleteStock(id: Long)
}