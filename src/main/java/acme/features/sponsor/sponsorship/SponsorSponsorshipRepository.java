
package acme.features.sponsor.sponsorship;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.project.Project;
import acme.entities.sponsorship.Invoice;
import acme.entities.sponsorship.Sponsorship;
import acme.roles.Sponsor;

@Repository
public interface SponsorSponsorshipRepository extends AbstractRepository {

	@Query("select s from Sponsorship s")
	public Collection<Sponsorship> findAllSponsorships();

	@Query("select s from Sponsorship s where s.sponsor.id = :sponsorid")
	public Collection<Sponsorship> findManySponsorshipsBySponsorId(int sponsorid);

	@Query("select s from Sponsorship s where s.id = :id")
	public Sponsorship findOneSponsorshipById(int id);

	@Query("select s from Sponsorship s where s.code = :code")
	public Sponsorship findOneSponsorshipByCode(String code);

	@Query("select s from Sponsor s where s.id = :id")
	public Sponsor findOneSponsorById(int id);

	@Query("select p from Project p where p.id = :projectId")
	public Project findOneProjectById(int projectId);

	@Query("select p from Project p ")
	public Collection<Project> findAllProjects();

	@Query("select i from Invoice i where i.sponsorship.id = :sponsorshipId")
	public Collection<Invoice> findManyInvoicesBySponsorshipId(int sponsorshipId);

	@Query("select sc.acceptedCurrencies from SystemConfiguration sc")
	String findAcceptedCurrencies();

}
