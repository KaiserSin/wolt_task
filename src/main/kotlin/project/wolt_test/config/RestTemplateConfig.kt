package project.wolt_test.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.DefaultResponseErrorHandler
import org.springframework.web.client.RestTemplate

@Configuration
class RestTemplateConfig {

    @Bean
    fun restTemplate(): RestTemplate {
        val factory = SimpleClientHttpRequestFactory()
        factory.setConnectTimeout(5000)
        factory.setReadTimeout(5000)
        val restTemplate = RestTemplate(factory)
        restTemplate.errorHandler = DefaultResponseErrorHandler()
        return restTemplate
    }
}