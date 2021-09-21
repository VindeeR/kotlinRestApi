package br.com.erick.restApiKotlin

import br.com.erick.restApiKotlin.model.ProductDTO
import br.com.erick.restApiKotlin.model.ProductStockDTO
import br.com.erick.restApiKotlin.repository.ProductRepository
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
class ProductDTOControllerTest {

	@Autowired lateinit var mockMvc: MockMvc

	@Autowired lateinit var productRepository: ProductRepository

	@Test
	fun `test find all`() {
		productRepository.save(ProductDTO(name = "Test", description = "descricao"))

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
		val productDTO = productRepository.save(ProductDTO(name = "Test", description = "descricao"))

		mockMvc.perform(MockMvcRequestBuilders.get("/stock/${productDTO.id}"))
			.andExpect(MockMvcResultMatchers.status().isOk)
			.andExpect(MockMvcResultMatchers.jsonPath("\$.id").value(productDTO.id))
			.andExpect(MockMvcResultMatchers.jsonPath("\$.name").value(productDTO.name))
			.andExpect(MockMvcResultMatchers.jsonPath("\$.description").value(productDTO.description))
			.andDo(MockMvcResultHandlers.print())
	}

	@Test
	fun `test create Product`() {
		val productDTO = ProductDTO(name = "Test", description = "descricao")
		val json = ObjectMapper().writeValueAsString(productDTO)
		productRepository.deleteAll()
		mockMvc.perform(MockMvcRequestBuilders.post("/stock/")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(json))
			.andExpect(MockMvcResultMatchers.status().isCreated)
			.andExpect(MockMvcResultMatchers.jsonPath("\$.name").value(productDTO.name))
			.andExpect(MockMvcResultMatchers.jsonPath("\$.description").value(productDTO.description))
			.andDo(MockMvcResultHandlers.print())

		Assertions.assertFalse(productRepository.findAll().isEmpty())
	}

	@Test
	fun `test update Product`() {
		val productDTO = productRepository
			.save(ProductDTO(name = "Test", description = "descricao"))
			.copy(name = "Updated", description = "test")
		val productStockDTO = ProductStockDTO(name = productDTO.name, description = productDTO.description, quantity = 0) // Quantity para testar regra do negocio aonde não se pode
		// atualizar valor para qualquer coisa menor que 0
		val json = ObjectMapper().writeValueAsString(productStockDTO)
		mockMvc.perform(MockMvcRequestBuilders.put("/stock/${productDTO.id}")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(json))
			.andExpect(MockMvcResultMatchers.status().isOk)
			.andExpect(MockMvcResultMatchers.jsonPath("\$.name").value(productStockDTO.name))
			.andExpect(MockMvcResultMatchers.jsonPath("\$.description").value(productStockDTO.description))
			.andDo(MockMvcResultHandlers.print())

		val findById = productRepository.findById(productDTO.id!!)
		Assertions.assertTrue(findById.isPresent)
		Assertions.assertEquals(productDTO.name, findById.get().name)
	}

	@Test
	fun `test update Product quantity`() {
		val productDTO = productRepository
			.save(ProductDTO(name = "Test", description = "descricao"))
			.copy(name = "Updated", description = "test")
		val productStockDTO = ProductStockDTO(name = productDTO.name, description = productDTO.description, quantity = -1) // Quantity para testar regra do negocio aonde não se pode
		// atualizar valor para qualquer coisa menor que 0
		val json = ObjectMapper().writeValueAsString(productStockDTO)
		mockMvc.perform(MockMvcRequestBuilders.put("/stock/${productDTO.id}")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(json))
			.andExpect(MockMvcResultMatchers.status().isNotFound)
			.andDo(MockMvcResultHandlers.print())

		val findById = productRepository.findById(productDTO.id!!)
		Assertions.assertTrue(findById.isPresent)
	}

	@Test
	fun `test update quantity`() {
		val productDTO = productRepository
			.save(ProductDTO(name = "Test", description = "descricao")).copy()
		val productStockDTO = ProductStockDTO(name = "", description = "", quantity = 10) // Quantity para testar regra do negocio aonde não se pode
		// atualizar valor para qualquer coisa menor que 0
		val json = ObjectMapper().writeValueAsString(productStockDTO)
		mockMvc.perform(MockMvcRequestBuilders.put("/stock/${productDTO.id}")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(json))
			.andExpect(MockMvcResultMatchers.status().isOk)
			.andDo(MockMvcResultHandlers.print())

		val findById = productRepository.findById(productDTO.id!!)
		Assertions.assertTrue(findById.isPresent)
	}

	@Test
	fun `test delete Product`() {
		val productDTO = productRepository
			.save(ProductDTO(name = "Test", description = "descricao"))
		mockMvc.perform(MockMvcRequestBuilders.delete("/stock/${productDTO.id}"))
			.andExpect(MockMvcResultMatchers.status().isOk)
			.andDo(MockMvcResultHandlers.print())

		val findById = productRepository.findById(productDTO.id!!)
		Assertions.assertFalse(findById.isPresent)
	}

}

