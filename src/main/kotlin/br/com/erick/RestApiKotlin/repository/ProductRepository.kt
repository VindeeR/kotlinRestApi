package br.com.erick.RestApiKotlin.repository

import br.com.erick.RestApiKotlin.model.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long> {

}