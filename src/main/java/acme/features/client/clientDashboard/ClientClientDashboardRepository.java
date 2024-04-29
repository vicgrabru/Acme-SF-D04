/*
 * AdministratorDashboardRepository.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.client.clientDashboard;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.data.datatypes.Money;
import acme.client.repositories.AbstractRepository;
import acme.entities.configuration.SystemConfiguration;

@Repository
public interface ClientClientDashboardRepository extends AbstractRepository {

	@Query("select count(pl) from ProgressLog pl where pl.contract.client.id = :clientId and pl.draftMode = false and pl.completeness < 25.0")
	Integer below25CompletenessProgressLogsByClientId(int clientId);

	@Query("select count(pl) from ProgressLog pl where pl.contract.client.id = :clientId and pl.draftMode = false and pl.completeness >= 25.0 and pl.completeness < 50.0")
	Integer between25and50CompletenessProgressLogsByClientId(int clientId);

	@Query("select count(pl) from ProgressLog pl where pl.contract.client.id = :clientId and pl.draftMode = false and pl.completeness >= 50.0 and pl.completeness <= 75.0")
	Integer between50and75CompletenessProgressLogsByClientId(int clientId);

	@Query("select count(pl) from ProgressLog pl where pl.contract.client.id = :clientId and pl.draftMode = false and pl.completeness > 75.0")
	Integer above75CompletenessProgressLogsByClientId(int clientId);

	@Query("select c.budget from Contract c where c.client.id = :clientId and c.draftMode = false")
	Collection<Money> getBudgetOfContractsByClientId(int clientId);

	@Query("select sc from SystemConfiguration sc")
	SystemConfiguration getSystemConfiguration();

}
