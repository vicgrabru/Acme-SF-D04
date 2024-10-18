/*
 * DeveloperDashboardRepository.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.developer.developerDashboard;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.training.TrainingModule;
import acme.entities.training.TrainingSession;

@Repository
public interface DeveloperDashboardRepository extends AbstractRepository {

	@Query("select avg(t.totalTime) from TrainingModule t where t.developer.id= :id and t.draftMode= false")
	public Double avgTrainingModuleTime(int id);

	@Query("select stddev(t.totalTime) from TrainingModule t where t.developer.id= :id and t.draftMode= false")
	public Double devTrainingModuleTime(int id);

	@Query("select min(t.totalTime) from TrainingModule t where t.developer.id= :id and t.draftMode= false")
	public Double minTrainingModuleTime(int id);

	@Query("select max(t.totalTime) from TrainingModule t where t.developer.id= :id and t.draftMode= false")
	public Double maxTrainingModuleTime(int id);

	@Query("select count(t) from TrainingModule t where not t.updateMoment=null and t.developer.id= :id and t.draftMode= false")
	public Integer numberOfTrainingModulesWithUpdateMoment(int id);

	@Query("select count(t) from TrainingSession t where not t.link=null and length(t.link)>0 and t.trainingModule.developer.id = :id and t.draftMode= false")
	public Integer numberOfTrainingSessionWithLink(int id);

	@Query("select t from TrainingModule t where t.developer.id= :id")
	public Collection<TrainingModule> findTrainingModules(int id);

	@Query("select t from TrainingSession t where t.trainingModule.developer.id = :id")
	public Collection<TrainingSession> findTrainingSessions(int id);
}
