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

package acme.features.developer.developerDashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.forms.DeveloperDashboard;
import acme.roles.Developer;

@Service
public class DeveloperDashboardShowService extends AbstractService<Developer, DeveloperDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private DeveloperDashBoardRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		DeveloperDashboard dashboard = new DeveloperDashboard();
		Double avgTrainingModuleTime = this.repository.averageTrainingModuleTime();
		Double devTrainingModuleTime = this.repository.deviationTrainingModuleTime();
		Double minTrainingModuleTime = this.repository.minTrainingModuleTime();
		Double maxTrainingModuleTime = this.repository.maxTrainingModuleTime();
		Integer numberOfTrainingModulesWithUpdateMoment = this.repository.numberOfTrainingModulesWithUpdateMoment();
		Integer numberOfTrainingSessionWithLink = this.repository.numberOfTrainingSessionWithLink();
		dashboard.setAverageTrainingModuleTime(avgTrainingModuleTime);
		dashboard.setDeviationTrainingModuleTime(devTrainingModuleTime);
		dashboard.setMaximumTrainingModuleTime(maxTrainingModuleTime);
		dashboard.setMinimumTrainingModuleTime(minTrainingModuleTime);
		dashboard.setNumberOfTrainingModulesWithUpdateMoment(numberOfTrainingModulesWithUpdateMoment);
		dashboard.setNumberOfTrainingSessionWithLink(numberOfTrainingSessionWithLink);
		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final DeveloperDashboard object) {
		Dataset dataset;

		dataset = super.unbind(object, "numberOfTrainingModulesWithUpdateMoment", "numberOfTrainingSessionWithLink", "averageTrainingModuleTime", "deviationTrainingModuleTime", "maximumTrainingModuleTime", "minimumTrainingModuleTime");

		super.getResponse().addData(dataset);
	}

}
