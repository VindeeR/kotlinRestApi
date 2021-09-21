package br.com.erick.restApiKotlin.repository

import br.com.erick.restApiKotlin.model.StockDTO
import org.springframework.data.jpa.repository.JpaRepository

interface StockRepository : JpaRepository<StockDTO, Long> {

}