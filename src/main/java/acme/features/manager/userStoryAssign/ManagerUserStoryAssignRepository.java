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

package acme.features.manager.userStoryAssign;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.project.Project;
import acme.entities.project.UserStory;
import acme.entities.project.UserStoryAssign;

@Repository
public interface ManagerUserStoryAssignRepository extends AbstractRepository {

	@Query("select p from Project p where p.manager.id = :managerId and p.draftMode = true and not exists(select usa from UserStoryAssign usa where usa.userStory.id = :userStoryId and usa.project.id = p.id)")
	Collection<Project> findManyDraftModeProjectsWithoutUserStoryByManagerIdAndUserStoryId(int managerId, int userStoryId);

	@Query("select usa.project from UserStoryAssign usa where usa.userStory.id = :id")
	Collection<Project> findManyProjectsWithUserStoryAssignedByUserStoryId(int id);

	@Query("select usa from UserStoryAssign usa where usa.userStory.id = :id and usa.project.draftMode = true")
	Collection<UserStoryAssign> findManyUserStoryAssignsWithDraftModeProjectByUserStoryId(int id);

	@Query("select usa from UserStoryAssign usa where usa.userStory.id = :userStoryId and usa.project.id = :projectId")
	UserStoryAssign findOneUserStoryAssignByUserStoryIdAndProjectId(int userStoryId, int projectId);

	@Query("select p from Project p where p.id = :id")
	Project findOneProjectById(int id);

	@Query("select us from UserStory us where us.id = :id")
	UserStory findOneUserStoryById(int id);

}
