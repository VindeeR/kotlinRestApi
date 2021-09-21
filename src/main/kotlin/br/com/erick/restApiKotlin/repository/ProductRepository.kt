package br.com.erick.restApiKotlin.repository

import br.com.erick.restApiKotlin.model.ProductDTO
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<ProductDTO, Long> {

}