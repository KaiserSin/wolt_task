package project.wolt_test.model

data class OrderInfo (
    var total_price: Int = 0,
    var small_order_surcharge: Int = 0,
    var cart_value: Int = 0,
    var delivery: DeliveryInfo = DeliveryInfo(0,0)
)