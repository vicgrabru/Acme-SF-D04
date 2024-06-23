/*
 * SponsorSponsorDashboardShowService.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.sponsor.sponsorDashboard;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.datatypes.Money;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.configuration.SystemConfiguration;
import acme.forms.SponsorDashboard;
import acme.roles.Sponsor;
import acme.utils.MoneyExchangeRepository;
import acme.utils.MoneyUtils;

@Service
public class SponsorSponsorDashboardShowService extends AbstractService<Sponsor, SponsorDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorSponsorDashboardRepository	repository;

	@Autowired
	private MoneyExchangeRepository				exchangeRepo;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

		int sponsorId;
		SponsorDashboard dashboard;

		Integer totalNumberOfInvoicesWithLessOrEqualThan21;

		Integer totalNumberOfSponsorshipsWithLink;

		Money avgAmountOfSponsorships;
		Money devAmountOfSponsorships;
		Money minAmountOfSponsorships;
		Money maxAmountOfSponsorships;

		Money avgQuantityOfInvoices;
		Money devQuantityOfInvoices;
		Money minQuantityOfInvoices;
		Money maxQuantityOfInvoices;

		SystemConfiguration systemConfiguration;
		Collection<Money> amountOfSponsorships;
		Collection<Money> quantityOfInvoices;

		sponsorId = super.getRequest().getPrincipal().getActiveRoleId();

		totalNumberOfInvoicesWithLessOrEqualThan21 = this.repository.totalNumberOfInvoicesWithLessOrEqualThan21(sponsorId);

		totalNumberOfSponsorshipsWithLink = this.repository.totalNumberOfSponsorshipsWithLink(sponsorId);

		systemConfiguration = this.repository.getSystemConfiguration();

		amountOfSponsorships = this.repository.findSponsorshipsAmountBySponsorId(sponsorId).stream().map(a -> this.exchangeRepo.exchangeMoney(a)).toList();
		quantityOfInvoices = this.repository.findInvoicesQuantityBySponsorId(sponsorId).stream().map(q -> this.exchangeRepo.exchangeMoney(q)).toList();

		if (!amountOfSponsorships.isEmpty()) {
			avgAmountOfSponsorships = MoneyUtils.getAvg(amountOfSponsorships, systemConfiguration.getSystemCurrency());
			devAmountOfSponsorships = MoneyUtils.getStd(amountOfSponsorships, systemConfiguration.getSystemCurrency());
			minAmountOfSponsorships = MoneyUtils.getMin(amountOfSponsorships, systemConfiguration.getSystemCurrency());
			maxAmountOfSponsorships = MoneyUtils.getMax(amountOfSponsorships, systemConfiguration.getSystemCurrency());
		} else {
			Money result = new Money();
			result.setAmount(0.0);
			result.setCurrency(systemConfiguration.getSystemCurrency());
			avgAmountOfSponsorships = result;
			devAmountOfSponsorships = result;
			minAmountOfSponsorships = result;
			maxAmountOfSponsorships = result;
		}

		if (!quantityOfInvoices.isEmpty()) {
			avgQuantityOfInvoices = MoneyUtils.getAvg(quantityOfInvoices, systemConfiguration.getSystemCurrency());
			devQuantityOfInvoices = MoneyUtils.getStd(quantityOfInvoices, systemConfiguration.getSystemCurrency());
			minQuantityOfInvoices = MoneyUtils.getMin(quantityOfInvoices, systemConfiguration.getSystemCurrency());
			maxQuantityOfInvoices = MoneyUtils.getMax(quantityOfInvoices, systemConfiguration.getSystemCurrency());
		} else {
			Money result = new Money();
			result.setAmount(0.0);
			result.setCurrency(systemConfiguration.getSystemCurrency());
			avgQuantityOfInvoices = result;
			devQuantityOfInvoices = result;
			minQuantityOfInvoices = result;
			maxQuantityOfInvoices = result;
		}

		dashboard = new SponsorDashboard();

		dashboard.setTotalNumberOfInvoicesWithLessOrEqualThan21(totalNumberOfInvoicesWithLessOrEqualThan21);

		dashboard.setTotalNumberOfSponsorshipsWithLink(totalNumberOfSponsorshipsWithLink);

		if (avgAmountOfSponsorships.getAmount() != 0.0)
			dashboard.setAvgAmountOfSponsorships(avgAmountOfSponsorships);
		if (devAmountOfSponsorships.getAmount() != 0.0)
			dashboard.setDevAmountOfSponsorships(devAmountOfSponsorships);
		if (minAmountOfSponsorships.getAmount() != 0.0)
			dashboard.setMinAmountOfSponsorships(minAmountOfSponsorships);
		if (maxAmountOfSponsorships.getAmount() != 0.0)
			dashboard.setMaxAmountOfSponsorships(maxAmountOfSponsorships);

		if (avgQuantityOfInvoices.getAmount() != 0.0)
			dashboard.setAvgQuantityOfInvoices(avgQuantityOfInvoices);
		if (devQuantityOfInvoices.getAmount() != 0.0)
			dashboard.setDevQuantityOfInvoices(devQuantityOfInvoices);
		if (minQuantityOfInvoices.getAmount() != 0.0)
			dashboard.setMinQuantityOfInvoices(minQuantityOfInvoices);
		if (maxQuantityOfInvoices.getAmount() != 0.0)
			dashboard.setMaxQuantityOfInvoices(maxQuantityOfInvoices);
		super.getBuffer().addData(dashboard);

	}

	@Override
	public void unbind(final SponsorDashboard object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "totalNumberOfInvoicesWithLessOrEqualThan21", "totalNumberOfSponsorshipsWithLink", "avgAmountOfSponsorships", "devAmountOfSponsorships", "minAmountOfSponsorships", "maxAmountOfSponsorships", "avgQuantityOfInvoices",
			"devQuantityOfInvoices", "minQuantityOfInvoices", "maxQuantityOfInvoices");

		super.getResponse().addData(dataset);

	}

}
