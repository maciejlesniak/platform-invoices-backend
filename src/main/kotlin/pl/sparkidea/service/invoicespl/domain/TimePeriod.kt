package pl.sparkidea.service.invoicespl.domain

import java.time.Instant

/**
 *
 * @author Maciej Lesniak
 */
class TimePeriod (
        val start : Instant,
        val end: Instant
)