/*
 * ManagerProjectUpdateService.java
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

import acme.client.data.datatypes.Money;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.project.Project;
import acme.roles.Manager;
import acme.utils.MoneyExchangeRepository;
import acme.utils.SpamRepository;

@Service
public class ManagerProjectUpdateService extends AbstractService<Manager, Project> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerProjectRepository	repository;

	@Autowired
	private MoneyExchangeRepository		exchangeRepository;

	@Autowired
	private SpamRepository				spamRepository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int projectId;
		Project project;

		projectId = super.getRequest().getData("id", int.class);
		project = this.repository.findOneProjectById(projectId);
		status = project != null && //
			project.isDraftMode() && //
			super.getRequest().getPrincipal().hasRole(project.getManager());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Project object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneProjectById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Project object) {
		assert object != null;

		super.bind(object, "title", "abstractField", "hasFatalErrors", "cost", "optionalLink");
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

		if (!super.getBuffer().getErrors().hasErrors("title"))
			super.state(!this.spamRepository.checkTextValue(object.getTitle()), "title", "manager.project.form.error.spam-in-title");

		if (!super.getBuffer().getErrors().hasErrors("abstractField"))
			super.state(!this.spamRepository.checkTextValue(object.getAbstractField()), "abstractField", "manager.project.form.error.spam-in-abstract-field");
	}

	@Override
	public void perform(final Project object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Project object) {
		assert object != null;

		Money exchangedCost;
		Dataset dataset;
		boolean isAcceptedCurrency;
		boolean isSystemCurrency;

		dataset = super.unbind(object, "code", "title", "abstractField", "hasFatalErrors", "cost", "optionalLink", "draftMode");
		dataset.put("masterId", object.getId());
		dataset.put("readOnlyCode", true);

		isSystemCurrency = this.exchangeRepository.findSystemCurrency().equals(object.getCost().getCurrency());
		isAcceptedCurrency = this.repository.findAcceptedCurrenciesInSystem().contains(object.getCost().getCurrency());

		dataset.put("showExchangedCost", isAcceptedCurrency && !isSystemCurrency);

		exchangedCost = this.exchangeRepository.exchangeMoney(object.getCost());
		dataset.put("exchangedCost", exchangedCost);

		super.getResponse().addData(dataset);
	}

}
