package pl.sparkidea.service.invoicespl.services.converters

import org.springframework.stereotype.Component
import pl.sparkidea.service.invoicespl.domain.BankAccount
import pl.sparkidea.service.invoicespl.domain.InvoicePaymentBankAccount
import pl.sparkidea.service.invoicespl.dto.BankAccountDto
import pl.sparkidea.service.invoicespl.repositories.domain.mongo.DbBankAccount
import pl.sparkidea.service.invoicespl.repositories.domain.mongo.DbInvoicePaymentBankAccount
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*
import java.util.stream.Collectors

/**
 *
 * @author Maciej Lesniak
 */

@Component
class DbBankAccountsToChosenBankAccountConverter(
        private val dbBankAccountToBankAccountConverter: DbBankAccountToBankAccountConverter
) : ContextualMonoConverter<List<DbBankAccount>, BankAccount, String?> {
    override fun convert(source: List<DbBankAccount>, converterContext: String?): Mono<BankAccount> {

        if (converterContext == null) {
            return Mono.empty()
        }

        return Flux.fromIterable(source)
                .filter { it.id === converterContext }
                .singleOrEmpty()
                .flatMap { dbBankAccountToBankAccountConverter.convert(it) }

    }
}

@Component
class DbBankAccountToBankAccountConverter : MonoConverter<DbBankAccount, BankAccount> {
    override fun convert(source: DbBankAccount): Mono<BankAccount> {
        return Mono.just(BankAccount(source.id, source.accountName, source.accountType, source.accountNumber))
    }
}

@Component
class DbBankAccountsListToBankAccountsListConverter(
        val dbBankAccountToBankAccountConverter: DbBankAccountToBankAccountConverter
) : MonoConverter<List<DbBankAccount>, List<BankAccount>> {
    override fun convert(source: List<DbBankAccount>): Mono<List<BankAccount>> {
        return Flux.fromIterable(source)
                .flatMap { dbBankAccountToBankAccountConverter.convert(it) }
                .collectList()
    }
}

@Component
class BankAccountToDbBankAccountConverter : MonoConverter<BankAccount, DbBankAccount> {
    override fun convert(source: BankAccount): Mono<DbBankAccount> {
        return Mono.just(DbBankAccount(source.id, source.accountName, source.accountType, source.accountNumber))
    }
}

@Component
class BankAccountsListToDbBankAccountsListConverter(
        private val bankAccountToDbBankAccountConverter: BankAccountToDbBankAccountConverter
) : MonoConverter<List<BankAccount>, List<DbBankAccount>> {
    override fun convert(source: List<BankAccount>): Mono<List<DbBankAccount>> {
        return Flux.fromIterable(source)
                .flatMap { bankAccountToDbBankAccountConverter.convert(it) }
                .collectList()
    }
}

@Component
class BankAccountsListToBankAccountsDtoListConverter(
        private val bankAccountToBankAccountDtoConverter: BankAccountToBankAccountDtoConverter
) : MonoConverter<List<BankAccount>, List<BankAccountDto>> {
    override fun convert(source: List<BankAccount>): Mono<List<BankAccountDto>> {
        return Flux.fromIterable(source)
                .flatMap { bankAccountToBankAccountDtoConverter.convert(it) }
                .collectList()
    }
}

@Component
class BankAccountToBankAccountDtoConverter : MonoConverter<BankAccount, BankAccountDto> {
    override fun convert(source: BankAccount): Mono<BankAccountDto> {
        val dto = BankAccountDto(source.accountName, source.accountType, source.accountNumber)
        dto.accountId = source.id
        return Mono.just(dto)
    }
}


@Component
class BankAccountDtoCollectionToBankAccountCollectionConverter : MonoConverter<Collection<BankAccountDto>, Collection<BankAccount>> {
    override fun convert(source: Collection<BankAccountDto>): Mono<Collection<BankAccount>> = Mono.just(source.stream()
            .map {
                BankAccount(
                        it.accountId ?: UUID.randomUUID().toString(),
                        it.accountName,
                        it.accountType,
                        it.accountNumber)
            }
            .collect(Collectors.toList()))
}

@Component
class BankAccountCollectionToDbBankAccountCollectionConverter : MonoConverter<Collection<BankAccount>, Collection<DbBankAccount>> {
    override fun convert(source: Collection<BankAccount>): Mono<Collection<DbBankAccount>> = Mono.just(source.stream()
            .map { account -> DbBankAccount(account.id, account.accountName, account.accountType, account.accountNumber) }.collect(Collectors.toList()))
}

@Component
class DbBankAccountCollectionToBankAccountDtoCollectionConverter : MonoConverter<Collection<DbBankAccount>, Collection<BankAccountDto>> {
    override fun convert(source: Collection<DbBankAccount>): Mono<Collection<BankAccountDto>> = Mono.just(source.stream()
            .map { account -> BankAccountDto(account.accountName, account.accountType, account.accountNumber) }.collect(Collectors.toList()))
}

@Component
class InvoicePaymentBankAccountToDbInvoicePaymentBankAccountConverter : MonoConverter<InvoicePaymentBankAccount, DbInvoicePaymentBankAccount> {
    override fun convert(source: InvoicePaymentBankAccount): Mono<DbInvoicePaymentBankAccount> {

        return Mono.just(
                DbInvoicePaymentBankAccount(
                        source.accountType,
                        source.accountNumber
                )
        )
    }
}

@Component
class DbInvoicePaymentBankAccountToInvoicePaymentBankAccountConverter : MonoConverter<DbInvoicePaymentBankAccount, InvoicePaymentBankAccount> {
    override fun convert(source: DbInvoicePaymentBankAccount): Mono<InvoicePaymentBankAccount> {

        return Mono.just(
                InvoicePaymentBankAccount(
                        source.accountType,
                        source.accountNumber
                )
        )

    }
}