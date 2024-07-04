/*
 * ManagerUserStoryAssignCreateService.java
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
public class ManagerUserStoryAssignCreateService extends AbstractService<Manager, UserStoryAssign> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerUserStoryAssignRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int userStoryId;
		int managerId;
		UserStory userStory;
		Collection<Project> candidates;

		userStoryId = super.getRequest().getData("userStoryId", int.class);
		userStory = this.repository.findOneUserStoryById(userStoryId);
		managerId = super.getRequest().getPrincipal().getActiveRoleId();
		candidates = this.repository.findManyDraftModeProjectsWithoutUserStoryByManagerIdAndUserStoryId(managerId, userStoryId);

		status = userStory != null && //
			!candidates.isEmpty() && //
			super.getRequest().getPrincipal().hasRole(userStory.getManager());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int userStoryId;
		UserStory userStory;
		UserStoryAssign object;

		userStoryId = super.getRequest().getData("userStoryId", int.class);
		userStory = this.repository.findOneUserStoryById(userStoryId);

		object = new UserStoryAssign();
		object.setUserStory(userStory);

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

		if (!super.getBuffer().getErrors().hasErrors("project")) {
			Collection<Project> projectsAssigned;

			projectsAssigned = this.repository.findManyProjectsWithUserStoryAssignedByUserStoryId(object.getUserStory().getId());

			super.state(!projectsAssigned.contains(object.getProject()), "project", "manager.user-story-assign.form.error.user-story-already-assigned-to-project");
			super.state(object.getProject().isDraftMode(), "project", "manager.user-story-assign.form.error.project-is-published");
		}
	}

	@Override
	public void perform(final UserStoryAssign object) {
		assert object != null;
		object.setId(0);

		this.repository.save(object);
	}

	@Override
	public void unbind(final UserStoryAssign object) {
		assert object != null;

		int managerId;
		int userStoryId;
		Collection<Project> draftModeProjectsNotAssigned;
		SelectChoices choices;
		Dataset dataset;

		managerId = super.getRequest().getPrincipal().getActiveRoleId();
		userStoryId = super.getRequest().getData("userStoryId", int.class);

		draftModeProjectsNotAssigned = this.repository.findManyDraftModeProjectsWithoutUserStoryByManagerIdAndUserStoryId(managerId, userStoryId);

		choices = SelectChoices.from(draftModeProjectsNotAssigned, "title", object.getProject());

		dataset = super.unbind(object, "userStory");
		dataset.put("project", choices.getSelected().getKey());
		dataset.put("projects", choices);
		dataset.put("userStoryId", object.getUserStory().getId());

		super.getResponse().addData(dataset);
	}

}
