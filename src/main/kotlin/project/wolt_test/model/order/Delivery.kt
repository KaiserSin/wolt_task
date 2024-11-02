package project.wolt_test.model.order

class Delivery(var fee: Int, var distance: Int){
    init {
        require(fee >= 0) { "Delivery fee cannot be negative" }
        require(distance >= 0) { "Distance cannot be negative" }
    }
}