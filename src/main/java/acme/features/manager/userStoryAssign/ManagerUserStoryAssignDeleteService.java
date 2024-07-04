/*
 * ManagerUserStoryAssignDeleteService.java
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.project.Project;
import acme.entities.project.UserStory;
import acme.entities.project.UserStoryAssign;
import acme.roles.Manager;

@Service
public class ManagerUserStoryAssignDeleteService extends AbstractService<Manager, UserStoryAssign> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerUserStoryAssignRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int userStoryId;
		UserStory userStory;
		Collection<Project> candidates;

		userStoryId = super.getRequest().getData("userStoryId", int.class);
		userStory = this.repository.findOneUserStoryById(userStoryId);
		candidates = this.repository.findManyDraftModeProjectsWithUserStoryAssignedByUserStoryId(userStoryId);

		status = userStory != null && //
			!candidates.isEmpty() && //
			super.getRequest().getPrincipal().hasRole(userStory.getManager());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int userStoryId;

		UserStoryAssign object;
		Collection<UserStoryAssign> options;

		userStoryId = super.getRequest().getData("userStoryId", int.class);
		options = this.repository.findManyUserStoryAssignsWithDraftModeProjectByUserStoryId(userStoryId);

		object = options.stream().findFirst().orElse(null);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final UserStoryAssign object) {
		assert object != null;

		int userStoryId;
		int projectId;

		UserStory userStory;
		Project project;

		userStoryId = super.getRequest().getData("userStoryId", int.class);
		userStory = this.repository.findOneUserStoryById(userStoryId);
		object.setUserStory(userStory);

		projectId = super.getRequest().getData("project", int.class);
		project = this.repository.findOneProjectById(projectId);
		object.setProject(project);
	}

	@Override
	public void validate(final UserStoryAssign object) {
		assert object != null;

		Collection<Project> projectsAssigned;

		projectsAssigned = this.repository.findManyDraftModeProjectsWithUserStoryAssignedByUserStoryId(object.getUserStory().getId());

		super.state(projectsAssigned.contains(object.getProject()), "project", "manager.user-story-assign.form.error.unassigning-user-story-not-assigned-to-project");
		super.state(object.getProject() != null, "project", "manager.user-story-assign.form.error.project-is-null");
		if (object.getProject() != null)
			super.state(object.getProject().isDraftMode(), "project", "manager.user-story-assign.form.error.project-is-published");
	}

	@Override
	public void perform(final UserStoryAssign object) {
		assert object != null;

		UserStoryAssign toDelete;

		toDelete = this.repository.findOneUserStoryAssignByUserStoryIdAndProjectId(object.getUserStory().getId(), object.getProject().getId());

		this.repository.delete(toDelete);
	}

	@Override
	public void unbind(final UserStoryAssign object) {
		assert object != null;

		Collection<Project> draftModeProjects;
		SelectChoices choices;
		Dataset dataset;

		draftModeProjects = this.repository.findManyDraftModeProjectsWithUserStoryAssignedByUserStoryId(super.getRequest().getData("userStoryId", int.class));

		choices = SelectChoices.from(draftModeProjects, "title", object.getProject());

		dataset = super.unbind(object, "userStory");
		dataset.put("project", choices.getSelected().getKey());
		dataset.put("projects", choices);
		dataset.put("userStoryId", object.getUserStory().getId());

		super.getResponse().addData(dataset);
	}

}
