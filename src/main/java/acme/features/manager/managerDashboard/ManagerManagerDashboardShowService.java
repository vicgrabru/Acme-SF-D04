/*
 * AdministratorDashboardShowService.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.manager.managerDashboard;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.datatypes.Money;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.configuration.SystemConfiguration;
import acme.forms.ManagerDashboard;
import acme.roles.Manager;
import acme.utils.MoneyExchangeRepository;
import acme.utils.MoneyUtils;

@Service
public class ManagerManagerDashboardShowService extends AbstractService<Manager, ManagerDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerManagerDashboardRepository	repository;

	@Autowired
	private MoneyExchangeRepository				exchangeRepository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

		int managerId;
		ManagerDashboard dashboard;

		Integer totalNumberOfUserStoriesWithMustPriority;
		Integer totalNumberOfUserStoriesWithShouldPriority;
		Integer totalNumberOfUserStoriesWithCouldPriority;
		Integer totalNumberOfUserStoriesWithWontPriority;

		Double avgEstimatedCostOfUserStories;
		Integer minEstimatedCostOfUserStories;
		Integer maxEstimatedCostOfUserStories;
		Double stdEstimatedCostOfUserStories;

		Money avgCostOfProjects;
		Money minCostOfProjects;
		Money maxCostOfProjects;
		Money stdCostOfProjects;

		Collection<Money> costOfProjects;
		List<Money> costOfProjectsExchanged;

		SystemConfiguration systemConfiguration;

		managerId = super.getRequest().getPrincipal().getActiveRoleId();

		totalNumberOfUserStoriesWithMustPriority = this.repository.totalNumberOfUserStoriesWithMustPriorityByManagerId(managerId).intValue();
		totalNumberOfUserStoriesWithShouldPriority = this.repository.totalNumberOfUserStoriesWithShouldPriorityByManagerId(managerId).intValue();
		totalNumberOfUserStoriesWithCouldPriority = this.repository.totalNumberOfUserStoriesWithCouldPriorityByManagerId(managerId).intValue();
		totalNumberOfUserStoriesWithWontPriority = this.repository.totalNumberOfUserStoriesWithWontPriorityByManagerId(managerId).intValue();

		avgEstimatedCostOfUserStories = this.repository.avgEstimatedCostOfUserStoriesByManagerId(managerId);
		minEstimatedCostOfUserStories = this.repository.minEstimatedCostOfUserStoriesByManagerId(managerId);
		maxEstimatedCostOfUserStories = this.repository.maxEstimatedCostOfUserStoriesByManagerId(managerId);
		stdEstimatedCostOfUserStories = this.repository.stdEstimatedCostOfUserStoriesByManagerId(managerId);

		avgEstimatedCostOfUserStories = avgEstimatedCostOfUserStories == null ? 0.0 : avgEstimatedCostOfUserStories;
		minEstimatedCostOfUserStories = minEstimatedCostOfUserStories == null ? 0 : minEstimatedCostOfUserStories;
		maxEstimatedCostOfUserStories = maxEstimatedCostOfUserStories == null ? 0 : maxEstimatedCostOfUserStories;
		stdEstimatedCostOfUserStories = stdEstimatedCostOfUserStories == null ? 0.0 : stdEstimatedCostOfUserStories;

		costOfProjects = this.repository.getCostOfAllProjectsByManagerId(managerId);
		systemConfiguration = this.repository.getSystemConfiguration();

		if (costOfProjects.isEmpty()) {
			Money dummy = new Money();
			dummy.setAmount(0.0);
			dummy.setCurrency(systemConfiguration.getSystemCurrency());

			avgCostOfProjects = dummy;
			minCostOfProjects = dummy;
			maxCostOfProjects = dummy;
			stdCostOfProjects = dummy;

		} else {
			costOfProjectsExchanged = costOfProjects.stream().map(x -> this.exchangeRepository.exchangeMoney(x)).toList();

			avgCostOfProjects = MoneyUtils.getAvg(costOfProjectsExchanged, systemConfiguration.getSystemCurrency());
			minCostOfProjects = MoneyUtils.getMin(costOfProjectsExchanged, systemConfiguration.getSystemCurrency());
			maxCostOfProjects = MoneyUtils.getMax(costOfProjectsExchanged, systemConfiguration.getSystemCurrency());
			stdCostOfProjects = MoneyUtils.getStd(costOfProjectsExchanged, systemConfiguration.getSystemCurrency());
		}
		dashboard = new ManagerDashboard();

		dashboard.setTotalNumberOfUserStoriesWithMustPriority(totalNumberOfUserStoriesWithMustPriority);
		dashboard.setTotalNumberOfUserStoriesWithShouldPriority(totalNumberOfUserStoriesWithShouldPriority);
		dashboard.setTotalNumberOfUserStoriesWithCouldPriority(totalNumberOfUserStoriesWithCouldPriority);
		dashboard.setTotalNumberOfUserStoriesWithWontPriority(totalNumberOfUserStoriesWithWontPriority);

		dashboard.setAvgEstimatedCostOfUserStories(avgEstimatedCostOfUserStories);
		dashboard.setMinEstimatedCostOfUserStories(minEstimatedCostOfUserStories);
		dashboard.setMaxEstimatedCostOfUserStories(maxEstimatedCostOfUserStories);
		dashboard.setStdEstimatedCostOfUserStories(stdEstimatedCostOfUserStories);

		dashboard.setAvgCostOfProjects(avgCostOfProjects);
		dashboard.setMinCostOfProjects(minCostOfProjects);
		dashboard.setMaxCostOfProjects(maxCostOfProjects);
		dashboard.setStdCostOfProjects(stdCostOfProjects);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final ManagerDashboard object) {
		Dataset dataset;

		dataset = super.unbind(object, //
			"totalNumberOfUserStoriesWithMustPriority", "totalNumberOfUserStoriesWithShouldPriority", //
			"totalNumberOfUserStoriesWithCouldPriority", "totalNumberOfUserStoriesWithWontPriority", //
			"avgEstimatedCostOfUserStories", "minEstimatedCostOfUserStories", //
			"maxEstimatedCostOfUserStories", "stdEstimatedCostOfUserStories", //
			"avgCostOfProjects", "minCostOfProjects", //
			"maxCostOfProjects", "stdCostOfProjects");

		super.getResponse().addData(dataset);
	}

}
