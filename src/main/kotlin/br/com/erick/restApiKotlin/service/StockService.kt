package br.com.erick.restApiKotlin.service

import br.com.erick.restApiKotlin.model.ProductDTO
import br.com.erick.restApiKotlin.model.StockDTO
import br.com.erick.restApiKotlin.model.ProductStockDTO
import java.util.*

interface StockService {
    fun createProduct(productStockDTO: ProductStockDTO): ProductDTO

    fun getAllProduct(): List<ProductDTO>

    fun getAllStock(): List<StockDTO>

    fun getStockById(id: Long): Optional<StockDTO>

    fun getProductById( id: Long ) : Optional<ProductDTO>

    fun updateProduct(id: Long, productStockDTO: ProductStockDTO) : Optional<ProductDTO>

    fun updateQuantityStock(id: Long, quantity: Int) : Optional<StockDTO>

    fun updateStock(id: Long, quantity: Int)

    fun deleteProduct(id: Long)

    fun deleteStock(id: Long)
}