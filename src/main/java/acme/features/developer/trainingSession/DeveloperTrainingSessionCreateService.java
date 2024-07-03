/*
 * DeveloperTrainingSessionCreateService.java
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.training.TrainingModule;
import acme.entities.training.TrainingSession;
import acme.roles.Developer;
import acme.utils.SpamRepository;

@Service
public class DeveloperTrainingSessionCreateService extends AbstractService<Developer, TrainingSession> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private DeveloperTrainingSessionRepository	repository;

	@Autowired
	private SpamRepository						spamRepository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		int moduleId = super.getRequest().getData("masterId", int.class);
		TrainingModule module = this.repository.findTrainingModuleById(moduleId);
		status = module != null && super.getRequest().getPrincipal().hasRole(module.getDeveloper());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrainingSession session;
		TrainingModule module;
		int id;

		id = super.getRequest().getData("masterId", int.class);
		module = this.repository.findTrainingModuleById(id);
		super.getResponse().addGlobal("masterId", id);
		session = new TrainingSession();
		session.setTrainingModule(module);
		session.setDraftMode(true);

		super.getBuffer().addData(session);
	}

	@Override
	public void bind(final TrainingSession object) {
		assert object != null;

		super.bind(object, "code", "startPeriod", "endPeriod", "location", "instructor", "contactEmail", "link");
	}

	@Override
	public void validate(final TrainingSession object) {
		assert object != null;
		if (!super.getBuffer().getErrors().hasErrors("code")) {
			boolean duplicatedCode = this.repository.findTrainingSessions().stream().anyMatch(ts -> ts.getCode().equals(object.getCode()));
			super.state(!duplicatedCode, "code", "developer.training-session.form.error.duplicatedCode");
		}
		if (!super.getBuffer().getErrors().hasErrors("location"))
			super.state(!this.spamRepository.checkTextValue(super.getRequest().getData("location", String.class)), "location", "developer.training-session.form.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("instructor"))
			super.state(!this.spamRepository.checkTextValue(super.getRequest().getData("instructor", String.class)), "instructor", "developer.training-session.form.error.spam");
		long weekSeconds = 7 * 24 * 60 * 60;
		if (!super.getBuffer().getErrors().hasErrors("endPeriod")) {
			super.state(object.getEndPeriod().after(object.getStartPeriod()), "startPeriod", "developer.training-session.form.error.endPeriod.not-after-startPeriod");
			super.state((object.getEndPeriod().getTime() - object.getStartPeriod().getTime()) / 1000 >= weekSeconds, "endPeriod", "developer.training-session.form.error.periodNotDuringOneWeek");
		}
		int moduleId = super.getRequest().getData("masterId", int.class);
		TrainingModule module = this.repository.findTrainingModuleById(moduleId);
		if (!super.getBuffer().getErrors().hasErrors("startPeriod"))
			super.state((object.getStartPeriod().getTime() - module.getCreationMoment().getTime()) / 1000 >= weekSeconds, "startPeriod", "developer.training-session.form.error.periodNotStartAWeekAfterCreationMoment");
	}

	@Override
	public void perform(final TrainingSession object) {
		assert object != null;
		object.setId(0);
		this.repository.save(object);
	}

	@Override
	public void unbind(final TrainingSession object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "startPeriod", "endPeriod", "location", "instructor", "contactEmail", "link");

		super.getResponse().addData(dataset);
	}

}
