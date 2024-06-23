/*
 * DeveloperTrainingModulePublishService.java
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

import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.training.TrainingModule;
import acme.roles.Developer;
import acme.utils.SpamRepository;

@Service
public class DeveloperTrainingModulePublishService extends AbstractService<Developer, TrainingModule> {

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
		status = module != null && module.isDraftMode() && super.getRequest().getPrincipal().hasRole(module.getDeveloper());

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

		super.bind(object, "code", "creationMoment", "details", "difficulty", "updateMoment", "totalTime", "link", "project");
		object.setUpdateMoment(updateMoment);
	}

	@Override
	public void validate(final TrainingModule object) {
		assert object != null;
		if (!super.getBuffer().getErrors().hasErrors("draftMode"))
			super.state(!this.repository.findTrainingSessionsOfTrainingModule(object.getId()).isEmpty(), "draftMode", "developer.training-module.form.error.emptyTrainingSessions");
		if (!super.getBuffer().getErrors().hasErrors("code"))
			super.state(!this.spamRepository.checkTextValue(super.getRequest().getData("code", String.class)), "code", "developer.training-module.form.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("details"))
			super.state(!this.spamRepository.checkTextValue(super.getRequest().getData("details", String.class)), "details", "developer.training-module.form.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("difficulty"))
			super.state(!this.spamRepository.checkTextValue(super.getRequest().getData("difficulty", String.class)), "difficulty", "developer.training-module.form.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("link"))
			super.state(!this.spamRepository.checkTextValue(super.getRequest().getData("link", String.class)), "link", "developer.training-module.form.error.spam");

	}

	@Override
	public void perform(final TrainingModule object) {
		assert object != null;
		object.setDraftMode(false);
		this.repository.save(object);
	}
}
