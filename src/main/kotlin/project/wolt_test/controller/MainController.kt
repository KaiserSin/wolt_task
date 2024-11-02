package project.wolt_test.controller

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import project.wolt_test.model.order.Order
import project.wolt_test.service.ApiService
import project.wolt_test.model.user.User

@RestController
class MainController(private val apiService: ApiService) {
    @GetMapping("/delivery-order-price")
    fun get(@RequestParam("venue_slug") venue_slug: String,
            @RequestParam("cart_value") cart_value: Int,
            @RequestParam("user_lat") user_lat: Double,
            @RequestParam("user_lon") user_lon: Double): ResponseEntity<Any> {
        if(cart_value < 0){
            return ResponseEntity.badRequest().body("Cart value couldn't be negative")
        }
        if (user_lat !in -90.0..90.0 || user_lon !in -180.0..180.0) {
            return ResponseEntity.badRequest().body("Invalid coordinates for latitude/longitude")
        }
        return try {
            val cafeInformation = apiService.getData(venue_slug)
            val user = User(cart_value, user_lat, user_lon)
            val order = Order().apply { fillFields(cafeInformation, user) }
            ResponseEntity.ok(order)
        }catch (e: Exception){
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error: ${e.message}")
        }

    }
}