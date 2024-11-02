package project.wolt_test.model.order

import project.wolt_test.model.place.CafeInformation
import project.wolt_test.model.place.DistanceRange
import project.wolt_test.model.user.User
import kotlin.math.*

class Order (
    var total_price: Int = 0,
    var small_order_surcharge: Int = 0,
    var cart_value: Int = 0,
    var delivery: Delivery = Delivery(0,0)
){

    fun fillFields(cafe: CafeInformation, user: User){
        cart_value = user.cart_value
        small_order_surcharge = calcSmallOrderSurcharge(cafe.minimum_surcharge)
        delivery.distance = calcDistance(cafe.longitude, cafe.latitude, user.user_lon, user.user_lat)
        delivery.fee = calcDelivery(cafe.base_price,cafe.distance_ranges)
        total_price = small_order_surcharge + cart_value + delivery.fee
    }

    private fun calcSmallOrderSurcharge(minimum_surcharge: Int): Int {
        return if (minimum_surcharge > cart_value ) {
            minimum_surcharge - cart_value
        } else {
            0
        }
    }

    private fun calcDistance(cafeLo: Double, cafeLa: Double, userLo:Double, userLa: Double): Int{
        val R = 6371e3
        val lat1 = Math.toRadians(cafeLa)
        val lon1 = Math.toRadians(cafeLo)
        val lat2 = Math.toRadians(userLa)
        val lon2 = Math.toRadians(userLo)
        val deltaLat = lat2 - lat1
        val deltaLon = lon2 - lon1
        val a = sin(deltaLat / 2).pow(2) +
                cos(lat1) * cos(lat2) * sin(deltaLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        val distance = R * c
        return distance.roundToInt()
    }

    private fun calcDelivery(base_price: Int, ranges: List<DistanceRange>): Int{
        for (i in ranges){
            if(i.min<= delivery.distance && delivery.distance <=  i.max){
                return (base_price + i.a + (i.b*delivery.distance)/10).toInt()
            }
        }
        throw IllegalArgumentException("Delivery not enable to your adress")
    }
}