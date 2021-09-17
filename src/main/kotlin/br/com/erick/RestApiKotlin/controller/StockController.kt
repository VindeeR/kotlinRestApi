package br.com.erick.RestApiKotlin.controller

import br.com.erick.RestApiKotlin.model.Product
import br.com.erick.RestApiKotlin.model.Stock
import br.com.erick.RestApiKotlin.model.UpdateProduct
import br.com.erick.RestApiKotlin.service.StockService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/stock")
class StockController (private val service: StockService){

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody product: Product): Product = service.create(product)

    @GetMapping
    fun getAllProduct(): List<Product> = service.getAllProduct()

    @GetMapping("/getStock")
    fun getAllStock(): List<Stock> = service.getAllStock()

    @GetMapping("/{id}")
    fun getbyId(@PathVariable id: Long) : ResponseEntity<Product> =
        service.getbyId(id).map{
            ResponseEntity.ok(it)
        }.orElse(ResponseEntity.notFound().build())

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody updateProduct: UpdateProduct) {
        service.update(id, updateProduct).map {
            ResponseEntity.ok(it)
        }.orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) : ResponseEntity<Void> {
        service.delete(id)
        return ResponseEntity<Void>(HttpStatus.OK)
    }
}