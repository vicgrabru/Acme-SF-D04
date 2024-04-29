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

import java.math.BigDecimal;
import java.math.RoundingMode;
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

@Service
public class ClientContractPublishService extends AbstractService<Client, Contract> {

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

	}

	@Override
	public void validate(final Contract object) {
		assert object != null;

		int projectId = object.getProject().getId();
		Money projectCost = this.exchangeRepo.exchangeMoney(object.getProject().getCost());
		double remainingCost = projectCost.getAmount() - this.repository.findPublishedContractsByProjectId(projectId).stream().map(c -> this.exchangeRepo.exchangeMoney(c.getBudget()).getAmount()).reduce(0.0, Double::sum);
		remainingCost = BigDecimal.valueOf(remainingCost).setScale(2, RoundingMode.HALF_UP).doubleValue();
		if (!super.getBuffer().getErrors().hasErrors("budget"))
			super.state(remainingCost >= this.exchangeRepo.exchangeMoney(object.getBudget()).getAmount(), "budget", "client.contract.form.error.project-cost-exceeded");
	}

	@Override
	public void perform(final Contract object) {
		assert object != null;

		object.setDraftMode(false);
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

		Money eb = this.exchangeRepo.exchangeMoney(object.getBudget());
		dataset.put("exchangedBudget", eb);

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
