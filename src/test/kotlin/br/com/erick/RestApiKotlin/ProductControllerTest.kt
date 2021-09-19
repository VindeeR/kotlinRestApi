package br.com.erick.RestApiKotlin

import br.com.erick.RestApiKotlin.model.Product
import br.com.erick.RestApiKotlin.model.ProductStock
import br.com.erick.RestApiKotlin.repository.ProductRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

	@Autowired lateinit var mockMvc: MockMvc

	@Autowired lateinit var productRepository: ProductRepository

	@Test
	fun `test find all`() {
		productRepository.save(Product(name = "Test", description = "descricao"))

		mockMvc.perform(MockMvcRequestBuilders.get("/stock"))
			.andExpect(MockMvcResultMatchers.status().isOk)
			.andExpect(MockMvcResultMatchers.jsonPath("\$").isArray)
			.andExpect(MockMvcResultMatchers.jsonPath("\$[0].id").isNumber)
			.andExpect(MockMvcResultMatchers.jsonPath("\$[0].name").isString)
			.andExpect(MockMvcResultMatchers.jsonPath("\$[0].description").isString)
			.andDo(MockMvcResultHandlers.print())
	}

	@Test
	fun `test find by id`() {
		val product = productRepository.save(Product(name = "Test", description = "descricao"))

		mockMvc.perform(MockMvcRequestBuilders.get("/stock/${product.id}"))
			.andExpect(MockMvcResultMatchers.status().isOk)
			.andExpect(MockMvcResultMatchers.jsonPath("\$.id").value(product.id))
			.andExpect(MockMvcResultMatchers.jsonPath("\$.name").value(product.name))
			.andExpect(MockMvcResultMatchers.jsonPath("\$.description").value(product.description))
			.andDo(MockMvcResultHandlers.print())
	}

	@Test
	fun `test create Product`() {
		val product = Product(name = "Test", description = "descricao")
		val json = ObjectMapper().writeValueAsString(product)
		productRepository.deleteAll()
		mockMvc.perform(MockMvcRequestBuilders.post("/stock/")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(json))
			.andExpect(MockMvcResultMatchers.status().isCreated)
			.andExpect(MockMvcResultMatchers.jsonPath("\$.name").value(product.name))
			.andExpect(MockMvcResultMatchers.jsonPath("\$.description").value(product.description))
			.andDo(MockMvcResultHandlers.print())

		Assertions.assertFalse(productRepository.findAll().isEmpty())
	}

	@Test
	fun `test update Product`() {
		val product = productRepository
			.save(Product(name = "Test", description = "descricao"))
			.copy(name = "Updated", description = "test")
		val productStock = ProductStock(name = product.name, description = product.description, quantity = 0) // Quantity para testar regra do negocio aonde não se pode
		// atualizar valor para qualquer coisa menor que 0
		val json = ObjectMapper().writeValueAsString(productStock)
		mockMvc.perform(MockMvcRequestBuilders.put("/stock/${product.id}")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(json))
			.andExpect(MockMvcResultMatchers.status().isOk)
			.andExpect(MockMvcResultMatchers.jsonPath("\$.name").value(productStock.name))
			.andExpect(MockMvcResultMatchers.jsonPath("\$.description").value(productStock.description))
			.andDo(MockMvcResultHandlers.print())

		val findById = productRepository.findById(product.id!!)
		Assertions.assertTrue(findById.isPresent)
		Assertions.assertEquals(product.name, findById.get().name)
	}

	@Test
	fun `test update Product quantity`() {
		val product = productRepository
			.save(Product(name = "Test", description = "descricao"))
			.copy(name = "Updated", description = "test")
		val productStock = ProductStock(name = product.name, description = product.description, quantity = -1) // Quantity para testar regra do negocio aonde não se pode
		// atualizar valor para qualquer coisa menor que 0
		val json = ObjectMapper().writeValueAsString(productStock)
		mockMvc.perform(MockMvcRequestBuilders.put("/stock/${product.id}")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(json))
			.andExpect(MockMvcResultMatchers.status().isNotFound)
			.andDo(MockMvcResultHandlers.print())

		val findById = productRepository.findById(product.id!!)
		Assertions.assertTrue(findById.isPresent)
	}

	@Test
	fun `test update quantity`() {
		val product = productRepository
			.save(Product(name = "Test", description = "descricao")).copy()
		val productStock = ProductStock(name = "", description = "", quantity = 10) // Quantity para testar regra do negocio aonde não se pode
		// atualizar valor para qualquer coisa menor que 0
		val json = ObjectMapper().writeValueAsString(productStock)
		mockMvc.perform(MockMvcRequestBuilders.put("/stock/${product.id}")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(json))
			.andExpect(MockMvcResultMatchers.status().isOk)
			.andDo(MockMvcResultHandlers.print())

		val findById = productRepository.findById(product.id!!)
		Assertions.assertTrue(findById.isPresent)
	}

	@Test
	fun `test delete Product`() {
		val product = productRepository
			.save(Product(name = "Test", description = "descricao"))
		mockMvc.perform(MockMvcRequestBuilders.delete("/stock/${product.id}"))
			.andExpect(MockMvcResultMatchers.status().isOk)
			.andDo(MockMvcResultHandlers.print())

		val findById = productRepository.findById(product.id!!)
		Assertions.assertFalse(findById.isPresent)
	}

}

