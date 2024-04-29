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

package acme.features.administrator.administratorDashboard;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Administrator;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.forms.AdministratorDashboard;

@Service
public class AdministratorAdministratorDashboardShowService extends AbstractService<Administrator, AdministratorDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAdministratorDashboardRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		AdministratorDashboard dashboard;

		Map<String, Integer> numberOfPrincipalsByRole;
		Double ratioOfNoticesWithEmailAdressAndLink;
		Double ratioOfCriticalObjectives;
		Double ratioOfNonCriticalObjectives;

		Double avgRiskValue;
		Double minRiskValue;
		Double maxRiskValue;
		Double stdRiskValue;

		Integer minNumberOfClaims;
		Integer maxNumberOfClaims;
		Double stdNumberOfClaims;

		Date startDate;
		Date endDate;

		List<Integer> numberOfClaimsPerWeek;

		numberOfPrincipalsByRole = new HashMap<String, Integer>();

		numberOfPrincipalsByRole.put("Administrator", this.repository.numberOfPrincipalsWithAdministratorRole().intValue());
		numberOfPrincipalsByRole.put("Auditor", this.repository.numberOfPrincipalsWithAuditorRole().intValue());
		numberOfPrincipalsByRole.put("Client", this.repository.numberOfPrincipalsWithClientRole().intValue());
		numberOfPrincipalsByRole.put("Consumer", this.repository.numberOfPrincipalsWithConsumerRole().intValue());
		numberOfPrincipalsByRole.put("Developer", this.repository.numberOfPrincipalsWithDeveloperRole().intValue());
		numberOfPrincipalsByRole.put("Manager", this.repository.numberOfPrincipalsWithManagerRole().intValue());
		numberOfPrincipalsByRole.put("Provider", this.repository.numberOfPrincipalsWithProviderRole().intValue());
		numberOfPrincipalsByRole.put("Sponsor", this.repository.numberOfPrincipalsWithSponsorRole().intValue());

		ratioOfNoticesWithEmailAdressAndLink = this.repository.ratioOfNoticesWithEmailAdressAndLink();
		ratioOfCriticalObjectives = this.repository.ratioOfCriticalObjectives();
		ratioOfNonCriticalObjectives = this.repository.ratioOfNonCriticalObjectives();

		avgRiskValue = this.repository.avgRiskValue();
		minRiskValue = this.repository.minRiskValue();
		maxRiskValue = this.repository.maxRiskValue();
		stdRiskValue = this.repository.stdRiskValue();

		endDate = MomentHelper.getCurrentMoment();
		numberOfClaimsPerWeek = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			startDate = MomentHelper.deltaFromMoment(endDate, -1, ChronoUnit.WEEKS);
			numberOfClaimsPerWeek.add(this.repository.numberOfClaimsBetweenDates(startDate, endDate).intValue());
			endDate = startDate;
		}

		Double avgNumberOfClaims = numberOfClaimsPerWeek.stream().mapToDouble(x -> x).average().orElse(0.);
		minNumberOfClaims = numberOfClaimsPerWeek.stream().mapToInt(x -> x).min().orElse(0);
		maxNumberOfClaims = numberOfClaimsPerWeek.stream().mapToInt(x -> x).max().orElse(0);
		stdNumberOfClaims = numberOfClaimsPerWeek.stream().mapToDouble(x -> x).map(x -> Math.pow(x - avgNumberOfClaims, 2) / 2).sum();

		dashboard = new AdministratorDashboard();

		dashboard.setNumberOfPrincipalsByRole(numberOfPrincipalsByRole);
		dashboard.setRatioOfNoticesWithEmailAdressAndLink(ratioOfNoticesWithEmailAdressAndLink);
		dashboard.setRatioOfCriticalObjectives(ratioOfCriticalObjectives);
		dashboard.setRatioOfNonCriticalObjectives(ratioOfNonCriticalObjectives);

		dashboard.setAvgRiskValue(avgRiskValue);
		dashboard.setMinRiskValue(minRiskValue);
		dashboard.setMaxRiskValue(maxRiskValue);
		dashboard.setStdRiskValue(stdRiskValue);

		dashboard.setAvgNumberOfClaims(avgNumberOfClaims);
		dashboard.setMinNumberOfClaims(minNumberOfClaims);
		dashboard.setMaxNumberOfClaims(maxNumberOfClaims);
		dashboard.setStdNumberOfClaims(stdNumberOfClaims);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final AdministratorDashboard object) {
		Dataset dataset;

		dataset = super.unbind(object, //
			"numberOfPrincipalsByRole", "ratioOfNoticesWithEmailAdressAndLink", //
			"ratioOfCriticalObjectives", "ratioOfNonCriticalObjectives", //
			"avgRiskValue", "minRiskValue", //
			"maxRiskValue", "stdRiskValue", //
			"avgNumberOfClaims", "minNumberOfClaims", //
			"maxNumberOfClaims", "stdNumberOfClaims");

		super.getResponse().addData(dataset);
	}

}
