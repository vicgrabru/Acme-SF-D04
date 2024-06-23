/*
 * ManagerUserStoryUpdateService.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.manager.userStory;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.project.Project;
import acme.entities.project.UserStory;
import acme.entities.project.UserStoryAssign;
import acme.entities.project.UserStoryPriority;
import acme.roles.Manager;
import acme.utils.SpamRepository;

@Service
public class ManagerUserStoryUpdateService extends AbstractService<Manager, UserStory> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerUserStoryRepository	repository;

	@Autowired
	private SpamRepository				spamRepository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int userStoryId;
		UserStory userStory;
		Collection<UserStoryAssign> relationships;

		userStoryId = super.getRequest().getData("id", int.class);

		relationships = this.repository.findManyUserStoryAssignsByUserStoryId(userStoryId);
		userStory = this.repository.findOneUserStoryById(userStoryId);

		status = userStory != null && //
			userStory.isDraftMode() && //
			relationships.stream().allMatch(x -> x.getProject().isDraftMode()) && //
			super.getRequest().getPrincipal().hasRole(userStory.getManager());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		UserStory object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneUserStoryById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final UserStory object) {
		assert object != null;

		super.bind(object, "title", "description", "estimatedCost", "acceptanceCriteria", "priority", "optionalLink");
	}

	@Override
	public void validate(final UserStory object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("title"))
			super.state(!this.spamRepository.checkTextValue(object.getTitle()), "title", "manager.user-story.form.error.spam-in-title");

		if (!super.getBuffer().getErrors().hasErrors("description"))
			super.state(!this.spamRepository.checkTextValue(object.getDescription()), "description", "manager.user-story.form.error.spam-in-description");

		if (!super.getBuffer().getErrors().hasErrors("acceptanceCriteria"))
			super.state(!this.spamRepository.checkTextValue(object.getAcceptanceCriteria()), "acceptanceCriteria", "manager.user-story.form.error.spam-in-acceptance-criteria");
	}

	@Override
	public void perform(final UserStory object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final UserStory object) {
		assert object != null;

		Collection<Project> draftModeProjectsAssigned;
		Collection<Project> draftModeProjectsUnassigned;
		SelectChoices choices;
		Dataset dataset;

		int managerId;
		int userStoryId;

		managerId = super.getRequest().getPrincipal().getActiveRoleId();
		userStoryId = object.getId();

		choices = SelectChoices.from(UserStoryPriority.class, object.getPriority());

		dataset = super.unbind(object, "title", "description", "estimatedCost", "acceptanceCriteria", "priority", "optionalLink", "draftMode");
		dataset.put("userStoryId", userStoryId);
		dataset.put("priorities", choices);

		draftModeProjectsAssigned = this.repository.findManyDraftModeProjectsWithUserStoryAssignedByUserStoryId(userStoryId);
		draftModeProjectsUnassigned = this.repository.findManyDraftModeProjectsWithoutUserStoryByManagerIdAndUserStoryId(managerId, userStoryId);

		dataset.put("showAssignButton", !draftModeProjectsUnassigned.isEmpty());
		dataset.put("showUnassignButton", !draftModeProjectsAssigned.isEmpty());

		super.getResponse().addData(dataset);
	}

}
