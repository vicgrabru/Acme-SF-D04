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
import spamDetector.SpamDetector;

@Service
public class DeveloperTrainingModuleCreateService extends AbstractService<Developer, TrainingModule> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private DeveloperTrainingModuleRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		TrainingModule object;
		Date creationMoment;

		creationMoment = MomentHelper.getCurrentMoment();

		object = new TrainingModule();
		object.setCreationMoment(creationMoment);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final TrainingModule object) {
		assert object != null;

		super.bind(object, "code", "creationMoment", "details", "difficulty", "updateMoment", "startTotalTime", "endTotalTime", "link", "draftMode");
	}

	@Override
	public void validate(final TrainingModule object) {
		assert object != null;
		if (!super.getBuffer().getErrors().hasErrors("code"))
			super.state(!SpamDetector.checkTextValue(super.getRequest().getData("code", String.class)), "code", "developer.training-module.form.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("details"))
			super.state(!SpamDetector.checkTextValue(super.getRequest().getData("details", String.class)), "details", "developer.training-module.form.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("difficulty"))
			super.state(!SpamDetector.checkTextValue(super.getRequest().getData("difficulty", String.class)), "difficulty", "developer.training-module.form.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("link"))
			super.state(!SpamDetector.checkTextValue(super.getRequest().getData("link", String.class)), "link", "developer.training-module.form.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("startTotalTime"))
			super.state(object.getStartTotalTime().after(object.getCreationMoment()), "startTotalTime", "developer.training-module.form.error.startTotalTime.not-after-creationMoment");
		if (!super.getBuffer().getErrors().hasErrors("endTotalTime"))
			super.state(object.getEndTotalTime().after(object.getStartTotalTime()), "endTotalTime", "developer.training-module.form.error.endTotalTime.not-after-startTotalTime");
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

		dataset = super.unbind(object, "code", "creationMoment", "details", "difficulty", "updateMoment", "startTotalTime", "endTotalTime", "link", "draftMode");
		final SelectChoices choices;
		choices = SelectChoices.from(Difficulty.class, object.getDifficulty());
		dataset.put("difficulty", choices.getSelected().getKey());
		dataset.put("difficulties", choices);
		super.getResponse().addData(dataset);
	}

}
