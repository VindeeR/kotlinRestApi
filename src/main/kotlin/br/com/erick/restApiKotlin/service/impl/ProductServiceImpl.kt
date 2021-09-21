package br.com.erick.restApiKotlin.service.impl

import br.com.erick.restApiKotlin.model.ProductDTO
import br.com.erick.restApiKotlin.model.StockDTO
import br.com.erick.restApiKotlin.model.ProductStockDTO
import br.com.erick.restApiKotlin.repository.ProductRepository
import br.com.erick.restApiKotlin.repository.StockRepository
import br.com.erick.restApiKotlin.service.StockService
import org.springframework.stereotype.Service
import java.lang.RuntimeException
import java.util.*

@Service
class StockServiceImpl(private val productRepository: ProductRepository, private val stockRepository: StockRepository) :
    StockService {
    override fun createProduct(productStockDTO: ProductStockDTO): ProductDTO {
        val productDTO = ProductDTO(description = productStockDTO.description, name = productStockDTO.name)
        val productAux = productRepository.save(productDTO)
        val quantity = if(productStockDTO.quantity >= 0) productStockDTO.quantity else 0
        val stockDTO = StockDTO(productId = productAux.id!!, quantityProduct = quantity)
        stockRepository.save(stockDTO)
        return productAux
    }

    override fun getAllProduct(): List<ProductDTO> {
        return productRepository.findAll()
    }

    override fun getAllStock(): List<StockDTO> {
        return stockRepository.findAll()
    }

    override fun getProductById(id: Long): Optional<ProductDTO> {
        return productRepository.findById(id)
    }

    override fun getStockById(id: Long): Optional<StockDTO> {
        return stockRepository.findById(id)
    }

    override fun updateProduct(id: Long, productStockDTO: ProductStockDTO): Optional<ProductDTO> {
        if (productStockDTO.quantity >= 0) {
            addQuantityStock(id, productStockDTO.quantity)
        }

        val product = getProductById(id)
        if (product.isEmpty) Optional.empty<ProductDTO>()
        when {
            productStockDTO.name == "" && productStockDTO.description == "" -> return Optional.empty<ProductDTO>()

            productStockDTO.name == "" -> return product.map {
                val productToUpdate = it.copy(
                    description = productStockDTO.description
                )
                productRepository.save(productToUpdate)
            }

            productStockDTO.description == "" -> return product.map {
                val productToUpdate = it.copy(
                    name = productStockDTO.name
                )
                productRepository.save(productToUpdate)
            }

            else -> return product.map {
                val productToUpdate = it.copy(
                    name = productStockDTO.name,
                    description = productStockDTO.description
                )
                productRepository.save(productToUpdate)
            }
        }
        return Optional.empty()
    }

    override fun addQuantityStock(id: Long, quantity: Int): Optional<StockDTO> {
        val stockDTO: Optional<StockDTO> = getStockById(id.plus(1))
        if(stockDTO.get().quantityProduct + quantity >= 0){
            return stockDTO.map {
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
        if(optional.isEmpty) Optional.empty<StockDTO>()

        val stockDTO: Optional<StockDTO> = getStockById(id.plus(1))
        stockDTO.map {
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
            getProductById(id.minus(1)).isEmpty.not() -> deleteProduct(id.minus(1))
        }
        stockRepository.findById(id).map {
            stockRepository.delete(it)
        }.orElseThrow{ throw RuntimeException("Id") }
    }
}