/*
 * DeveloperTrainingSessionListService.java
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

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.training.TrainingModule;
import acme.entities.training.TrainingSession;
import acme.roles.Developer;

@Service
public class DeveloperTrainingSessionListService extends AbstractService<Developer, TrainingSession> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private DeveloperTrainingSessionRepository repository;
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
		Collection<TrainingSession> objects;
		int masterId = super.getRequest().getData("masterId", int.class);
		objects = this.repository.findTrainingSessionsOfTrainingModule(masterId);
		super.getResponse().addGlobal("masterId", masterId);
		super.getBuffer().addData(objects);
	}
	@Override
	public void unbind(final TrainingSession object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "draftMode");
		if (object.isDraftMode())
			dataset.put("draftMode", "✓");
		else
			dataset.put("draftMode", "✗");
		super.getResponse().addData(dataset);
	}

}
