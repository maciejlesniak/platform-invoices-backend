package pl.sparkidea.service.invoicespl.services.converters

import org.springframework.stereotype.Component
import pl.sparkidea.service.invoicespl.domain.Address
import pl.sparkidea.service.invoicespl.domain.BankAccount
import pl.sparkidea.service.invoicespl.domain.TaxOffice
import pl.sparkidea.service.invoicespl.dto.AddressDto
import pl.sparkidea.service.invoicespl.dto.BankAccountDto
import pl.sparkidea.service.invoicespl.dto.TaxOfficeRequest
import pl.sparkidea.service.invoicespl.dto.TaxOfficeResponse
import pl.sparkidea.service.invoicespl.repositories.domain.mongo.DbAddress
import pl.sparkidea.service.invoicespl.repositories.domain.mongo.DbBankAccount
import pl.sparkidea.service.invoicespl.repositories.domain.mongo.DbTaxOffice
import reactor.core.publisher.Mono
import reactor.util.function.Tuple2

/**
 *
 * @author Maciej Lesniak
 */
@Component
class TaxOfficeRequestToTaxOfficeConverter(
        val bankAccountDtoCollectionToBankAccountCollectionConverter: BankAccountDtoCollectionToBankAccountCollectionConverter,
        val addressDtoToAddressConverter: AddressDtoToAddressConverter
) : MonoConverter<TaxOfficeRequest, TaxOffice> {

    override fun convert(source: TaxOfficeRequest): Mono<TaxOffice> = Mono.zip(
            addressDtoToAddressConverter.convert(source.address),
            bankAccountDtoCollectionToBankAccountCollectionConverter.convert(source.accounts)
    ).map { t: Tuple2<Address, Collection<BankAccount>> -> TaxOffice(source.name, t.t1, t.t2.toHashSet()) }
}

@Component
class TaxOfficeToDbTaxOfficeConverter(
        val addressToDbAddressConverter: AddressToDbAddressConverter,
        val bankAccountCollectionToDbBankAccountCollectionConverter: BankAccountCollectionToDbBankAccountCollectionConverter
) : MonoConverter<TaxOffice, DbTaxOffice> {
    override fun convert(source: TaxOffice): Mono<DbTaxOffice> =
            Mono.zip(
                    addressToDbAddressConverter.convert(source.address),
                    bankAccountCollectionToDbBankAccountCollectionConverter.convert(source.accounts)
            ).map { t: Tuple2<DbAddress, Collection<DbBankAccount>> ->
                DbTaxOffice(
                        source.name,
                        t.t1,
                        t.t2.toHashSet()
                )
            }
}

@Component
class DbTaxOfficeToTaxOfficeResponseConverter(
        val dbAddressToAddressConverter: DbAddressToAddressConverter,
        val dbBankAccountCollectionToBankAccountDtoCollectionConverter: DbBankAccountCollectionToBankAccountDtoCollectionConverter,
        val addressToAddressDtoConverter: AddressToAddressDtoConverter
) : MonoConverter<DbTaxOffice, TaxOfficeResponse> {
    override fun convert(source: DbTaxOffice): Mono<TaxOfficeResponse> = dbAddressToAddressConverter.convert(source.address)
            .flatMap { addressToAddressDtoConverter.convert(it) }
            .zipWith(dbBankAccountCollectionToBankAccountDtoCollectionConverter.convert(source.accounts))
            .map { t: Tuple2<AddressDto, Collection<BankAccountDto>> ->
                TaxOfficeResponse(
                        source.id,
                        source.name,
                        t.t1,
                        t.t2.toHashSet()
                )
            }
}