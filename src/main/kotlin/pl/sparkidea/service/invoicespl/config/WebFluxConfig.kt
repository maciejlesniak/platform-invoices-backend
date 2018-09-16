package pl.sparkidea.service.invoicespl.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer

/**
 *
 * @author Maciej Lesniak
 */
@Configuration
@EnableWebFlux
class WebFluxConfig : WebFluxConfigurer