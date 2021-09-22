package br.com.erick.restApiKotlin.controller

import br.com.erick.restApiKotlin.model.ProductDTO
import br.com.erick.restApiKotlin.model.StockDTO
import br.com.erick.restApiKotlin.model.ProductStockDTO
import br.com.erick.restApiKotlin.service.StockService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/service")
class StockController (private val service: StockService){

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createProduct(@RequestBody productStockDTO: ProductStockDTO): ProductDTO = service.createProduct(productStockDTO)

    @GetMapping
    fun getAllProduct(): List<ProductDTO> = service.getAllProduct()

    @GetMapping("/stock")
    fun getAllStock(): List<StockDTO> = service.getAllStock()

    @GetMapping("/{id:[\\d]+}")
    fun getProductById(@PathVariable id: Long) : ResponseEntity<ProductDTO> =
        service.getProductById(id).map{
            ResponseEntity.ok(it)
        }.orElse(ResponseEntity.notFound().build())

    @GetMapping("/stock/{id:[\\d]+}")
    fun getStockById(@PathVariable id: Long) : ResponseEntity<StockDTO> =
        service.getStockById(id).map{
            ResponseEntity.ok(it)
        }.orElse(ResponseEntity.notFound().build())

    @PutMapping("/{id:[\\d]+}")
    fun updateProduct(@PathVariable id: Long, @RequestBody productStockDTO: ProductStockDTO): ResponseEntity<ProductDTO>? {
        return service.updateProduct(id, productStockDTO).map {
            ResponseEntity.ok(it)
        }.orElse(ResponseEntity.notFound().build())
    }

    @PutMapping("/stock/{id:[\\d]+}")
    fun addQuantityStock(@PathVariable id: Long, @RequestBody quantity: Int): ResponseEntity<StockDTO>? {
        return service.updateQuantityStock(id, quantity).map {
            ResponseEntity.ok(it)
        }.orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id:[\\d]+}")
    fun deleteProduct(@PathVariable id: Long) : ResponseEntity<Void> {
        service.deleteProduct(id)
        return ResponseEntity<Void>(HttpStatus.OK)
    }

    @DeleteMapping("/stock/{id:[\\d]+}")
    fun deleteStock(@PathVariable id: Long) : ResponseEntity<Void> {
        service.deleteStock(id)
        return ResponseEntity<Void>(HttpStatus.OK)
    }
}