package br.com.erick.restApiKotlin.repository

import br.com.erick.restApiKotlin.model.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long> {

}