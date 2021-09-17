package br.com.erick.RestApiKotlin

import br.com.erick.RestApiKotlin.model.Product
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

		mockMvc.perform(MockMvcRequestBuilders.get("/Products"))
			.andExpect(MockMvcResultMatchers.status().isOk)
			.andExpect(MockMvcResultMatchers.jsonPath("\$").isArray)
			.andExpect(MockMvcResultMatchers.jsonPath("\$[0].id").isNumber)
			.andExpect(MockMvcResultMatchers.jsonPath("\$[0].name").isString)
			.andExpect(MockMvcResultMatchers.jsonPath("\$[0].document").isString)
			.andExpect(MockMvcResultMatchers.jsonPath("\$[0].phone").isString)
			.andDo(MockMvcResultHandlers.print())
	}

	@Test
	fun `test find by id`() {
		val product = productRepository.save(Product(name = "Test", description = "descricao"))

		mockMvc.perform(MockMvcRequestBuilders.get("/Products/${product.id}"))
			.andExpect(MockMvcResultMatchers.status().isOk)
			.andExpect(MockMvcResultMatchers.jsonPath("\$.id").value(product.id))
			.andExpect(MockMvcResultMatchers.jsonPath("\$.name").value(product.name))
			.andExpect(MockMvcResultMatchers.jsonPath("\$.document").value(product.description))
			.andDo(MockMvcResultHandlers.print())
	}

	@Test
	fun `test create Product`() {
		val product = Product(name = "Test", description = "descricao")
		val json = ObjectMapper().writeValueAsString(product)
		val quantity = 12
		productRepository.deleteAll()
		mockMvc.perform(MockMvcRequestBuilders.post("/Products/")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(json))
			.andExpect(MockMvcResultMatchers.status().isCreated)
			.andExpect(MockMvcResultMatchers.jsonPath("\$.name").value(product.name))
			.andExpect(MockMvcResultMatchers.jsonPath("\$.document").value(product.description))
			.andExpect(MockMvcResultMatchers.jsonPath("\$.quantity").value(quantity))
			.andDo(MockMvcResultHandlers.print())

		Assertions.assertFalse(productRepository.findAll().isEmpty())
	}

	@Test
	fun `test update Product`() {
		val product = productRepository
			.save(Product(name = "Test", description = "descricao"))
			.copy(name = "Updated")
		val json = ObjectMapper().writeValueAsString(product)
		mockMvc.perform(MockMvcRequestBuilders.put("/Products/${product.id}")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(json))
			.andExpect(MockMvcResultMatchers.status().isOk)
			.andExpect(MockMvcResultMatchers.jsonPath("\$.name").value(product.name))
			.andExpect(MockMvcResultMatchers.jsonPath("\$.document").value(product.description))
			.andDo(MockMvcResultHandlers.print())

		val findById = productRepository.findById(product.id!!)
		Assertions.assertTrue(findById.isPresent)
		Assertions.assertEquals(product.name, findById.get().name)
	}

	@Test
	fun `test delete Product`() {
		val product = productRepository
			.save(Product(name = "Test", description = "descricao"))
		mockMvc.perform(MockMvcRequestBuilders.delete("/Products/${product.id}"))
			.andExpect(MockMvcResultMatchers.status().isOk)
			.andDo(MockMvcResultHandlers.print())

		val findById = productRepository.findById(product.id!!)
		Assertions.assertFalse(findById.isPresent)
	}

}

