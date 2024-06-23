/*
 * DeveloperDashboardShowService.java
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

import java.util.ArrayList;
import java.util.List;

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
		double avgTrainingModuleTime = 0.;
		double devTrainingModuleTime = 0.;
		double minTrainingModuleTime = Double.MAX_VALUE;
		double maxTrainingModuleTime = Double.MIN_VALUE;
		Integer numberOfTrainingModulesWithUpdateMoment = this.repository.numberOfTrainingModulesWithUpdateMoment(super.getRequest().getPrincipal().getActiveRoleId());
		Integer numberOfTrainingSessionWithLink = this.repository.numberOfTrainingSessionWithLink(super.getRequest().getPrincipal().getActiveRoleId());
		List<TrainingModule> trainingModules = new ArrayList<>(this.repository.findTrainingModules(super.getRequest().getPrincipal().getActiveRoleId()));
		for (TrainingModule tm : trainingModules) {
			long seconds = tm.getTotalTime();
			if (seconds < minTrainingModuleTime)
				minTrainingModuleTime = seconds;
			if (seconds > maxTrainingModuleTime)
				maxTrainingModuleTime = seconds;
			avgTrainingModuleTime += seconds;
		}
		avgTrainingModuleTime = avgTrainingModuleTime / trainingModules.size();
		double devSum = 0.;
		for (TrainingModule tm : trainingModules) {
			long seconds = tm.getTotalTime();
			devSum += (seconds - avgTrainingModuleTime) * (seconds - avgTrainingModuleTime);
		}
		devTrainingModuleTime = Math.sqrt(devSum / trainingModules.size());
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
		List<TrainingModule> trainingModules = new ArrayList<>(this.repository.findTrainingModules(super.getRequest().getPrincipal().getActiveRoleId()));
		List<TrainingSession> trainingSessions = new ArrayList<>(this.repository.findTrainingSessions(super.getRequest().getPrincipal().getActiveRoleId()));
		dataset = super.unbind(object, "numberOfTrainingModulesWithUpdateMoment", "numberOfTrainingSessionWithLink", "averageTrainingModuleTime", "deviationTrainingModuleTime", "maximumTrainingModuleTime", "minimumTrainingModuleTime");
		if (trainingModules.isEmpty()) {
			dataset.put("numberOfTrainingModulesWithUpdateMoment", " ");
			dataset.put("numberOfTrainingModulesWithUpdateMoment", " ");
			dataset.put("averageTrainingModuleTime", " ");
			dataset.put("deviationTrainingModuleTime", " ");
			dataset.put("maximumTrainingModuleTime", " ");
			dataset.put("minimumTrainingModuleTime", " ");
		}
		if (trainingSessions.isEmpty())
			dataset.put("numberOfTrainingSessionWithLink", " ");
		super.getResponse().addData(dataset);
	}

}
