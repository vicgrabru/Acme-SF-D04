/*
 * EmployerApplicationRepository.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.manager.project;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.project.Project;
import acme.entities.project.UserStory;
import acme.entities.project.UserStoryAssign;
import acme.roles.Manager;

@Repository
public interface ManagerProjectRepository extends AbstractRepository {

	@Query("select usa from UserStoryAssign usa where usa.project.id = :id")
	Collection<UserStoryAssign> findManyUserStoryAssignsByProjectId(int id);

	@Query("select p from Project p where p.manager.id = :id")
	Collection<Project> findManyProjectsByManagerId(int id);

	@Query("select p from Project p where p.id = :id")
	Project findOneProjectById(int id);

	@Query("select p from Project p where p.code = :code")
	Project findOneProjectByCode(String code);

	@Query("select m from Manager m where m.id = :id")
	Manager findOneManagerById(int id);

	@Query("select sc.acceptedCurrencies from SystemConfiguration sc")
	String findAcceptedCurrenciesInSystem();

	@Query("select usa.userStory from UserStoryAssign usa where usa.project.id = :id")
	Collection<UserStory> findManyUserStoriesByProjectId(int id);

}
