package project.wolt_test.model.place

class DistanceRange(val min: Int, val max: Int, val a: Int, val b: Double){

    override fun toString(): String {
        return "DistanceRange($min" +
                "$max" +
                "$a" +
                "$b)"
    }
}