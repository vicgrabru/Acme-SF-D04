
package acme.features.sponsor.invoice;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.sponsorship.Invoice;
import acme.entities.sponsorship.Sponsorship;

@Repository
public interface SponsorInvoiceRepository extends AbstractRepository {

	@Query("select s from Sponsorship s where s.id = :masterId")
	Sponsorship findOneSponsorshipById(int masterId);

	@Query("select s from Sponsorship s where s.code = :code")
	Sponsorship findOneSponsorshipByCode(String code);

	@Query("select i from Invoice i where i.id = :masterId")
	Invoice findOneInvoiceById(int masterId);

	@Query("select i from Invoice i where i.sponsorship.id = :masterId")
	Collection<Invoice> findManyInvoicesByMasterId(int masterId);

	@Query("select sc.acceptedCurrencies from SystemConfiguration sc")
	String findAcceptedCurrencies();

}
