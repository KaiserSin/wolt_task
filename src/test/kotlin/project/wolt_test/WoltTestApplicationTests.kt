package project.wolt_test

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.web.util.UriComponentsBuilder
import project.wolt_test.model.DeliveryInfo
import project.wolt_test.model.OrderInfo
import java.net.URI
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WoltTestApplicationTests {

	@Autowired
	lateinit var restTemplate: TestRestTemplate

	val url = "http://localhost:8080/delivery-order-price"

	@Test
	fun `test simple request`() {
		val needed = OrderInfo(1190, 0, 1000, DeliveryInfo(190, 177))
		val uri: URI = UriComponentsBuilder.fromHttpUrl(url)
			.queryParam("venue_slug", "home-assignment-venue-helsinki")
			.queryParam("cart_value", 1000)
			.queryParam("user_lat", 60.17094)
			.queryParam("user_lon", 24.93087)
			.build()
			.toUri()
		val response = restTemplate.getForEntity(uri, OrderInfo::class.java)
		assertEquals(HttpStatus.OK, response.statusCode, "received a response")
		assertEquals( needed, response.body,"result is equal")
	}
}
