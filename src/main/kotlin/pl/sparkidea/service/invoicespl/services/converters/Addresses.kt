package pl.sparkidea.service.invoicespl.services.converters

import org.springframework.stereotype.Component
import pl.sparkidea.service.invoicespl.domain.Address
import pl.sparkidea.service.invoicespl.dto.AddressDto
import pl.sparkidea.service.invoicespl.repositories.domain.mongo.DbAddress
import reactor.core.publisher.Mono

/**
 *
 * @author Maciej Lesniak
 */

@Component
class AddressToDbAddressConverter : MonoConverter<Address, DbAddress> {
    override fun convert(source: Address): Mono<DbAddress> = Mono.just(
            DbAddress(
                    source.street,
                    source.house,
                    source.local,
                    source.city,
                    source.postalCode,
                    source.postOfficeCity,
                    source.countryIsoCode
            ))
}

@Component
class DbAddressToAddressConverter : MonoConverter<DbAddress, Address> {

    override fun convert(source: DbAddress): Mono<Address> = Mono.just(
            Address(
                    source.street,
                    source.house,
                    source.local,
                    source.city,
                    source.postalCode,
                    source.postalCodeCity,
                    source.countryIsoCode
            ))

}

@Component
class AddressDtoToAddressConverter : MonoConverter<AddressDto, Address> {
    override fun convert(source: AddressDto): Mono<Address> {
        return Mono.just(Address(
                source.street,
                source.house,
                source.local,
                source.city,
                source.postalCode,
                source.postalCodeCity,
                source.countryIsoCode
        ))
    }
}

@Component
class AddressToAddressDtoConverter: MonoConverter<Address, AddressDto> {
    override fun convert(source: Address): Mono<AddressDto> {
        return Mono.just(AddressDto(
                source.street,
                source.house,
                source.local,
                source.city,
                source.postalCode,
                source.postOfficeCity,
                source.countryIsoCode
        ))
    }
}