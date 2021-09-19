package br.com.erick.RestApiKotlin.service.impl

import br.com.erick.RestApiKotlin.model.Product
import br.com.erick.RestApiKotlin.model.Stock
import br.com.erick.RestApiKotlin.model.ProductStock
import br.com.erick.RestApiKotlin.repository.ProductRepository
import br.com.erick.RestApiKotlin.repository.StockRepository
import br.com.erick.RestApiKotlin.service.StockService
import org.springframework.stereotype.Service
import java.lang.RuntimeException
import java.util.*

@Service
class StockServiceImpl(private val productRepository: ProductRepository, private val stockRepository: StockRepository) :
    StockService {
    override fun createProduct(productStock: ProductStock): Product {
        val product = Product(description = productStock.description, name = productStock.name)
        val productAux = productRepository.save(product)
        val stock = Stock(productId = productAux.id!!, quantityProduct = 0)
        stockRepository.save(stock)
        return productAux
    }

    override fun getAllProduct(): List<Product> {
        return productRepository.findAll()
    }

    override fun getAllStock(): List<Stock> {
        return stockRepository.findAll()
    }

    override fun getProductById(id: Long): Optional<Product> {
        return productRepository.findById(id)
    }

    override fun getStockById(id: Long): Optional<Stock> {
        return stockRepository.findById(id)
    }

    override fun getStockByProductId(id: Long): Optional<Stock> {
        return stockRepository.findById(id.plus(1))
    }

    override fun updateProduct(id: Long, productStock: ProductStock): Optional<Product> {
        if (productStock.quantity >= 0) {
            val quantity = productStock.quantity
            updateStock(id, quantity)
        }

        val product = getProductById(id)
        if (product.isEmpty) Optional.empty<Product>()
        when {
            productStock.name == "" && productStock.description == "" -> return Optional.empty<Product>()

            productStock.name == "" -> return product.map {
                val productToUpdate = it.copy(
                    description = productStock.description
                )
                productRepository.save(productToUpdate)
            }

            productStock.description == "" -> return product.map {
                val productToUpdate = it.copy(
                    name = productStock.name
                )
                productRepository.save(productToUpdate)
            }

            else -> return product.map {
                val productToUpdate = it.copy(
                    name = productStock.name,
                    description = productStock.description
                )
                productRepository.save(productToUpdate)
            }
        }
        return Optional.empty()
    }

    override fun addQuantityStock(id: Long, quantity: Int): Optional<Stock> {

        val stock: Optional<Stock> = getStockByProductId(id)
        if(stock.get().quantityProduct + quantity >= 0){
            return stock.map {
                val stockToUpdate = it.copy(
                    quantityProduct = quantity + it.quantityProduct
                )
                stockRepository.save(stockToUpdate)
            }
        }
        return Optional.empty()
    }

    override fun updateStock(id: Long, quantity: Int) {
        val optional = getStockById(id)
        if(optional.isEmpty) Optional.empty<Stock>()

        val stock: Optional<Stock> = getStockByProductId(id)
        stock.map {
            val stockToUpdate = it.copy(
                quantityProduct = quantity
            )
            stockRepository.save(stockToUpdate)
        }
    }

    override fun deleteProduct(id: Long) {
        updateStock(id, 0)
        productRepository.findById(id).map {
            productRepository.delete(it)
        }.orElseThrow{ throw RuntimeException("Id") }
    }

    override fun deleteStock(id: Long){
        when{
            getStockById(id.minus(1)).isEmpty.not() -> deleteProduct(id.minus(1))
        }
        stockRepository.findById(id).map {
            stockRepository.delete(it)
        }.orElseThrow{ throw RuntimeException("Id") }
    }
}