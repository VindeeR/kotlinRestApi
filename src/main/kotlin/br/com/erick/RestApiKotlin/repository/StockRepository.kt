package br.com.erick.RestApiKotlin.repository

import br.com.erick.RestApiKotlin.model.Stock
import org.springframework.data.jpa.repository.JpaRepository

interface StockRepository : JpaRepository<Stock, Long> {

}