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

package acme.features.manager.managerDashboard;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.data.datatypes.Money;
import acme.client.repositories.AbstractRepository;
import acme.entities.configuration.SystemConfiguration;

@Repository
public interface ManagerManagerDashboardRepository extends AbstractRepository {

	@Query("select count(us) from UserStory us where us.manager.id = :id and us.draftMode = false and us.priority = acme.entities.project.Priority.MUST")
	Long totalNumberOfUserStoriesWithMustPriorityByManagerId(int id);

	@Query("select count(us) from UserStory us where us.manager.id = :id and us.draftMode = false and us.priority = acme.entities.project.Priority.SHOULD")
	Long totalNumberOfUserStoriesWithShouldPriorityByManagerId(int id);

	@Query("select count(us) from UserStory us where us.manager.id = :id and us.draftMode = false and us.priority = acme.entities.project.Priority.COULD")
	Long totalNumberOfUserStoriesWithCouldPriorityByManagerId(int id);

	@Query("select count(us) from UserStory us where us.manager.id = :id and us.draftMode = false and us.priority = acme.entities.project.Priority.WONT")
	Long totalNumberOfUserStoriesWithWontPriorityByManagerId(int id);

	@Query("select avg(us.estimatedCost) from UserStory us where us.manager.id = :id and us.draftMode = false")
	Double avgEstimatedCostOfUserStoriesByManagerId(int id);

	@Query("select min(us.estimatedCost) from UserStory us where us.manager.id = :id and us.draftMode = false")
	Integer minEstimatedCostOfUserStoriesByManagerId(int id);

	@Query("select max(us.estimatedCost) from UserStory us where us.manager.id = :id and us.draftMode = false")
	Integer maxEstimatedCostOfUserStoriesByManagerId(int id);

	@Query("select stddev(us.estimatedCost) from UserStory us where us.manager.id = :id and us.draftMode = false")
	Double stdEstimatedCostOfUserStoriesByManagerId(int id);

	@Query("select p.cost from Project p where p.manager.id = :id and p.draftMode = false")
	Collection<Money> getCostOfAllProjectsByManagerId(int id);

	@Query("select sc from SystemConfiguration sc")
	SystemConfiguration getSystemConfiguration();

}
