/*
 * ManagerProjectCreateService.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.manager.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.project.Project;
import acme.roles.Manager;
import acme.utils.SpamRepository;

@Service
public class ManagerProjectCreateService extends AbstractService<Manager, Project> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerProjectRepository	repository;

	@Autowired
	private SpamRepository				spamRepository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Project object;
		Manager manager;

		manager = this.repository.findOneManagerById(super.getRequest().getPrincipal().getActiveRoleId());

		object = new Project();
		object.setManager(manager);
		object.setDraftMode(true);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Project object) {
		assert object != null;

		super.bind(object, "code", "title", "abstractField", "hasFatalErrors", "cost", "optionalLink");
	}

	@Override
	public void validate(final Project object) {
		assert object != null;
		String currencies;

		if (!super.getBuffer().getErrors().hasErrors("cost")) {
			currencies = this.repository.findAcceptedCurrenciesInSystem();
			super.state(currencies.contains(object.getCost().getCurrency()), "cost", "manager.project.form.error.cost.invalid-currency");

			super.state(object.getCost().getAmount() >= 0., "cost", "manager.project.form.error.cost.negative");
		}

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Project existing;
			existing = this.repository.findOneProjectByCode(object.getCode());
			super.state(existing == null, "code", "manager.project.form.error.code-duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("title"))
			super.state(!this.spamRepository.checkTextValue(object.getTitle()), "title", "manager.project.form.error.spam-in-title");

		if (!super.getBuffer().getErrors().hasErrors("abstractField"))
			super.state(!this.spamRepository.checkTextValue(object.getAbstractField()), "abstractField", "manager.project.form.error.spam-in-abstract-field");

	}

	@Override
	public void perform(final Project object) {
		assert object != null;
		object.setId(0);

		this.repository.save(object);
	}

	@Override
	public void unbind(final Project object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "title", "abstractField", "hasFatalErrors", "cost", "optionalLink", "draftMode");
		dataset.put("masterId", object.getId());
		dataset.put("readOnlyCode", false);

		super.getResponse().addData(dataset);
	}

}
