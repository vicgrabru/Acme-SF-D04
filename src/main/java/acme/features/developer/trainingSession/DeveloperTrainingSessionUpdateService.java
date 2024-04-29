/*
 * EmployerApplicationUpdateService.java
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
import acme.entities.training.TrainingSession;
import acme.roles.Developer;
import spamDetector.SpamDetector;

@Service
public class DeveloperTrainingSessionUpdateService extends AbstractService<Developer, TrainingSession> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private DeveloperTrainingSessionRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int trainingSessionId;
		TrainingSession trainingSession;

		trainingSessionId = super.getRequest().getData("id", int.class);
		trainingSession = this.repository.findTrainingSessionById(trainingSessionId);
		status = trainingSession != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrainingSession object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findTrainingSessionById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final TrainingSession object) {
		assert object != null;
		super.bind(object, "code", "startPeriod", "endPeriod", "location", "instructor", "contactEmail", "link", "draftMode");
	}

	@Override
	public void validate(final TrainingSession object) {
		assert object != null;
		if (!super.getBuffer().getErrors().hasErrors("code"))
			super.state(!SpamDetector.checkTextValue(super.getRequest().getData("code", String.class)), "code", "developer.training-session.form.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("location"))
			super.state(!SpamDetector.checkTextValue(super.getRequest().getData("location", String.class)), "location", "developer.training-session.form.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("instructor"))
			super.state(!SpamDetector.checkTextValue(super.getRequest().getData("instructor", String.class)), "instructor", "developer.training-session.form.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("contactEmail"))
			super.state(!SpamDetector.checkTextValue(super.getRequest().getData("contactEmail", String.class)), "contactEmail", "developer.training-session.form.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("link"))
			super.state(!SpamDetector.checkTextValue(super.getRequest().getData("link", String.class)), "link", "developer.training-session.form.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("endPeriod"))
			super.state(object.getEndPeriod().after(object.getStartPeriod()), "startPeriod", "developer.training-session.form.error.endPeriod.not-after-startPeriod");
	}

	@Override
	public void perform(final TrainingSession object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final TrainingSession object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "startPeriod", "endPeriod", "location", "instructor", "contactEmail", "link", "draftMode");

		super.getResponse().addData(dataset);
	}

}
