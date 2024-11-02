package project.wolt_test.service

import org.slf4j.LoggerFactory
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestTemplate
import project.wolt_test.model.place.CafeInformation
import project.wolt_test.model.place.DistanceRange
import java.net.SocketTimeoutException

@Service
class ApiService(val restTemplate: RestTemplate) {

    private val logger = LoggerFactory.getLogger(ApiService::class.java)

    fun getData(url: String): CafeInformation {
        val staticIp = "https://consumer-api.development.dev.woltapi.com/home-assignment-api/v1/venues/$url/static"
        val dynamicIp = "https://consumer-api.development.dev.woltapi.com/home-assignment-api/v1/venues/$url/dynamic"

        var cafeInformation = CafeInformation()

        cafeInformation = getStaticData(staticIp, cafeInformation)

        cafeInformation = getDynamicData(dynamicIp, cafeInformation)

        return cafeInformation
    }

    @Retryable(
        value = [ResourceAccessException::class, SocketTimeoutException::class],
        maxAttempts = 3,
        backoff = Backoff(delay = 3000)
    )
    private fun getStaticData(staticIp: String, cafeInformation: CafeInformation): CafeInformation {
        try {
            val apiResponseStatic: ApiResponseStatic? = restTemplate.getForObject(staticIp, ApiResponseStatic::class.java)
            apiResponseStatic?.let {
                val coordinates = it.venue_raw.location.coordinates
                cafeInformation.longitude = coordinates[0]
                cafeInformation.latitude = coordinates[1]
            } ?: logger.warn("Static API response is null")
        } catch (e: ResourceAccessException) {
            logger.error("Network error while accessing static data. Retrying... Exception: $e")
            throw e
        } catch (e: Exception) {
            logger.error("Error in getting static data. Exception: $e")
            throw e
        }
        return cafeInformation
    }

    @Retryable(
        value = [ResourceAccessException::class, SocketTimeoutException::class],
        maxAttempts = 3,
        backoff = Backoff(delay = 3000)
    )
    private fun getDynamicData(dynamicIp: String, cafeInformation: CafeInformation): CafeInformation {
        try {
            val apiResponseDynamic: ApiResponseDynamic? = restTemplate.getForObject(dynamicIp, ApiResponseDynamic::class.java)
            apiResponseDynamic?.let {
                cafeInformation.minimum_surcharge = it.venue_raw.delivery_specs.order_minimum_no_surcharge
                cafeInformation.base_price = it.venue_raw.delivery_specs.delivery_pricing.base_price
                cafeInformation.distance_ranges = it.venue_raw.delivery_specs.delivery_pricing.distance_ranges
            } ?: logger.warn("Dynamic API response is null")
        } catch (e: ResourceAccessException) {
            logger.error("Network error while accessing dynamic data. Retrying... Exception: $e")
            throw e
        } catch (e: Exception) {
            logger.error("Error in getting dynamic data. Exception: $e")
            throw e
        }
        return cafeInformation
    }


    private data class Location(
        val coordinates: List<Double>
    )

    private data class VenueRawStatic(
        val location: Location
    )

    private data class ApiResponseStatic(
        val venue_raw: VenueRawStatic
    )

    private data class ApiResponseDynamic(
        val venue_raw: VenueRawDynamic
    )

    private data class VenueRawDynamic(
        val delivery_specs: DeliverySpecs,
    )

    private data class DeliverySpecs(
        val order_minimum_no_surcharge: Int,
        val delivery_pricing: DeliveryPricing
    )

    private data class DeliveryPricing(
        val base_price: Int,
        val distance_ranges: List<DistanceRange>
    )
}