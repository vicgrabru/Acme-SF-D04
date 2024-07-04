/*
 * DeveloperTrainingModuleUpdateService.java
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

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.training.Difficulty;
import acme.entities.training.TrainingModule;
import acme.roles.Developer;
import acme.utils.SpamRepository;

@Service
public class DeveloperTrainingModuleUpdateService extends AbstractService<Developer, TrainingModule> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private DeveloperTrainingModuleRepository	repository;

	@Autowired
	private SpamRepository						spamRepository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int moduleId = super.getRequest().getData("id", int.class);
		TrainingModule module = this.repository.findTrainingModuleById(moduleId);
		status = module != null && super.getRequest().getPrincipal().hasRole(module.getDeveloper());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrainingModule object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findTrainingModuleById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final TrainingModule object) {
		assert object != null;
		Date updateMoment;

		updateMoment = MomentHelper.getCurrentMoment();

		super.bind(object, "code", "details", "difficulty", "totalTime", "link", "project");
		object.setUpdateMoment(updateMoment);
	}

	@Override
	public void validate(final TrainingModule object) {
		assert object != null;
		if (!super.getBuffer().getErrors().hasErrors("code")) {
			boolean duplicatedCode = this.repository.findTrainingModules(super.getRequest().getPrincipal().getActiveRoleId()).stream().anyMatch(tr -> tr.getCode().equals(object.getCode()));
			super.state(!duplicatedCode, "code", "developer.training-module.form.error.duplicatedCode");
		}
		if (!super.getBuffer().getErrors().hasErrors("details"))
			super.state(!this.spamRepository.checkTextValue(super.getRequest().getData("details", String.class)), "details", "developer.training-module.form.error.spam");
	}

	@Override
	public void perform(final TrainingModule object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final TrainingModule object) {
		assert object != null;
		Dataset dataset;
		dataset = super.unbind(object, "code", "creationMoment", "details", "difficulty", "updateMoment", "totalTime", "link", "draftMode", "project");
		final SelectChoices difficultyChoices;
		final SelectChoices projectChoices;
		difficultyChoices = SelectChoices.from(Difficulty.class, object.getDifficulty());
		dataset.put("difficulty", difficultyChoices.getSelected().getKey());
		dataset.put("difficulties", difficultyChoices);
		projectChoices = SelectChoices.from(this.repository.findProjects(), "title", object.getProject());
		dataset.put("project", projectChoices.getSelected().getKey());
		dataset.put("projects", projectChoices);
		super.getResponse().addData(dataset);
	}

}
