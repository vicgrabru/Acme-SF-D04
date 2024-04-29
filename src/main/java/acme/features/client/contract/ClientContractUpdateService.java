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

package acme.features.client.contract;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.datatypes.Money;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.contract.Contract;
import acme.entities.project.Project;
import acme.roles.Client;
import acme.utils.MoneyExchangeRepository;
import spamDetector.SpamDetector;

@Service
public class ClientContractUpdateService extends AbstractService<Client, Contract> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ClientContractRepository	repository;

	@Autowired
	private MoneyExchangeRepository		exchangeRepo;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int contractId;
		Contract contract;

		contractId = super.getRequest().getData("id", int.class);
		contract = this.repository.findContractById(contractId);
		status = contract != null && super.getRequest().getPrincipal().hasRole(contract.getClient());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Contract object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findContractById(id);
		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Contract object) {
		assert object != null;

		super.bind(object, "goals", "budget", "provider", "customerName", "providerName");
	}

	@Override
	public void validate(final Contract object) {
		assert object != null;
		String currencies;

		if (!super.getBuffer().getErrors().hasErrors("budget")) {
			currencies = this.repository.findAcceptedCurrencies();
			super.state(currencies.contains(object.getBudget().getCurrency()), "budget", "client.contract.form.error.bugdet.invalid-currency");
		}
		if (!super.getBuffer().getErrors().hasErrors("budget"))
			super.state(object.getBudget().getAmount() >= 0., "budget", "client.contract.form.error.bugdet.negative-budget");
		if (!super.getBuffer().getErrors().hasErrors("budget"))
			super.state(this.exchangeRepo.exchangeMoney(object.getBudget()).getAmount() <= this.exchangeRepo.exchangeMoney(object.getProject().getCost()).getAmount(), "budget", "client.contract.form.error.budget.budget-over-project-cost");

		if (!super.getBuffer().getErrors().hasErrors("goals"))
			super.state(!SpamDetector.checkTextValue(object.getGoals()), "goals", "client.contract.form.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("providerName"))
			super.state(!SpamDetector.checkTextValue(object.getProviderName()), "providerName", "client.contract.form.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("customerName"))
			super.state(!SpamDetector.checkTextValue(object.getCustomerName()), "customerName", "client.contract.form.error.spam");
	}

	@Override
	public void perform(final Contract object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Contract object) {
		assert object != null;

		Dataset dataset;
		SelectChoices choicesProject;

		Collection<Project> projects;

		projects = this.repository.findPublishedProjects();

		choicesProject = SelectChoices.from(projects, "title", object.getProject());

		dataset = super.unbind(object, "code", "goals", "budget", "customerName", "providerName", "instantiationMoment", "draftMode");
		dataset.put("project", choicesProject.getSelected());
		dataset.put("projects", choicesProject);

		dataset.put("projectId", object.getProject().getId());
		dataset.put("contractId", object.getId());
		dataset.put("readOnlyCode", true);

		if (!super.getBuffer().getErrors().hasErrors("budget") || super.getBuffer().getErrors().getFirstError("budget").equals("The budget amount can't be higher than the project cost")
			|| super.getBuffer().getErrors().getFirstError("budget").equals("La cantidad de presupuesto no puede ser superior al coste del proyecto")) {
			Money eb = this.exchangeRepo.exchangeMoney(object.getBudget());
			dataset.put("exchangedBudget", eb);
		}

		int projectId = object.getProject().getId();
		Money projectCost = this.exchangeRepo.exchangeMoney(object.getProject().getCost());
		Double remainingCost = projectCost.getAmount() - this.repository.findPublishedContractsByProjectId(projectId).stream().map(c -> this.exchangeRepo.exchangeMoney(c.getBudget()).getAmount()).reduce(0.0, Double::sum);
		Money rCost = new Money();
		rCost.setAmount(remainingCost);
		rCost.setCurrency(projectCost.getCurrency());
		dataset.put("remainingCost", rCost);

		super.getResponse().addData(dataset);
	}

}
