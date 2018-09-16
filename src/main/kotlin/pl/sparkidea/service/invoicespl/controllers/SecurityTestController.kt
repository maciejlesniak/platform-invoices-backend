package pl.sparkidea.service.invoicespl.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.security.Principal

/**
 *
 * @author Maciej Lesniak
 */
@RestController
class SecurityTestController {

    @GetMapping("/test")
    fun getTestInfo(principal: Mono<Principal>): Mono<String> =
            principal.map { "test data for principal name: ${it.name}" }


}