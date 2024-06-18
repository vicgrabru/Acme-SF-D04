/*
 * ClientDashboardShowService.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.client.clientDashboard;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.datatypes.Money;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.configuration.SystemConfiguration;
import acme.forms.ClientDashboard;
import acme.roles.Client;
import acme.utils.MoneyExchangeRepository;
import acme.utils.MoneyUtils;

@Service
public class ClientClientDashboardShowService extends AbstractService<Client, ClientDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ClientClientDashboardRepository	repository;

	@Autowired
	private MoneyExchangeRepository			exchangeRepo;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

		int clientId;
		ClientDashboard dashboard;

		Integer below25CompletenessProgressLogs;
		Integer between25and50CompletenessProgressLogs;
		Integer between50and75CompletenessProgressLogs;
		Integer above75CompletenessProgressLogs;

		Money avgContractBudget;
		Money stdContractBudget;
		Money minContractBudget;
		Money maxContractBudget;

		Collection<Money> budgetOfContracts;
		SystemConfiguration systemConfiguration;

		clientId = super.getRequest().getPrincipal().getActiveRoleId();

		below25CompletenessProgressLogs = this.repository.below25CompletenessProgressLogsByClientId(clientId);
		between25and50CompletenessProgressLogs = this.repository.between25and50CompletenessProgressLogsByClientId(clientId);
		between50and75CompletenessProgressLogs = this.repository.between50and75CompletenessProgressLogsByClientId(clientId);
		above75CompletenessProgressLogs = this.repository.above75CompletenessProgressLogsByClientId(clientId);

		budgetOfContracts = this.repository.getBudgetOfContractsByClientId(clientId).stream().map(b -> this.exchangeRepo.exchangeMoney(b)).toList();
		systemConfiguration = this.repository.getSystemConfiguration();

		if (!budgetOfContracts.isEmpty()) {
			avgContractBudget = MoneyUtils.getAvg(budgetOfContracts, systemConfiguration.getSystemCurrency());
			stdContractBudget = MoneyUtils.getStd(budgetOfContracts, systemConfiguration.getSystemCurrency());
			minContractBudget = MoneyUtils.getMin(budgetOfContracts, systemConfiguration.getSystemCurrency());
			maxContractBudget = MoneyUtils.getMax(budgetOfContracts, systemConfiguration.getSystemCurrency());
		} else {
			avgContractBudget = null;
			stdContractBudget = null;
			minContractBudget = null;
			maxContractBudget = null;
		}

		dashboard = new ClientDashboard();

		dashboard.setBelow25CompletenessProgressLogs(below25CompletenessProgressLogs);
		dashboard.setBetween25and50CompletenessProgressLogs(between25and50CompletenessProgressLogs);
		dashboard.setBetween50and75CompletenessProgressLogs(between50and75CompletenessProgressLogs);
		dashboard.setAbove75CompletenessProgressLogs(above75CompletenessProgressLogs);

		dashboard.setAvgContractBudget(avgContractBudget);
		dashboard.setStdContractBudget(stdContractBudget);
		dashboard.setMinContractBudget(minContractBudget);
		dashboard.setMaxContractBudget(maxContractBudget);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final ClientDashboard object) {
		Dataset dataset;

		dataset = super.unbind(object, "below25CompletenessProgressLogs", "between25and50CompletenessProgressLogs", "between50and75CompletenessProgressLogs", "above75CompletenessProgressLogs", "avgContractBudget", "stdContractBudget", "minContractBudget",
			"maxContractBudget");

		super.getResponse().addData(dataset);
	}

}
