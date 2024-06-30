/*
 * SponsorSponsorshipUpdateService.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.sponsor.sponsorship;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.datatypes.Money;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.project.Project;
import acme.entities.sponsorship.Invoice;
import acme.entities.sponsorship.Sponsorship;
import acme.entities.sponsorship.SponsorshipType;
import acme.roles.Sponsor;
import acme.utils.MoneyExchangeRepository;

@Service
public class SponsorSponsorshipUpdateService extends AbstractService<Sponsor, Sponsorship> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorSponsorshipRepository	repository;

	@Autowired
	private MoneyExchangeRepository			exchangeRepo;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Sponsorship sponsorship;
		Sponsor sponsor;

		masterId = super.getRequest().getData("id", int.class);
		sponsorship = this.repository.findOneSponsorshipById(masterId);
		sponsor = sponsorship == null ? null : sponsorship.getSponsor();
		status = sponsorship != null && sponsorship.isDraftMode() && super.getRequest().getPrincipal().hasRole(sponsor);

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Sponsorship object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneSponsorshipById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Sponsorship object) {
		assert object != null;

		int projectId;
		Project project;

		projectId = super.getRequest().getData("project", int.class);
		project = this.repository.findOneProjectById(projectId);

		super.bind(object, "startDuration", "endDuration", "amount", "type", "email", "link");
		object.setProject(project);
	}

	@Override
	public void validate(final Sponsorship object) {
		assert object != null;
		String currencies;
		Collection<Invoice> invoices;

		invoices = this.repository.findManyPublishedInvoicesBySponsorshipId(object.getId());

		if (!super.getBuffer().getErrors().hasErrors("amount")) {
			List<String> invoicesCurrencies = invoices.stream().map(i -> i.getQuantity().getCurrency()).toList();
			boolean sameCurrency = invoicesCurrencies.stream().allMatch(c -> c.equals(object.getAmount().getCurrency()));
			super.state(sameCurrency, "amount", "sponsor.sponsorship.form.error.amount.currencyMustMatchCurrenciesOfTheInvoices");
		}
		if (!super.getBuffer().getErrors().hasErrors("amount")) {
			currencies = this.repository.findAcceptedCurrencies();
			super.state(currencies.contains(object.getAmount().getCurrency()), "amount", "sponsor.sponsorship.form.error.amount.invalid-currency");
		}
		if (!super.getBuffer().getErrors().hasErrors("amount"))
			super.state(object.getAmount().getAmount() >= 0., "amount", "sponsor.sponsorship.form.error.amount.no-negative");

		if (!super.getBuffer().getErrors().hasErrors("startDuration"))
			super.state(MomentHelper.isAfter(object.getStartDuration(), object.getMoment()), "startDuration", "sponsor.sponsorship.form.error.durationAfter");

		if (!super.getBuffer().getErrors().hasErrors("endDuration"))
			super.state(MomentHelper.isLongEnough(object.getStartDuration(), object.getEndDuration(), 1, ChronoUnit.MONTHS), "endDuration", "sponsor.sponsorship.form.error.atLeast1MonthLong");

	}

	@Override
	public void perform(final Sponsorship object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Sponsorship object) {
		assert object != null;

		Collection<Project> projects;
		Collection<Invoice> invoices;
		SelectChoices choicesProject;
		SelectChoices choicesType;
		Dataset dataset;

		invoices = this.repository.findManyPublishedInvoicesBySponsorshipId(object.getId());
		projects = this.repository.findAllProjects();
		choicesProject = SelectChoices.from(projects, "code", object.getProject());
		choicesType = SelectChoices.from(SponsorshipType.class, object.getType());
		dataset = super.unbind(object, "code", "moment", "startDuration", "endDuration", "amount", "type", "email", "link", "draftMode");
		dataset.put("project", choicesProject.getSelected().getKey());
		dataset.put("projects", choicesProject);
		dataset.put("types", choicesType);

		if (object.getAmount() != null) {
			Double totalAmount = invoices.stream().mapToDouble(i -> i.totalAmount().getAmount()).sum();
			Money InvoicesAmount = new Money();
			InvoicesAmount.setAmount(totalAmount);
			InvoicesAmount.setCurrency(object.getAmount().getCurrency());
			dataset.put("totalAmountOfInvoices", InvoicesAmount);
			Money eb1 = this.exchangeRepo.exchangeMoney(InvoicesAmount);
			dataset.put("exchangedTotalAmountOfInvoices", eb1);
		}

		if (object.getAmount() != null) {
			Money eb = this.exchangeRepo.exchangeMoney(object.getAmount());
			dataset.put("exchangedAmount", eb);
		}
		dataset.put("readOnlyCode", true);

		super.getResponse().addData(dataset);
	}
}
