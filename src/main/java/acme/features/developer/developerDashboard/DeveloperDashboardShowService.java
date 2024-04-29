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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.training.TrainingModule;
import acme.entities.training.TrainingSession;
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
		Map<Date, Integer> numberOfTrainingModulesPerUpdateMoment = new HashMap<>();
		for (TrainingModule t : this.repository.findTrainingModules())
			if (numberOfTrainingModulesPerUpdateMoment.containsKey(t.getUpdateMoment()))
				numberOfTrainingModulesPerUpdateMoment.put(t.getUpdateMoment(), numberOfTrainingModulesPerUpdateMoment.get(t.getUpdateMoment()) + 1);
			else
				numberOfTrainingModulesPerUpdateMoment.put(t.getUpdateMoment(), 1);
		Map<String, Integer> numberOfTrainingSessionPerLink = new HashMap<>();
		for (TrainingSession t : this.repository.findTrainingSessions())
			if (numberOfTrainingSessionPerLink.containsKey(t.getLink()))
				numberOfTrainingSessionPerLink.put(t.getLink(), numberOfTrainingSessionPerLink.get(t.getLink()) + 1);
			else
				numberOfTrainingSessionPerLink.put(t.getLink(), 1);
		dashboard.setNumberOfTrainingModulesPerUpdateMoment(numberOfTrainingModulesPerUpdateMoment);
		dashboard.setNumberOfTrainingSessionPerLink(numberOfTrainingSessionPerLink);
		Double avgTrainingModuleTime = this.repository.averageTrainingModuleTime();
		Double devTrainingModuleTime = this.repository.deviationTrainingModuleTime();
		Double minTrainingModuleTime = this.repository.minTrainingModuleTime();
		Double maxTrainingModuleTime = this.repository.maxTrainingModuleTime();
		dashboard.setAverageTrainingModuleTime(avgTrainingModuleTime);
		dashboard.setDeviationTrainingModuleTime(devTrainingModuleTime);
		dashboard.setMaximumTrainingModuleTime(maxTrainingModuleTime);
		dashboard.setMinimumTrainingModuleTime(minTrainingModuleTime);
		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final DeveloperDashboard object) {
		Dataset dataset;

		dataset = super.unbind(object, "numberOfTrainingModulesPerUpdateMoment", "numberOfTrainingSessionPerLink", "averageTrainingModuleTime", "deviationTrainingModuleTime", "maximumTrainingModuleTime", "minimumTrainingModuleTime");

		super.getResponse().addData(dataset);
	}

}
