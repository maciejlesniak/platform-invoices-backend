package pl.sparkidea.service.invoicespl.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.password.DelegatingPasswordEncoder
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain

/**
 *
 * @author Maciej Lesniak
 */
@Configuration
@EnableWebFluxSecurity
class SecurityConfiguration {

    @Value("\${security.default-user.name}")
    lateinit var defaultUserName: String

    @Value("\${security.default-user.password}")
    lateinit var defaultUserPassword: String

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
                .csrf().disable()
                .authorizeExchange()
//                .pathMatchers("/**").permitAll()
                .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyExchange().authenticated()
                .and()
                .httpBasic()
                .and()
                .build()
    }

    @Bean
    fun userDetailsService(): MapReactiveUserDetailsService {
        val defaultUser = User.builder()
                .username(defaultUserName)
                .password(defaultUserPassword)
                .roles("USER")
                .build()

        return MapReactiveUserDetailsService(defaultUser)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        // testing only...
        return NoOpPasswordEncoder.getInstance()
    }

}