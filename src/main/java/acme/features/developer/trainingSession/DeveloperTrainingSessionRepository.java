/*
 * DeveloperTrainingSessionRepository.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.developer.trainingSession;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.training.TrainingModule;
import acme.entities.training.TrainingSession;

@Repository
public interface DeveloperTrainingSessionRepository extends AbstractRepository {

	@Query("select t from TrainingModule t where t.id = :id")
	TrainingModule findTrainingModuleById(int id);

	@Query("select t from TrainingSession t where t.id = :id")
	TrainingSession findTrainingSessionById(int id);

	@Query("select t from TrainingSession t where t.trainingModule.id = :id")
	Collection<TrainingSession> findTrainingSessionsOfTrainingModule(int id);

	@Query("select t from TrainingSession t")
	Collection<TrainingSession> findTrainingSessions();

	@Query("select t.trainingModule from TrainingSession t where t.id = :sessionId")
	TrainingModule findTrainingModuleByTrainingSessionId(int sessionId);
}
