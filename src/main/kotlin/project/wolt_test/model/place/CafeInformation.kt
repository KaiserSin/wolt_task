package project.wolt_test.model.place

class CafeInformation(var longitude: Double = 0.0,
                      var latitude: Double = 0.0,
                      var minimum_surcharge: Int = 0,
                      var base_price: Int = 0,
                      var distance_ranges: List<DistanceRange> = emptyList()) {

    init {
        require(longitude in -180.0..180.0) { "Longitude must be between -180 and 180" }
        require(latitude in -90.0..90.0) { "Latitude must be between -90 and 90" }
        require(minimum_surcharge >= 0) { "Minimum surcharge cannot be negative" }
        require(base_price >= 0) { "Base price cannot be negative" }
    }

    override fun toString(): String {
        return "CafeInformation(" +
                "longitude=$longitude, " +
                "latitude=$latitude, " +
                "minimum_surcharge=$minimum_surcharge, " +
                "base_price=$base_price, " +
                "distance_ranges=$distance_ranges)"
    }

}