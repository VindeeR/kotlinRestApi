package br.com.erick.restApiKotlin.repository

import br.com.erick.restApiKotlin.model.Stock
import org.springframework.data.jpa.repository.JpaRepository

interface StockRepository : JpaRepository<Stock, Long> {

}