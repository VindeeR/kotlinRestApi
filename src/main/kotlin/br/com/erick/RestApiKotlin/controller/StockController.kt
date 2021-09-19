package br.com.erick.RestApiKotlin.controller

import br.com.erick.RestApiKotlin.model.Product
import br.com.erick.RestApiKotlin.model.Stock
import br.com.erick.RestApiKotlin.model.ProductStock
import br.com.erick.RestApiKotlin.service.StockService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/stock")
class StockController (private val service: StockService){

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody productStock: ProductStock): Product = service.create(productStock)

    @GetMapping
    fun getAllProduct(): List<Product> = service.getAllProduct()

    @GetMapping("/getStock")
    fun getAllStock(): List<Stock> = service.getAllStock()

    @GetMapping("/{id:[\\d]+}")
    fun getById(@PathVariable id: Long) : ResponseEntity<Product> =
        service.getbyId(id).map{
            ResponseEntity.ok(it)
        }.orElse(ResponseEntity.notFound().build())

    @GetMapping("/getStock/{id:[\\d]+}")
    fun getStockById(@PathVariable id: Long) : ResponseEntity<Stock> =
        service.getStockbyId(id).map{
            ResponseEntity.ok(it)
        }.orElse(ResponseEntity.notFound().build())

    @PutMapping("/{id:[\\d]+}")
    fun update(@PathVariable id: Long, @RequestBody productStock: ProductStock): ResponseEntity<Product>? {
        return service.update(id, productStock).map {
            ResponseEntity.ok(it)
        }.orElse(ResponseEntity.notFound().build())
    }

    @PutMapping("/addStock/{id:[\\d]+}")
    fun addProduct(@PathVariable id: Long, @RequestBody quantity: Int): ResponseEntity<Stock>? {
        return service.addProduct(id, quantity).map {
            ResponseEntity.ok(it)
        }.orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id:[\\d]+}")
    fun delete(@PathVariable id: Long) : ResponseEntity<Void> {
        service.delete(id)
        return ResponseEntity<Void>(HttpStatus.OK)
    }

    @DeleteMapping("/deleteStock/{id:[\\d]+}")
    fun deleteStock(@PathVariable id: Long) : ResponseEntity<Void> {
        service.deleteStock(id)
        return ResponseEntity<Void>(HttpStatus.OK)
    }
}