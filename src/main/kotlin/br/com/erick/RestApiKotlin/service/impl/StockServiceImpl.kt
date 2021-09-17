package br.com.erick.RestApiKotlin.service.impl

import br.com.erick.RestApiKotlin.model.Product
import br.com.erick.RestApiKotlin.model.Stock
import br.com.erick.RestApiKotlin.model.UpdateProduct
import br.com.erick.RestApiKotlin.repository.ProductRepository
import br.com.erick.RestApiKotlin.repository.StockRepository
import br.com.erick.RestApiKotlin.service.StockService
import org.springframework.stereotype.Service
import java.lang.RuntimeException
import java.util.*

@Service
class StockServiceImpl(private val productRepository: ProductRepository, private val stockRepository: StockRepository) :
    StockService {
    override fun create(product: Product): Product {
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

    override fun getbyId(id: Long): Optional<Product> {
        return productRepository.findById(id)
    }

    override fun update(id: Long, updateProduct: UpdateProduct): Optional<Product> {
        if(updateProduct.quantity > 0){
            val optional = getbyId(id)
            if(optional.isEmpty) Optional.empty<Product>()
            val quantity = updateProduct.quantity
            updateStock(id, quantity)
            return optional.map {
                val productToUpdate = it.copy(
                    name = updateProduct.name,
                    description = updateProduct.description
                )
                productRepository.save(productToUpdate)
            }
        }
        return Optional.empty()
    }

    override fun delete(id: Long) {
        productRepository.findById(id).map {
            productRepository.delete(it)
        }.orElseThrow{ throw RuntimeException("Id") }
    }

    override fun updateStock(id: Long, quantity: Int) {
        val optional = getbyId(id)
        if(optional.isEmpty) Optional.empty<Stock>()

        val stock: Optional<Stock> = getProductById(id)
        stock.map {
            val stockToUpdate = it.copy(
                quantityProduct = quantity + it.quantityProduct
            )
            stockRepository.save(stockToUpdate)
        }
    }

    override fun getProductById(id: Long): Optional<Stock> {
        return stockRepository.findById(id)
    }
}