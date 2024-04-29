
package acme.features.any.sponsorship;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.project.Project;
import acme.entities.sponsorship.Sponsorship;

@Repository
public interface AnySponsorshipRepository extends AbstractRepository {

	@Query("select s from Sponsorship s where s.draftMode = false")
	public Collection<Sponsorship> findAllSponsorships();

	@Query("select s from Sponsorship s where s.id = :id and s.draftMode = false ")
	public Sponsorship findOneSponsorshipById(int id);

	@Query("select p from Project p ")
	public Collection<Project> findAllProjects();

}
