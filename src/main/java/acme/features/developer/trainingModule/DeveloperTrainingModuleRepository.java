/*
 * DeveloperTrainingModuleRepository.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.developer.trainingModule;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.project.Project;
import acme.entities.training.TrainingModule;
import acme.entities.training.TrainingSession;
import acme.roles.Developer;

@Repository
public interface DeveloperTrainingModuleRepository extends AbstractRepository {

	@Query("select t from TrainingModule t where t.developer.id = :id")
	Collection<TrainingModule> findTrainingModules(int id);

	@Query("select p from Project p")
	Collection<Project> findProjects();

	@Query("select d from Developer d where d.id = :id")
	Developer findDeveloper(int id);

	@Query("select t from TrainingModule t where t.id = :id")
	TrainingModule findTrainingModuleById(int id);

	@Query("select t from TrainingSession t where t.trainingModule.id = :id")
	Collection<TrainingSession> findTrainingSessionsOfTrainingModule(int id);
}
