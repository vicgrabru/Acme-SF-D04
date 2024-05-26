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

package acme.features.developer.developerDashboard;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.training.TrainingModule;
import acme.entities.training.TrainingSession;

@Repository
public interface DeveloperDashBoardRepository extends AbstractRepository {

	@Query(value = "select avg(timestampdiff(second,t.startTotalTime,t.endTotalTime)) from TrainingModule t", nativeQuery = true)
	public Double averageTrainingModuleTime();
	@Query(value = "select stddev(timestampdiff(second,t.startTotalTime,t.endTotalTime)) from TrainingModule t", nativeQuery = true)
	public Double deviationTrainingModuleTime();
	@Query(value = "select max(timestampdiff(second,t.startTotalTime,t.endTotalTime)) from TrainingModule t", nativeQuery = true)
	public Double maxTrainingModuleTime();
	@Query(value = "select min(timestampdiff(second,t.startTotalTime,t.endTotalTime)) from TrainingModule t", nativeQuery = true)
	public Double minTrainingModuleTime();
	@Query("select count(t) from TrainingModule t where t.updateMoment!=null")
	public Integer numberOfTrainingModulesWithUpdateMoment();
	@Query("select count(t) from TrainingModule t where t.link!=null")
	public Integer numberOfTrainingSessionWithLink();
	@Query("select t from TrainingModule t")
	public Collection<TrainingModule> findTrainingModules();

	@Query("select t from TrainingSession t")
	public Collection<TrainingSession> findTrainingSessions();
}
