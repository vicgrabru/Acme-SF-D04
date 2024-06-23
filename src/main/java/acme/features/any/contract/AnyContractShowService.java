/*
 * AnyContractShowService.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.any.contract;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Any;
import acme.client.data.datatypes.Money;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.contract.Contract;
import acme.utils.MoneyExchangeRepository;

@Service
public class AnyContractShowService extends AbstractService<Any, Contract> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyContractRepository	repository;

	@Autowired
	private MoneyExchangeRepository	exchangeRepo;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int contractId;
		Contract contract;

		contractId = super.getRequest().getData("id", int.class);
		contract = this.repository.findContractById(contractId);
		status = contract != null && !contract.isDraftMode();

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
	public void unbind(final Contract object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "goals", "budget", "customerName", "providerName", "instantiationMoment");
		dataset.put("project", object.getProject().getTitle());

		dataset.put("projectId", object.getProject().getId());
		dataset.put("masterId", object.getId());

		Money eb = this.exchangeRepo.exchangeMoney(object.getBudget());
		dataset.put("exchangedBudget", eb);

		super.getResponse().addData(dataset);
	}

}
