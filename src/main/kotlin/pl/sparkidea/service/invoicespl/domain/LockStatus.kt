package pl.sparkidea.service.invoicespl.domain

/**
 *
 * @author Maciej Lesniak
 */
enum class LockStatus {
    CHANGES_ALLOWED,
    CHANGES_NOT_ALLOWED,
    TEMP_LOCK
}