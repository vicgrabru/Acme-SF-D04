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
import java.util.stream.Collectors;

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
	private DeveloperDashboardRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		DeveloperDashboard dashboard = new DeveloperDashboard();
		Double avgTrainingModuleTime = this.repository.avgTrainingModuleTime(super.getRequest().getPrincipal().getActiveRoleId());
		Double devTrainingModuleTime = this.repository.devTrainingModuleTime(super.getRequest().getPrincipal().getActiveRoleId());
		Double minTrainingModuleTime = this.repository.minTrainingModuleTime(super.getRequest().getPrincipal().getActiveRoleId());
		Double maxTrainingModuleTime = this.repository.maxTrainingModuleTime(super.getRequest().getPrincipal().getActiveRoleId());
		Integer numberOfTrainingModulesWithUpdateMoment = this.repository.numberOfTrainingModulesWithUpdateMoment(super.getRequest().getPrincipal().getActiveRoleId());
		Integer numberOfTrainingSessionWithLink = this.repository.numberOfTrainingSessionWithLink(super.getRequest().getPrincipal().getActiveRoleId());
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
		List<TrainingModule> trainingModules = new ArrayList<>(this.repository.findTrainingModules(super.getRequest().getPrincipal().getActiveRoleId())).stream().filter(tm -> !tm.isDraftMode()).collect(Collectors.toList());
		List<TrainingSession> trainingSessions = new ArrayList<>(this.repository.findTrainingSessions(super.getRequest().getPrincipal().getActiveRoleId())).stream().filter(ts -> !ts.isDraftMode()).collect(Collectors.toList());
		dataset = super.unbind(object, "numberOfTrainingModulesWithUpdateMoment", "numberOfTrainingSessionWithLink", "averageTrainingModuleTime", "deviationTrainingModuleTime", "maximumTrainingModuleTime", "minimumTrainingModuleTime");
		if (trainingModules.isEmpty()) {
			dataset.put("numberOfTrainingModulesWithUpdateMoment", "-");
			dataset.put("averageTrainingModuleTime", "-");
			dataset.put("deviationTrainingModuleTime", "-");
			dataset.put("maximumTrainingModuleTime", "-");
			dataset.put("minimumTrainingModuleTime", "-");
		}
		if (trainingSessions.isEmpty())
			dataset.put("numberOfTrainingSessionWithLink", "-");
		super.getResponse().addData(dataset);
	}

}
