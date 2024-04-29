
package acme.features.sponsor.sponsorDashboard;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.data.datatypes.Money;
import acme.client.repositories.AbstractRepository;
import acme.entities.configuration.SystemConfiguration;

@Repository
public interface SponsorSponsorDashboardRepository extends AbstractRepository {

	@Query("select count(i) from Invoice i where i.tax <= 21 and i.draftMode = false and i.sponsorship.sponsor.id = :sponsorId")
	Integer totalNumberOfInvoicesWithLessOrEqualThan21(int sponsorId);

	@Query("select count(s) from Sponsorship s where s.link is not null and s.draftMode = false and s.sponsor.id = :sponsorId")
	Integer totalNumberOfSponsorshipsWithLink(int sponsorId);

	@Query("select s.amount from Sponsorship s where s.draftMode = false and s.sponsor.id = :sponsorId")
	Collection<Money> findSponsorshipsAmountBySponsorId(int sponsorId);

	@Query("select i.quantity from Invoice i where i.draftMode = false and i.sponsorship.sponsor.id = :sponsorId")
	Collection<Money> findInvoicesQuantityBySponsorId(int sponsorId);

	@Query("select sc from SystemConfiguration sc")
	SystemConfiguration getSystemConfiguration();

}
