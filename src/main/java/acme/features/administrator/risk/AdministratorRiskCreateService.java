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

package acme.features.administrator.risk;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Administrator;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.project.Project;
import acme.entities.risk.Risk;
import spamDetector.SpamDetector;

@Service
public class AdministratorRiskCreateService extends AbstractService<Administrator, Risk> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorRiskRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Risk object;

		object = new Risk();

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Risk object) {
		assert object != null;

		super.bind(object, "reference", "identificationDate", "impact", "probability", "description", "link", "project");

	}

	@Override
	public void validate(final Risk object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("description"))
			super.state(!SpamDetector.checkTextValue(object.getDescription()), "description", "administrator.risk.form.error.spam-in-description");

		if (!super.getBuffer().getErrors().hasErrors("project")) {
			Collection<Project> projects;

			projects = this.repository.findPublishedProjects();
			super.state(projects.contains(object.getProject()), "project", "administrator.risk.form.error.selected-project-not-published");
		}

		if (!super.getBuffer().getErrors().hasErrors("reference")) {
			Risk existing;

			existing = this.repository.findOneRiskByReference(object.getReference());
			super.state(existing == null, "reference", "administrator.risk.form.error.reference-duplicated");
		}
	}

	@Override
	public void perform(final Risk object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Risk object) {
		assert object != null;

		Dataset dataset;
		SelectChoices choicesProject;
		Collection<Project> projects;

		projects = this.repository.findPublishedProjects();
		choicesProject = SelectChoices.from(projects, "title", object.getProject());

		dataset = super.unbind(object, "reference", "identificationDate", "impact", "probability", "description", "link");
		dataset.put("riskValue", object.getValue());
		dataset.put("readOnlyReference", false);
		dataset.put("project", choicesProject.getSelected());
		dataset.put("projects", choicesProject);

		super.getResponse().addData(dataset);
	}

}
