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

package acme.features.administrator.administratorDashboard;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AdministratorAdministratorDashboardRepository extends AbstractRepository {

	@Query("select count(n) from Notice n")
	Integer numberOfNotices();

	@Query("select count(o) from Objective o")
	Integer numberOfObjectives();

	@Query("select count(r) from Risk r")
	Integer numberOfRisks();

	@Query("select count(c) from Claim c")
	Integer numberOfClaims();

	@Query("select count(a) from Administrator a")
	Long numberOfPrincipalsWithAdministratorRole();

	@Query("select count(a) from Auditor a")
	Long numberOfPrincipalsWithAuditorRole();

	@Query("select count(a) from Client a")
	Long numberOfPrincipalsWithClientRole();

	@Query("select count(a) from Consumer a")
	Long numberOfPrincipalsWithConsumerRole();

	@Query("select count(a) from Developer a")
	Long numberOfPrincipalsWithDeveloperRole();

	@Query("select count(a) from Manager a")
	Long numberOfPrincipalsWithManagerRole();

	@Query("select count(a) from Provider a")
	Long numberOfPrincipalsWithProviderRole();

	@Query("select count(a) from Sponsor a")
	Long numberOfPrincipalsWithSponsorRole();

	@Query("select 1.0 * count(a) / (select count(b) from Notice b) from Notice a where not a.email = null and not a.link = null")
	Double ratioOfNoticesWithEmailAdressAndLink();

	@Query("select 1.0 * count(a) / (select count(b) from Objective b) from Objective a where a.isCritical = true")
	Double ratioOfCriticalObjectives();

	@Query("select 1.0 * count(a) / (select count(b) from Objective b) from Objective a where a.isCritical = false")
	Double ratioOfNonCriticalObjectives();

	@Query("select avg(r.impact * r.probability / 100) from Risk r")
	Double avgRiskValue();

	@Query("select min(r.impact * r.probability / 100) from Risk r")
	Double minRiskValue();

	@Query("select max(r.impact * r.probability / 100) from Risk r")
	Double maxRiskValue();

	@Query("select stddev(r.impact * r.probability / 100) from Risk r")
	Double stdRiskValue();

	@Query("select day(c.instantiationMoment), count(c) from Claim c where c.instantiationMoment > :thresholdDate group by day(c.instantiationMoment)")
	List<Object[]> numberOfClaimsByDay(Date thresholdDate);

	@Query("select count(c) from Claim c where c.instantiationMoment >= :startDate and c.instantiationMoment < :endDate")
	Long numberOfClaimsBetweenDates(Date startDate, Date endDate);
}
