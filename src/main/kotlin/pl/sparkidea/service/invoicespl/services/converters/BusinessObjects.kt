package pl.sparkidea.service.invoicespl.services.converters

import org.springframework.stereotype.Component
import pl.sparkidea.service.invoicespl.domain.BankAccount
import pl.sparkidea.service.invoicespl.domain.BusinessObjectData
import pl.sparkidea.service.invoicespl.domain.BusinessRole
import pl.sparkidea.service.invoicespl.dto.BusinessObjectResponse
import pl.sparkidea.service.invoicespl.dto.NewBusinessObjectRequest
import pl.sparkidea.service.invoicespl.repositories.domain.mongo.DbBusinessObjectData
import pl.sparkidea.service.invoicespl.repositories.domain.mongo.DbInvoiceBusinessObjectData
import reactor.core.publisher.Mono
import java.security.Principal
import java.util.*

/**
 *
 * @author Maciej Lesniak
 */
@Component
class BusinessObjectDataToInfoConverter(
        val addressToAddressDtoConverter: AddressToAddressDtoConverter,
        val individualDtoConverter: IndividualToIndividualDtoConverter,
        val bankAccountsListToBankAccountsDtoListConverter: BankAccountsListToBankAccountsDtoListConverter
) : MonoConverter<BusinessObjectData, BusinessObjectResponse> {
    override fun convert(source: BusinessObjectData): Mono<BusinessObjectResponse> {
        return Mono.zip(
                addressToAddressDtoConverter.convert(source.objAddress),
                individualDtoConverter.convert(source.coordinatorInfoCopy),
                bankAccountsListToBankAccountsDtoListConverter.convert(source.bankAccounts))
                .map {
                    BusinessObjectResponse(
                            source.id!!,
                            source.objFullName,
                            source.objShortName,
                            source.objNip,
                            source.objRegon,
                            it.t1,
                            it.t2,
                            it.t3,
                            source.vatPayer,
                            source.taxOfficeId)
                }
    }
}

@Component
class BusinessObjectDataToDbBusinessObjectData(
        val addressToDbAddressConverter: AddressToDbAddressConverter,
        val individualToDbIndividualConverter: IndividualToDbIndividualConverter,
        val bakAccountsListToDbBankAccountsListConverter: BankAccountsListToDbBankAccountsListConverter
) : MonoConverter<BusinessObjectData, DbBusinessObjectData> {
    override fun convert(source: BusinessObjectData): Mono<DbBusinessObjectData> {
        return Mono.zip(
                addressToDbAddressConverter.convert(source.objAddress),
                individualToDbIndividualConverter.convert(source.coordinatorInfoCopy),
                bakAccountsListToDbBankAccountsListConverter.convert(source.bankAccounts))
                .map {
                    DbBusinessObjectData(
                            source.objFullName,
                            source.objShortName,
                            source.objNip,
                            source.objRegon,
                            it.t1,
                            it.t2,
                            it.t3,
                            source.vatPayer,
                            source.taxOfficeId,
                            source.ownershipId,
                            source.businessRole)
                }
    }
}

@Component
class BusinessObjectDataCopyToDbInvoiceBusinessObjectData(
        val addressToDbAddressConverter: AddressToDbAddressConverter,
        val individualToDbIndividualConverter: IndividualToDbIndividualConverter
) : MonoConverter<BusinessObjectData, DbInvoiceBusinessObjectData> {
    override fun convert(source: BusinessObjectData): Mono<DbInvoiceBusinessObjectData> {
        return Mono.zip(
                addressToDbAddressConverter.convert(source.objAddress),
                individualToDbIndividualConverter.convert(source.coordinatorInfoCopy))
                .map {
                    DbInvoiceBusinessObjectData(
                            source.objFullName,
                            source.objShortName,
                            source.objNip,
                            source.objRegon,
                            it.t1,
                            it.t2,
                            source.vatPayer,
                            source.taxOfficeId)
                }
    }
}

@Component
class DbBusinessObjectDataToBusinessObjectDataConverter(
        val dbAddressToAddressConverter: DbAddressToAddressConverter,
        val dbIndividualToIndividualConverter: DbIndividualToIndividualConverter,
        val dbBankAccountsListToBankAccountsListConverter: DbBankAccountsListToBankAccountsListConverter
) : MonoConverter<DbBusinessObjectData, BusinessObjectData> {

    override fun convert(source: DbBusinessObjectData): Mono<BusinessObjectData> {
        return Mono.zip(
                dbAddressToAddressConverter.convert(source.objAddress),
                dbIndividualToIndividualConverter.convert(source.coordinatorInfoCopy),
                dbBankAccountsListToBankAccountsListConverter.convert(source.objIban))
                .map {
                    BusinessObjectData(
                            source.getSafeInitializedIdValue(),
                            source.objFullName,
                            source.objShortName,
                            source.objNip,
                            source.objRegon,
                            it.t1,
                            it.t2,
                            it.t3,
                            source.vatPayer,
                            source.taxOfficeId,
                            source.businessRole,
                            source.ownershipId)
                }
    }

}

@Component
class NewBusinessObjectRequestToBusinessObjectConverter(
        private val addressDtoToAddressConverter: AddressDtoToAddressConverter,
        private val individualDtoToIndividualConverter: IndividualDtoToIndividualConverter,
        private val bankAccountDtoCollectionToBankAccountCollectionConverter: BankAccountDtoCollectionToBankAccountCollectionConverter
) : ContextualMonoConverter<NewBusinessObjectRequest, BusinessObjectData, Pair<Principal, BusinessRole>?> {
    override fun convert(source: NewBusinessObjectRequest, converterContext: Pair<Principal, BusinessRole>?): Mono<BusinessObjectData> {
        return Mono.zip(
                addressDtoToAddressConverter.convert(source.address),
                individualDtoToIndividualConverter.convert(source.coordinator),
                bankAccountDtoCollectionToBankAccountCollectionConverter.convert(source.bankAccounts)
        )
                .map {
                    BusinessObjectData(
                            null,
                            source.fullName,
                            source.shortName,
                            source.nip,
                            source.regon,
                            it.t1,
                            it.t2,
                            it.t3.toList(),
                            source.vatPayer,
                            source.taxOfficeId,
                            converterContext!!.second,
                            converterContext.first.name)
                }

    }
}

@Component
class DbInvoiceBusinessObjectDataToBusinessObjectDataConverter(
        val dbAddressToAddressConverter: DbAddressToAddressConverter,
        val dbIndividualToIndividualConverter: DbIndividualToIndividualConverter
) : MonoConverter<DbInvoiceBusinessObjectData, BusinessObjectData> {
    override fun convert(source: DbInvoiceBusinessObjectData): Mono<BusinessObjectData> {

        return Mono.zip(
                dbAddressToAddressConverter.convert(source.objAddress),
                dbIndividualToIndividualConverter.convert(source.coordinatorInfoCopy))
                .map {
                    BusinessObjectData(
                            source.getSafeInitializedIdValue(),
                            source.objFullName,
                            source.objShortName,
                            source.objNip,
                            source.objRegon,
                            it.t1,
                            it.t2,
                            Arrays.asList(BankAccount.empty()),
                            source.vatPayer,
                            source.taxOfficeId,
                            BusinessRole.UNKNOWN,
                            "")
                }

    }
}