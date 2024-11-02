package project.wolt_test.model.user

class User (val cart_value: Int,
            val user_lat: Double,
            val user_lon: Double){
    init {
        require(cart_value >= 0) { "Cart value cannot be negative" }
        require(user_lat in -90.0..90.0) { "Latitude must be between -90 and 90" }
        require(user_lon in -180.0..180.0) { "Longitude must be between -180 and 180" }
    }
}
