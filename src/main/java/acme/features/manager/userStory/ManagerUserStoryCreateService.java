/*
 * ManagerUserStoryCreateService.java
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
public class ManagerUserStoryCreateService extends AbstractService<Manager, UserStory> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerUserStoryRepository	repository;

	@Autowired
	private SpamRepository				spamRepository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int projectId;
		Project project;

		projectId = super.getRequest().getData("masterId", int.class);
		project = this.repository.findOneProjectById(projectId);
		status = project != null && project.isDraftMode() && super.getRequest().getPrincipal().hasRole(project.getManager());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		UserStory object;
		Manager manager;

		manager = this.repository.findOneManagerById(super.getRequest().getPrincipal().getActiveRoleId());

		object = new UserStory();
		object.setManager(manager);
		object.setDraftMode(true);

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

		int projectId;
		Project project;
		UserStoryAssign relationship;

		object.setId(0);

		this.repository.save(object);

		projectId = super.getRequest().getData("masterId", int.class);
		project = this.repository.findOneProjectById(projectId);

		relationship = new UserStoryAssign();

		relationship.setUserStory(object);
		relationship.setProject(project);

		this.repository.save(relationship);
	}

	@Override
	public void unbind(final UserStory object) {
		assert object != null;

		int projectId;

		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(UserStoryPriority.class, object.getPriority());

		dataset = super.unbind(object, "title", "description", "estimatedCost", "acceptanceCriteria", "priority", "optionalLink", "draftMode");
		projectId = super.getRequest().getData("masterId", int.class);

		dataset.put("masterId", projectId);
		dataset.put("priorities", choices);

		super.getResponse().addData(dataset);
	}

}
