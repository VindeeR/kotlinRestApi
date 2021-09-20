package br.com.erick.restApiKotlin.controller

import br.com.erick.restApiKotlin.model.Product
import br.com.erick.restApiKotlin.model.Stock
import br.com.erick.restApiKotlin.model.ProductStock
import br.com.erick.restApiKotlin.service.StockService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/stock")
class StockController (private val service: StockService){

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createProduct(@RequestBody productStock: ProductStock): Product = service.createProduct(productStock)

    @GetMapping
    fun getAllProduct(): List<Product> = service.getAllProduct()

    @GetMapping("/getStock")
    fun getAllStock(): List<Stock> = service.getAllStock()

    @GetMapping("/{id:[\\d]+}")
    fun getProductById(@PathVariable id: Long) : ResponseEntity<Product> =
        service.getProductById(id).map{
            ResponseEntity.ok(it)
        }.orElse(ResponseEntity.notFound().build())

    @GetMapping("/getStock/{id:[\\d]+}")
    fun getStockById(@PathVariable id: Long) : ResponseEntity<Stock> =
        service.getStockById(id).map{
            ResponseEntity.ok(it)
        }.orElse(ResponseEntity.notFound().build())

    @PutMapping("/{id:[\\d]+}")
    fun updateProduct(@PathVariable id: Long, @RequestBody productStock: ProductStock): ResponseEntity<Product>? {
        return service.updateProduct(id, productStock).map {
            ResponseEntity.ok(it)
        }.orElse(ResponseEntity.notFound().build())
    }

    @PutMapping("/addStock/{id:[\\d]+}")
    fun addQuantityStock(@PathVariable id: Long, @RequestBody quantity: Int): ResponseEntity<Stock>? {
        return service.addQuantityStock(id, quantity).map {
            ResponseEntity.ok(it)
        }.orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id:[\\d]+}")
    fun deleteProduct(@PathVariable id: Long) : ResponseEntity<Void> {
        service.deleteProduct(id)
        return ResponseEntity<Void>(HttpStatus.OK)
    }

    @DeleteMapping("/deleteStock/{id:[\\d]+}")
    fun deleteStock(@PathVariable id: Long) : ResponseEntity<Void> {
        service.deleteStock(id)
        return ResponseEntity<Void>(HttpStatus.OK)
    }
}